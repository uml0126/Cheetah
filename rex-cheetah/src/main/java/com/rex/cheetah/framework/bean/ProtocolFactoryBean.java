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

import com.rex.cheetah.common.entity.ProtocolEntity;

public class ProtocolFactoryBean extends AbstractFactoryBean {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolFactoryBean.class);

    private ProtocolEntity protocolEntity;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("ProtocolFactoryBean has been initialized...");
    }

    @Override
    public ProtocolEntity getObject() throws Exception {
        return protocolEntity;
    }

    @Override
    public Class<ProtocolEntity> getObjectType() {
        return ProtocolEntity.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setProtocolEntity(ProtocolEntity protocolEntity) {
        this.protocolEntity = protocolEntity;
    }

    public ProtocolEntity getProtocolEntity() {
        return protocolEntity;
    }
}