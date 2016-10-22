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
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.util.HostUtil;
import com.rex.cheetah.framework.bean.ApplicationFactoryBean;
import com.rex.cheetah.framework.exception.FrameworkException;
import com.rex.cheetah.framework.exception.FrameworkExceptionFactory;

@SuppressWarnings("all")
public class ApplicationBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationBeanDefinitionParser.class);

    public ApplicationBeanDefinitionParser(CheetahDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        String namespaceElementName = properties.getString(CheetahConstants.NAMESPACE_ELEMENT_NAME);
        String applicationElementName = CheetahConstants.APPLICATION_ELEMENT_NAME;
        String applicationAttributeName = CheetahConstants.APPLICATION_ATTRIBUTE_NAME;
        String groupAttributeName = CheetahConstants.GROUP_ATTRIBUTE_NAME;
        String clusterAttributeName = CheetahConstants.CLUSTER_ATTRIBUTE_NAME;
        String hostAttributeName = CheetahConstants.HOST_ATTRIBUTE_NAME;
        String portAttributeName = CheetahConstants.PORT_ATTRIBUTE_NAME;
        String hostParameterName = CheetahConstants.HOST_PARAMETER_NAME;
        String portParameterName = CheetahConstants.PORT_PARAMETER_NAME;

        String application = element.getAttribute(applicationAttributeName);
        if (StringUtils.isEmpty(application)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, applicationElementName, applicationAttributeName);
        }

        LOG.info("Application is {}", application);
        
        String group = element.getAttribute(groupAttributeName);
        if (StringUtils.isEmpty(group)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, applicationElementName, groupAttributeName);
        }
        
        LOG.info("Group is {}", group);
        
        String cluster = element.getAttribute(clusterAttributeName);
        if (StringUtils.isEmpty(cluster)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, applicationElementName, clusterAttributeName);
        }
        
        LOG.info("Cluster is {}", cluster);

        // 通过-DCheetahHost获取值
        String host = System.getProperty(hostParameterName);
        if (StringUtils.isEmpty(host)) {
            host = element.getAttribute(hostAttributeName);
            if (StringUtils.isEmpty(host)) {
                host = HostUtil.getLocalhost();
            }
        }
        
        LOG.info("Host is {}", host);

        // 通过-DCheetahPort获取值
        String port = System.getProperty(portParameterName);
        if (StringUtils.isEmpty(port)) {
            port = element.getAttribute(portAttributeName);
            if (StringUtils.isEmpty(port)) {
                throw new FrameworkException("Port value is null");
            }
        }
        
        LOG.info("Port is {}", port);
        
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setApplication(application);
        applicationEntity.setGroup(group);
        applicationEntity.setCluster(cluster);
        applicationEntity.setHost(host);
        applicationEntity.setPort(Integer.parseInt(port));

        cacheContainer.setApplicationEntity(applicationEntity);
        builder.addPropertyValue(createBeanName(ApplicationEntity.class), applicationEntity);
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ApplicationFactoryBean.class;
    }
}