package com.rex.cheetah.testcase.tool;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.event.registry.InstanceEventType;
import com.rex.cheetah.event.registry.ReferenceInstanceEvent;
import com.rex.cheetah.event.registry.ServiceInstanceEvent;
import com.rex.cheetah.registry.zookeeper.ZookeeperInstanceEventInterceptor;

public class ConsistencyTest {
    private static final Logger LOG = LoggerFactory.getLogger(ConsistencyTest.class);

    private int serviceEventCount = 0;
    private int referenceEventCount = 0;

    @Test
    public void test() throws Exception {        
        ZookeeperInstanceEventInterceptor eventInterceptor = new ZookeeperInstanceEventInterceptor() {
            @Override
            protected void onEvent(ServiceInstanceEvent event) {
                // 监听Service上下线
                InstanceEventType eventType = event.getEventType();
                String interfaze = event.getInterface();
                ApplicationEntity applicationEntity = event.getApplicationEntity();
                
                switch (eventType) {
                    case ONLINE:
                        LOG.info("服务端 [{}] 上线 ：[{}]", interfaze, applicationEntity);
                        break;
                    case OFFLINE:
                        LOG.info("服务端 [{}] 下线 ：[{}]", interfaze, applicationEntity);
                        break;
                }
                
                serviceEventCount++;
                LOG.info("事件总数={}", serviceEventCount);
            }

            @Override
            protected void onEvent(ReferenceInstanceEvent event) {
                // 监听Reference上下线
                InstanceEventType eventType = event.getEventType();
                String interfaze = event.getInterface();
                ApplicationEntity applicationEntity = event.getApplicationEntity();
                
                switch (eventType) {
                    case ONLINE:
                        LOG.info("客户端 [{}] 上线 ：[{}]", interfaze, applicationEntity);
                        break;
                    case OFFLINE:
                        LOG.info("客户端 [{}] 下线 ：[{}]", interfaze, applicationEntity);
                        break;
                }
                
                referenceEventCount++;
                LOG.info("事件总数={}", referenceEventCount);
            }
        };
        
        // 启动注册中心连接
        eventInterceptor.start("localhost:2181", ProtocolType.NETTY);
        
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setApplication("APP-IOS");
        applicationEntity.setGroup("POA_EA_INF");

        String interfaze1 = "com.rex.cheetah.test.service.UserService";
        eventInterceptor.addServiceInstanceWatcher(interfaze1, applicationEntity);
        eventInterceptor.addReferenceInstanceWatcher(interfaze1, applicationEntity);
        
        String interfaze2 = "com.rex.cheetah.test.service.AnimalService";
        eventInterceptor.addServiceInstanceWatcher(interfaze2, applicationEntity);
        eventInterceptor.addReferenceInstanceWatcher(interfaze2, applicationEntity);

        // 停止注册中心连接
        // eventInterceptor.stop();

        System.in.read();
    }
}