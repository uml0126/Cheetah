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

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.MonitorEntity;
import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.protocol.apache.ApacheAsyncClientExecutor;
import com.rex.cheetah.serialization.SerializerExecutor;

public class WebServiceMonitorExecutor extends AbstractMonitorExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(WebServiceMonitorExecutor.class);
    
    private CloseableHttpAsyncClient httpAsyncClient;

    @Override
    public void execute(MonitorStat monitorStat) throws Exception {
        MonitorEntity monitorEntity = cacheContainer.getMonitorEntity();
        List<String> addresses = monitorEntity.getAddresses();
        if (CollectionUtils.isEmpty(addresses)) {
            LOG.error("Webservice monitor addresses are null");
            
            return;
        }
        
        for (String address : addresses) {
            if (!address.startsWith("http://")) {
                address = "http://" + address;
            }

            String value = SerializerExecutor.toJson(monitorStat);

            HttpEntity entity = new StringEntity(value, CheetahConstants.ENCODING_FORMAT);

            HttpPost httpPost = new HttpPost(address);
            httpPost.addHeader("content-type", "application/json;charset=" + CheetahConstants.ENCODING_FORMAT);
            httpPost.setEntity(entity);

            HttpAsyncCallback httpAsyncCallback = new HttpAsyncCallback();
            httpAsyncCallback.setHttpPost(httpPost);

            httpAsyncClient.execute(httpPost, httpAsyncCallback);
        }
    }

    @Override
    public void setProperties(CheetahProperties properties) {
        super.setProperties(properties);

        try {
            ApacheAsyncClientExecutor clientExecutor = new ApacheAsyncClientExecutor();
            clientExecutor.initialize(properties);
            httpAsyncClient = clientExecutor.getClient();
        } catch (Exception e) {
            LOG.error("Get htty async client failed", e);
        }
    }

    public class HttpAsyncCallback implements FutureCallback<HttpResponse> {
        private HttpPost httpPost;

        public void setHttpPost(HttpPost httpPost) {
            this.httpPost = httpPost;
        }

        @Override
        public void completed(HttpResponse httpResponse) {
            httpPost.reset();
        }

        @Override
        public void failed(Exception e) {
            httpPost.reset();
            
            LOG.error("Monitor web service invoke failed, url={}", httpPost.getURI(), e);
        }

        @Override
        public void cancelled() {
            httpPost.reset();
        }
    }
}