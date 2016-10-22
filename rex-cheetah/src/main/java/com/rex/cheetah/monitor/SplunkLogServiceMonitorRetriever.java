package com.rex.cheetah.monitor;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.common.property.CheetahProperties;
import com.splunk.JobExportArgs;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;
import com.splunk.ServiceArgs;

public class SplunkLogServiceMonitorRetriever extends AbstractMonitorRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(SplunkLogServiceMonitorRetriever.class);
    
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:m:ss";
    public static final String DATE_FORMAT_SPLUNK = "yyyy-MM-dd'T'HH:mm:ss";

    private CheetahProperties properties;
    private Service service;

    public void initialize(CheetahProperties properties) throws Exception {
        this.properties = properties;
        if (service == null) {
            try {
                ServiceArgs loginArgs = new ServiceArgs();
                loginArgs.setHost(properties.getString(CheetahConstants.SPLUNK_HOST_ATTRIBUTE_NAME));
                loginArgs.setPort(properties.getInteger(CheetahConstants.SPLUNK_PORT_ATTRIBUTE_NAME));
                loginArgs.setUsername(properties.getString(CheetahConstants.SPLUNK_USER_NAME_ATTRIBUTE_NAME));
                loginArgs.setPassword(properties.getString(CheetahConstants.SPLUNK_PASSWORD_ATTRIBUTE_NAME));
                loginArgs.setSSLSecurityProtocol(SSLSecurityProtocol.TLSv1_2);

                service = Service.connect(loginArgs);
            } catch (Exception e) {
                LOG.error("Initialize Splunk connection failed", e);
                dispose();
            }
        }
    }
    
    public void dispose() {
        service = null;
    }
    
    public boolean enabled() {
        return service != null;
    }
    
    public List<MonitorStat> retrieve(String traceId) throws Exception {
        return retrieve(traceId, null);
    }
    
    public List<MonitorStat> retrieve(String traceId, Map<String, Object> conditions) throws Exception {
        return retrieve(traceId, conditions, CheetahConstants.ENCODING_FORMAT);
    }

    public List<MonitorStat> retrieve(String traceId, Map<String, Object> conditions, String encoding) throws Exception {
        if (StringUtils.isEmpty(traceId)) {
            throw new MonitorException("Trace ID is null");
        }
        
        if (service == null) {
            throw new MonitorException("Splunk service is null");
        }

        String sourceType = properties.getString(CheetahConstants.NAMESPACE_ELEMENT_NAME);
        int maximumTime = properties.getInteger(CheetahConstants.SPLUNK_MAXIMUM_TIME_ATTRIBUTE_NAME);
        String earliestTime = null;
        String latestTime = null;
        if (MapUtils.isNotEmpty(conditions)) {
            Object sourceTypeObject = conditions.get(CheetahConstants.SPLUNK_SOURCE_TYPE_ATTRIBUTE_NAME);
            if (sourceTypeObject != null) {
                sourceType = sourceTypeObject.toString();
            }
            
            Object maximumTimeObject = conditions.get(CheetahConstants.SPLUNK_MAXIMUM_TIME_ATTRIBUTE_NAME);
            if (maximumTimeObject != null) {
                maximumTime = (Integer) maximumTimeObject;
            }
            
            Object earliestTimeObject = conditions.get(CheetahConstants.SPLUNK_EARLIEST_TIME_ATTRIBUTE_NAME);
            if (earliestTimeObject != null) {
                earliestTime = new SimpleDateFormat(DATE_FORMAT_SPLUNK).format((Date) earliestTimeObject);
            }


            Object latestTimeObject = conditions.get(CheetahConstants.SPLUNK_LATEST_TIME_ATTRIBUTE_NAME);
            if (latestTimeObject != null) {
                latestTime = new SimpleDateFormat(DATE_FORMAT_SPLUNK).format((Date) latestTimeObject);
            }
        }

        JobExportArgs exportArgs = new JobExportArgs();
        exportArgs.setOutputMode(JobExportArgs.OutputMode.JSON);
        exportArgs.setMaximumTime(maximumTime);
        if (StringUtils.isNotEmpty(earliestTime)) {
            exportArgs.setEarliestTime(earliestTime);
        }
        if (StringUtils.isNotEmpty(latestTime)) {
            exportArgs.setLatestTime(latestTime);
        }

        InputStream inputStream = service.export("search sourcetype=\"" + sourceType + "\" " + CheetahConstants.TRACE_ID + "=\"" + traceId + "\"", exportArgs);
        if (inputStream == null) {
            throw new MonitorException("Input stream is null");
        }

        List<MonitorStat> monitorStatList = new ArrayList<MonitorStat>();

        String key = "{\"" + CheetahConstants.TRACE_ID + "\":\"" + traceId + "\"";

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charsets.toCharset(encoding));
        BufferedReader bufferedReader = IOUtils.toBufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            line = line.replace("\\\"", "\"");
            if (line.contains(key)) {
                line = line.substring(line.indexOf(key) + 1);
                line = line.substring(0, line.indexOf("}"));
                line = "{" + line + "}";
                try {
                    MonitorStat monitorStat = create(line);
                    String exception = monitorStat.getException();
                    if (StringUtils.isNotEmpty(exception)) {
                        exception = exception.replace("\\r\\n\\t", "\r\n\t").replace("\\r\\n", "\r\n");
                        monitorStat.setException(exception);
                    }
                    monitorStatList.add(monitorStat);
                } catch (Exception e) {
                    LOG.error("Create MonitorStat failed", e);
                }
            }
            line = bufferedReader.readLine();
        }

        sort(monitorStatList);

        IOUtils.closeQuietly(bufferedReader);
        IOUtils.closeQuietly(inputStreamReader);
        IOUtils.closeQuietly(inputStream);

        return monitorStatList;
    }
}