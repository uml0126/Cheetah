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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.util.RandomUtil;
import com.rex.cheetah.registry.zookeeper.common.ZookeeperInvoker;

public class ZookeeperTest {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperTest.class);
    
    private static final String ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 60 * 1000;
    private static final int CONNECT_TIMEOUT = 15 * 1000;
    private static final int CONNECT_WAIT_TIME = 1000;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    
    @Test
    public void testCreate() throws Exception {
        ZookeeperInvoker invoker = new ZookeeperInvoker();
        CuratorFramework client = invoker.create(ADDRESS, SESSION_TIMEOUT, CONNECT_TIMEOUT, CONNECT_WAIT_TIME);
        invoker.startAndBlock(client, 20000, TimeUnit.MILLISECONDS);
        
        String path1 = "/cheetah/application/netty/POA_EA_INF/APP-IOS/service/com.rex.cheetah.test.service.UserService/{\"application\":\"APP-IOS\",\"group\":\"POA_EA_INF\",\"cluster\":\"NettyServerCluster\",\"host\":\"10.11.106.121\",\"port\":6010}";
        create(invoker, client, path1);
        
        String path2 = "/cheetah/application/netty/POA_EA_INF/APP-IOS/service/com.rex.cheetah.test.service.AnimalService/{\"application\":\"APP-IOS\",\"group\":\"POA_EA_INF\",\"cluster\":\"NettyServerCluster\",\"host\":\"10.11.106.121\",\"port\":6010}";
        create(invoker, client, path2);
        
        // invoker.close(client);
        
        System.in.read();
    }
    
    private void create(final ZookeeperInvoker invoker, final CuratorFramework client, final String path) throws Exception {
        for (int i = 0; i < 5000; i++) {
            LOG.info("发送事件={}", i);
            EXECUTOR.execute(new Runnable() {
                public void run() {
                    try {
                        String fullPath = path + RandomUtil.uuidRandom();
                        if (invoker.pathExist(client, fullPath)) {
                            invoker.deletePath(client, fullPath);
                        }
                        invoker.createPath(client, fullPath, CreateMode.EPHEMERAL);
                        // TimeUnit.MILLISECONDS.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Test
    public void testDelete() throws Exception {
        ZookeeperInvoker invoker = new ZookeeperInvoker();
        CuratorFramework client = invoker.create(ADDRESS, SESSION_TIMEOUT, CONNECT_TIMEOUT, CONNECT_WAIT_TIME);
        invoker.startAndBlock(client, 20000, TimeUnit.MILLISECONDS);
        
        invoker.deletePath(client, "/cheetah");
        
        // invoker.close(client);
        
        System.in.read();
    }
}