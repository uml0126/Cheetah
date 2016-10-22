package com.rex.cheetah.protocol.hessian;

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

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ConnectionEntity;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.protocol.AbstractClientExecutor;

public class HessianClientExecutor extends AbstractClientExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(HessianClientExecutor.class);

    private HessianAuthProxyFactory proxyFactory = new HessianAuthProxyFactory();

    @Override
    public void start(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        boolean started = started(interfaze, applicationEntity);
        if (started) {
            return;
        }
        
        proxyFactory.setReferenceConfigMap(cacheContainer.getReferenceConfigMap());

        Class<?> interfaceClass = Class.forName(interfaze);

        String url = HessianUrlUtil.toUrl(interfaze, applicationEntity, cacheContainer);

        Object proxy = proxyFactory.create(interfaceClass, url);

        LOG.info("Create proxy for {} successfully", url);

        online(interfaze, applicationEntity, proxy);
    }
    
    @Override
    public void setProperties(CheetahProperties properties) {
        super.setProperties(properties);

        long readTimeout = properties.getLong(CheetahConstants.HESSIAN_READ_TIMEOUT_ATTRIBUTE_NAME);
        long connectTimeout = properties.getLong(CheetahConstants.HESSIAN_CONNECT_TIMEOUT_ATTRIBUTE_NAME);

        proxyFactory.setOverloadEnabled(true);
        proxyFactory.setReadTimeout(readTimeout);
        proxyFactory.setConnectTimeout(connectTimeout);
    }
    
    @Override
    public ConnectionEntity online(String interfaze, ApplicationEntity applicationEntity, Object proxy) throws Exception {
        ConnectionEntity connectionEntity = super.online(interfaze, applicationEntity, proxy);

        String url = HessianUrlUtil.toUrl(interfaze, applicationEntity, cacheContainer);
        connectionEntity.setUrl(url);

        return connectionEntity;
    }
}