package com.rex.cheetah.protocol.apache;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.property.CheetahProperties;

public class ApacheAsyncClientExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(ApacheAsyncClientExecutor.class);
    
    private CloseableHttpAsyncClient httpAsyncClient;

    public void initialize(final CheetahProperties properties) throws Exception {
        final CyclicBarrier barrier = new CyclicBarrier(2);
        Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                            .setIoThreadCount(CheetahConstants.CPUS)
                            .setConnectTimeout(properties.getInteger(CheetahConstants.APACHE_CONNECT_TIMEOUT_ATTRIBUTE_NAME))
                            .setSoTimeout(properties.getInteger(CheetahConstants.APACHE_SO_TIMEOUT_ATTRIBUTE_NAME))
                            .setSndBufSize(properties.getInteger(CheetahConstants.APACHE_SNDBUF_SIZE_ATTRIBUTE_NAME))
                            .setRcvBufSize(properties.getInteger(CheetahConstants.APACHE_RCVBUF_SIZE_ATTRIBUTE_NAME))
                            .setBacklogSize(properties.getInteger(CheetahConstants.APACHE_BACKLOG_SIZE_ATTRIBUTE_NAME))
                            .setTcpNoDelay(true)
                            .setSoReuseAddress(true)
                            .setSoKeepAlive(true)
                            .build();
                    ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
                    PoolingNHttpClientConnectionManager httpManager = new PoolingNHttpClientConnectionManager(ioReactor);
                    httpManager.setMaxTotal(CheetahConstants.CPUS * properties.getInteger(CheetahConstants.APACHE_MAX_TOTAL_ATTRIBUTE_NAME));

                    httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(httpManager).build();
                    httpAsyncClient.start();
                    
                    LOG.info("Create apache async client successfully");
                    
                    barrier.await();
                } catch (IOReactorException e) {
                    LOG.error("Create apache async client failed", e);
                }

                return null;
            }
        });

        barrier.await(properties.getLong(CheetahConstants.APACHE_CONNECT_TIMEOUT_ATTRIBUTE_NAME) * 2, TimeUnit.MILLISECONDS);
    }

    public CloseableHttpAsyncClient getClient() {
        return httpAsyncClient;
    }
}