package com.rex.cheetah.testcase.algorithm;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.util.RandomUtil;
import com.rex.cheetah.cluster.loadbalance.consistenthash.ketama.DefaultHashAlgorithm;
import com.rex.cheetah.cluster.loadbalance.consistenthash.ketama.KetamaNodeLocator;
import com.rex.cheetah.cluster.loadbalance.consistenthash.ketama.NodeLocator;

public class HashAlgorithmSchedulerTest {
    private static final Logger LOG = LoggerFactory.getLogger(HashAlgorithmSchedulerTest.class);
    public static final Integer NODE_COUNT = 5;
    public static final Integer NODE_ADDED_COUNT = 3;
    public static final Integer NODE_REDUCED_COUNT = 4;

    // 测试在定时器下，每个定时点选择的节点情况
    @Test
    public void test() throws Exception {
        LOG.info("Normal node count : {}", NODE_COUNT);
        final List<ApplicationEntity> nodeList = HashAlgorithmFactory.getNodeList(NODE_COUNT, 1);
        final NodeLocator<ApplicationEntity> locator = new KetamaNodeLocator<ApplicationEntity>(nodeList, DefaultHashAlgorithm.KETAMA_HASH);

        final AtomicInteger times = new AtomicInteger(0);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ApplicationEntity node = locator.getPrimary(RandomUtil.uuidRandom());
                LOG.info("Select node : {}", node);
                
                times.addAndGet(1);
                if (times.get() == 50) {
                    LOG.info("Added node count : {}", NODE_ADDED_COUNT);
                    nodeAdded(locator);
                }
                
                if (times.get() == 100) {
                    LOG.info("Reduced node count : {}", NODE_REDUCED_COUNT);
                    nodeReduced(locator);
                }
            }
        }, 0, 200);

        System.in.read();
    }
    
    private void nodeAdded(NodeLocator<ApplicationEntity> locator) {
        List<ApplicationEntity> nodeList = locator.getAll();
        
        List<ApplicationEntity> nodeAddedList = HashAlgorithmFactory.getNodeList(NODE_ADDED_COUNT, NODE_COUNT + 1);
        nodeList.addAll(nodeAddedList);
        
        locator.updateLocator(nodeList);
    }
    
    private void nodeReduced(NodeLocator<ApplicationEntity> locator) {
        List<ApplicationEntity> nodeList = locator.getAll();
        
        List<ApplicationEntity> nodeReducedList = new ArrayList<ApplicationEntity>();
        for (int i = 0; i < NODE_REDUCED_COUNT; i++) {
            nodeReducedList.add(nodeList.get(i));
        }
        nodeList.removeAll(nodeReducedList);
                
        locator.updateLocator(nodeList);
    }
}