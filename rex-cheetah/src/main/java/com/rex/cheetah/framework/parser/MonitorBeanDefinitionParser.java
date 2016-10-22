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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.common.entity.MonitorEntity;
import com.rex.cheetah.common.entity.MonitorType;
import com.rex.cheetah.framework.bean.MonitorFactoryBean;
import com.rex.cheetah.framework.exception.FrameworkException;
import com.rex.cheetah.monitor.MonitorExecutor;

public class MonitorBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorBeanDefinitionParser.class);

    public MonitorBeanDefinitionParser(CheetahDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);
        
        String typeAttributeName = CheetahConstants.TYPE_ATTRIBUTE_NAME;
        
        String type = element.getAttribute(typeAttributeName);        
        List<MonitorType> monitorTypes = null;
        if (StringUtils.isNotEmpty(type)) {
            monitorTypes = new ArrayList<MonitorType>();
            MonitorType monitorType = null;
            String[] typeArray = StringUtils.split(type, ",");
            for (String typeValue : typeArray) {
                monitorType = MonitorType.fromString(typeValue);
                monitorTypes.add(monitorType);
            }
        } else {
            monitorTypes = Arrays.asList(MonitorType.LOG_SERVICE);
        }
        
        LOG.info("Monitor types are {}", monitorTypes);

        MonitorEntity monitorEntity = new MonitorEntity();
        monitorEntity.setTypes(monitorTypes);
                
        cacheContainer.setMonitorEntity(monitorEntity);
        builder.addPropertyValue(createBeanName(MonitorEntity.class), monitorEntity);

        List<MonitorExecutor> monitorExecutors = createMonitorExecutors(monitorTypes);
        builder.addPropertyValue(createBeanName(MonitorExecutor.class) + "s", monitorExecutors);
    }

    protected List<MonitorExecutor> createMonitorExecutors(List<MonitorType> monitorTypes) {
        List<MonitorExecutor> monitorExecutors = executorContainer.getMonitorExecutors();
        if (CollectionUtils.isEmpty(monitorExecutors)) {
            String logServiceMonitorExecutorId = CheetahConstants.LOG_SERVICE_MONITOR_EXECUTOR_ID;
            String redisServiceMonitorExecutorId = CheetahConstants.REDIS_SERVICE_MONITOR_EXECUTOR_ID;
            String webServiceMonitorExecutorId = CheetahConstants.WEB_SERVICE_MONITOR_EXECUTOR_ID;
            
            monitorExecutors = new ArrayList<MonitorExecutor>();
            MonitorExecutor monitorExecutor = null;
            try {
                for (MonitorType monitorType : monitorTypes) {
                    switch (monitorType) {
                        case LOG_SERVICE:
                            monitorExecutor = createDelegate(logServiceMonitorExecutorId);
                            break;
                        case REDIS_SERVICE:
                            monitorExecutor = createDelegate(redisServiceMonitorExecutorId);
                            break;
                        case WEB_SERVICE:
                            monitorExecutor = createDelegate(webServiceMonitorExecutorId);
                            break;
                    }
                    monitorExecutors.add(monitorExecutor);
                }
            } catch (Exception e) {
                throw new FrameworkException("Creat MonitorExecutors failed", e);
            }

            executorContainer.setMonitorExecutors(monitorExecutors);
        }

        return monitorExecutors;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return MonitorFactoryBean.class;
    }
}