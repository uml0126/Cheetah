package com.rex.cheetah.testcase.eventbus;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.rex.cheetah.event.eventbus.EventControllerFactory;
import com.rex.cheetah.event.eventbus.EventControllerType;
import com.rex.cheetah.testcase.http.HttpTest;

public class MySyncSubscriber1 {
    private static final Logger LOG = LoggerFactory.getLogger(HttpTest.class);
    
    public MySyncSubscriber1() {
        EventControllerFactory.getSingletonController(EventControllerType.SYNC).register(this);
    }
    
    @Subscribe
    public void listen(MyEvent event) {
        LOG.info("Listen:{}", event.getSource());
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}