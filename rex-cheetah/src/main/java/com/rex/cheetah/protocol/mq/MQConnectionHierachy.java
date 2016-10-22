package com.rex.cheetah.protocol.mq;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.InitializingBean;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.util.ClassUtil;

public class MQConnectionHierachy extends MQHierachy {

    @SuppressWarnings("incomplete-switch")
    @Override
    public void initialize() throws Exception {
        super.initialize();
        
        String initialConnectionFactoryClass = mqPropertyEntity.getMQEntity().getInitialConnectionFactoryClass();
        String url = mqPropertyEntity.getString(CheetahConstants.MQ_URL_ATTRIBUTE_NAME);
        String userName = mqPropertyEntity.getString(CheetahConstants.MQ_USER_NAME_ATTRIBUTE_NAME);
        String password = mqPropertyEntity.getString(CheetahConstants.MQ_PASSWORD_ATTRIBUTE_NAME);
        
        ConnectionFactory targetConnectionFactory = ClassUtil.createInstance(initialConnectionFactoryClass);
        
        switch (protocolType) {
            case ACTIVE_MQ:
                ClassUtil.invoke(targetConnectionFactory, "setBrokerURL", new Class<?>[] {String.class}, new Object[] {url});
                ClassUtil.invoke(targetConnectionFactory, "setUserName", new Class<?>[] {String.class}, new Object[] {userName});
                ClassUtil.invoke(targetConnectionFactory, "setPassword", new Class<?>[] {String.class}, new Object[] {password});
                break;
            case TIBCO:
                ClassUtil.invoke(targetConnectionFactory, "setServerUrl", new Class<?>[] {String.class}, new Object[] {url});
                ClassUtil.invoke(targetConnectionFactory, "setUserName", new Class<?>[] {String.class}, new Object[] {userName});
                ClassUtil.invoke(targetConnectionFactory, "setUserPassword", new Class<?>[] {String.class}, new Object[] {password});
                break;
        }
        
        if (targetConnectionFactory instanceof InitializingBean) {
            InitializingBean initializingBean = (InitializingBean) targetConnectionFactory;
            initializingBean.afterPropertiesSet();
        }
        
        setTargetConnectionFactory(targetConnectionFactory);

        afterPropertiesSet();
    }
}