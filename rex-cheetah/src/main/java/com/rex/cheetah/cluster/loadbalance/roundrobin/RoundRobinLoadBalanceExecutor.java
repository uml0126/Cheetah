package com.rex.cheetah.cluster.loadbalance.roundrobin;

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
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Maps;
import com.rex.cheetah.common.entity.ConnectionEntity;
import com.rex.cheetah.cluster.loadbalance.AbstractLoadBalanceExecutor;

public class RoundRobinLoadBalanceExecutor extends AbstractLoadBalanceExecutor {
    private Map<String, AtomicLong> indexMap = Maps.newConcurrentMap();

    @Override
    protected ConnectionEntity loadBalance(String interfaze, List<ConnectionEntity> connectionEntityList) throws Exception {        
        AtomicLong atomicLong = indexMap.get(interfaze);
        if (atomicLong == null) {
            atomicLong = new AtomicLong(0);
            indexMap.put(interfaze, atomicLong);
        }

        int index = (int) (atomicLong.getAndAdd(1) % connectionEntityList.size());

        return connectionEntityList.get(Math.abs(index));
    }
}