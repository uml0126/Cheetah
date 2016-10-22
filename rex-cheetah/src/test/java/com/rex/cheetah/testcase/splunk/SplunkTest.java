package com.rex.cheetah.testcase.splunk;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.protocol.apache.ApacheSyncClientExecutor;

public class SplunkTest {
    private static final Logger LOG = LoggerFactory.getLogger(SplunkTest.class);

    private CloseableHttpClient httpSyncClient;
    private String loginUrl = "https://192.168.63.112:8089/services/auth/login";
    private String dataUrl = "https://192.168.63.112:8089/servicesNS/admin/search/search/jobs/export/";

    @Test
    public void test() throws Exception {
        CheetahProperties properties = CheetahPropertiesManager.getProperties();

        ApacheSyncClientExecutor clientExecutor = new ApacheSyncClientExecutor();
        clientExecutor.initialize(properties, true);
        httpSyncClient = clientExecutor.getClient();

        String sessionKey = login();
        get(sessionKey);
    }

    public String login() throws Exception {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("username", "admin"));
        parameters.add(new BasicNameValuePair("password", "Aa123456"));

        HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");

        HttpPost httpPost = new HttpPost(loginUrl);
        httpPost.setEntity(entity);

        HttpResponse httpResponse = httpSyncClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = null;

        try {
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sessionKey = result.substring(result.indexOf("<sessionKey>") + "<sessionKey>".length(), result.lastIndexOf("</sessionKey>"));
        LOG.info("sessionKey is {}", sessionKey);

        return sessionKey;
    }

    public void get(String sessionKey) throws Exception {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("search", "search 'source=\"cheetah.log\" sourcetype=\"cheetah\" traceId=\"A1(1)\"'"));
        // parameters.add(new BasicNameValuePair("output_mode", "json"));

        HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");

        HttpPost httpPost = new HttpPost(dataUrl);
        httpPost.addHeader("Authorization", "Splunk " + sessionKey);
        httpPost.setEntity(entity);

        HttpResponse httpResponse = httpSyncClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = null;

        try {
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info(result);
    }
}