package com.rex.cheetah.cluster.loadbalance.random;

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

import com.rex.cheetah.common.entity.ConnectionEntity;
import com.rex.cheetah.common.util.RandomUtil;
import com.rex.cheetah.cluster.loadbalance.AbstractLoadBalanceExecutor;

public class RandomLoadBalanceExecutor extends AbstractLoadBalanceExecutor {

    @Override
    protected ConnectionEntity loadBalance(String interfaze, List<ConnectionEntity> connectionEntityList) throws Exception {
        int random = RandomUtil.threadLocalRandom(connectionEntityList.size());
        // int random = RandomUtil.random(0, connectionEntityList.size() - 1);

        return connectionEntityList.get(random);
    }
}