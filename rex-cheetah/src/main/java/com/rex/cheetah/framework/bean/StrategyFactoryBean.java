package com.rex.cheetah.framework.bean;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.StrategyEntity;
import com.rex.cheetah.cluster.consistency.ConsistencyExecutor;
import com.rex.cheetah.cluster.loadbalance.LoadBalanceExecutor;

public class StrategyFactoryBean extends AbstractFactoryBean {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyFactoryBean.class);

    private StrategyEntity strategyEntity;
    private LoadBalanceExecutor loadBalanceExecutor;
    private ConsistencyExecutor consistencyExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("StrategyFactoryBean has been initialized...");
    }

    @Override
    public StrategyEntity getObject() throws Exception {
        return strategyEntity;
    }

    @Override
    public Class<StrategyEntity> getObjectType() {
        return StrategyEntity.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setStrategyEntity(StrategyEntity strategyEntity) {
        this.strategyEntity = strategyEntity;
    }

    public StrategyEntity getStrategyEntity() {
        return strategyEntity;
    }

    public void setLoadBalanceExecutor(LoadBalanceExecutor loadBalanceExecutor) {
        this.loadBalanceExecutor = loadBalanceExecutor;
    }
    
    public LoadBalanceExecutor getLoadBalanceExecutor() {
        return loadBalanceExecutor;
    }
    
    public void setConsistencyExecutor(ConsistencyExecutor consistencyExecutor) {
        this.consistencyExecutor = consistencyExecutor;
    }

    public ConsistencyExecutor getConsistencyExecutor() {
        return consistencyExecutor;
    }
}