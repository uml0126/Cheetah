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

import com.rex.cheetah.common.entity.ApplicationEntity;

public class ApplicationFactoryBean extends AbstractFactoryBean {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationFactoryBean.class);

    private ApplicationEntity applicationEntity;

    @Override
    public void afterPropertiesSet() throws Exception {        
        LOG.info("ApplicationFactoryBean has been initialized...");
    }

    @Override
    public ApplicationEntity getObject() throws Exception {
        return applicationEntity;
    }

    @Override
    public Class<ApplicationEntity> getObjectType() {
        return ApplicationEntity.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setApplicationEntity(ApplicationEntity applicationEntity) {
        this.applicationEntity = applicationEntity;
    }

    public ApplicationEntity getApplicationEntity() {
        return applicationEntity;
    }
}