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

import org.junit.Test;

import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.event.eventbus.EventControllerFactory;
import com.rex.cheetah.event.eventbus.EventControllerType;

public class MySubscriberTest {

    @Test
    public void testAsync() throws Exception {        
        CheetahProperties properties = CheetahPropertiesManager.getProperties();
        ThreadPoolFactory.initialize(properties);
        
        new MySyncSubscriber1();
        new MySyncSubscriber2();
        new MyAsyncSubscriber1();
        new MyAsyncSubscriber2();
        
        new Runnable() {
            @Override
            public void run() {
                EventControllerFactory.getSingletonController(EventControllerType.SYNC).post(new MyEvent("A"));
            } 
        }.run();
        
        new Runnable() {
            @Override
            public void run() {
                EventControllerFactory.getSingletonController(EventControllerType.ASYNC).post(new MyEvent("B"));
            } 
        }.run();

        System.in.read();
    }
}