package com.rex.cheetah.framework.parser;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.entity.LoadBalanceType;
import com.rex.cheetah.common.entity.ProtocolEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.entity.StrategyEntity;
import com.rex.cheetah.framework.bean.StrategyFactoryBean;
import com.rex.cheetah.framework.exception.FrameworkException;
import com.rex.cheetah.cluster.consistency.ConsistencyExecutor;
import com.rex.cheetah.cluster.loadbalance.LoadBalanceExecutor;

public class StrategyBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyBeanDefinitionParser.class);

    public StrategyBeanDefinitionParser(CheetahDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        StrategyEntity strategyEntity = new StrategyEntity();
        
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
        ProtocolType protocolType = protocolEntity.getType();
        if (protocolType.isLoadBalanceSupported()) {
            String loadBalanceAttributeName = CheetahConstants.LOAD_BALANCE_ATTRIBUTE_NAME;
            
            String loadBalance = element.getAttribute(loadBalanceAttributeName);
            LoadBalanceType loadBalanceType = null;
            if (StringUtils.isNotEmpty(loadBalance)) {
                loadBalanceType = LoadBalanceType.fromString(loadBalance);
            } else {
                loadBalanceType = LoadBalanceType.CONSISTENT_HASH;
            }

            LOG.info("Load balance type is {}", loadBalanceType);

            strategyEntity.setLoadBalanceType(loadBalanceType);
            LoadBalanceExecutor loadBalanceExecutor = createLoadBalanceExecutor(loadBalanceType);
            builder.addPropertyValue(createBeanName(LoadBalanceExecutor.class), loadBalanceExecutor);
            
            ConsistencyExecutor consistencyExecutor = createConsistencyExecutor();
            builder.addPropertyValue(createBeanName(ConsistencyExecutor.class), consistencyExecutor);
        }
        
        cacheContainer.setStrategyEntity(strategyEntity);
        builder.addPropertyValue(createBeanName(StrategyEntity.class), strategyEntity);
    }
    
    protected LoadBalanceExecutor createLoadBalanceExecutor(LoadBalanceType loadBalanceType) {
        LoadBalanceExecutor loadBalanceExecutor = executorContainer.getLoadBalanceExecutor();
        if (loadBalanceExecutor == null) {
            String consistentHashLoadBalanceExecutorId = CheetahConstants.CONSISTENT_HASH_LOAD_BALANCE_EXECUTOR_ID;
            String roundRobinLoadBalanceExecutorId = CheetahConstants.ROUND_ROBIN_LOAD_BALANCE_EXECUTOR_ID;
            String randomLoadBalanceExecutorId = CheetahConstants.RANDOM_LOAD_BALANCE_EXECUTOR_ID;
            try {
                switch (loadBalanceType) {
                    case CONSISTENT_HASH:
                        loadBalanceExecutor = createDelegate(consistentHashLoadBalanceExecutorId);
                        break;
                    case ROUND_ROBIN:
                        loadBalanceExecutor = createDelegate(roundRobinLoadBalanceExecutorId);
                        break;
                    case RANDOM:
                        loadBalanceExecutor = createDelegate(randomLoadBalanceExecutorId);
                        break;
                }
            } catch (Exception e) {
                throw new FrameworkException("Creat LoadBalanceExecutor failed", e);
            }

            executorContainer.setLoadBalanceExecutor(loadBalanceExecutor);
        }
        
        return loadBalanceExecutor;
    }
    
    protected ConsistencyExecutor createConsistencyExecutor() {
        ConsistencyExecutor consistencyExecutor = executorContainer.getConsistencyExecutor();
        if (consistencyExecutor == null) {
            String consistencyExecutorId = CheetahConstants.CONSISTENCY_EXECUTOR_ID;
            try {
                consistencyExecutor = createDelegate(consistencyExecutorId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ConsistencyExecutor failed", e);
            }
            
            executorContainer.setConsistencyExecutor(consistencyExecutor);
        }
        
        return consistencyExecutor;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return StrategyFactoryBean.class;
    }
}