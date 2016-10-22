package com.rex.cheetah.security;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.ServiceEntity;

public class SecurityBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityBootstrap.class);

    private AtomicBoolean start = new AtomicBoolean(false);
    private ScheduledExecutorService executor;

    private Map<String, ServiceEntity> serviceEntityMap;

    public SecurityBootstrap() {
        
    }
    
    public void setServiceEntityMap(Map<String, ServiceEntity> serviceEntityMap) {
        this.serviceEntityMap = serviceEntityMap;
    }

    public synchronized void start(final int frequency) {
        if (!start.get() && frequency > 0) {
            try {
                executor = Executors.newScheduledThreadPool(1);
                executor.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (Map.Entry<String, ServiceEntity> entry : serviceEntityMap.entrySet()) {
                                String interfaze = entry.getKey();
                                ServiceEntity serviceEntity = entry.getValue();
                                serviceEntity.resetToken();

                                long defaultToken = serviceEntity.getDefaultToken().get();

                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Refreshing token successfully, service={}, frequency={} ms, token={}", interfaze, frequency, defaultToken);
                                }
                            }
                        } catch (Exception e) {
                            LOG.warn("Refreshing token failed", e);
                        }
                    }
                }, 0, frequency, TimeUnit.MILLISECONDS);

                LOG.info("Refreshing token task has started, frequency={} ms", frequency);
            } finally {
                start.set(true);
            }
        }
    }

    public synchronized void stop() {
        if (start.get()) {
            try {
                if (executor != null) {
                    executor.shutdownNow();

                    LOG.info("Refreshing token task has stopped");
                }
            } finally {
                start.set(false);
            }
        }
    }

    public synchronized void restart(int frequency) {
        if (start.get()) {
            stop();
        }
        
        start(frequency);
    }
}