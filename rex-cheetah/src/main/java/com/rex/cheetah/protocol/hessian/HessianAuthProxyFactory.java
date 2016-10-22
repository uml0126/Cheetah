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

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.client.HessianURLConnectionFactory;
import com.rex.cheetah.common.config.ReferenceConfig;
import com.rex.cheetah.common.constant.CheetahConstants;

public class HessianAuthProxyFactory extends HessianProxyFactory {
    private Map<String, ReferenceConfig> referenceConfigMap;

    public HessianAuthProxyFactory() {
        super();
    }

    public HessianAuthProxyFactory(ClassLoader loader) {
        super(loader);
    }

    @Override
    protected HessianConnectionFactory createHessianConnectionFactory() {
        return new HessianURLConnectionFactory() {
            @Override
            public HessianConnection open(URL url) throws IOException {
                HessianConnection connection = super.open(url);
                
                String interfaze = HessianUrlUtil.extractInterface(url.toString());
                ReferenceConfig referenceConfig = referenceConfigMap.get(interfaze);
                String secretKey = referenceConfig.getSecretKey();
                int version = referenceConfig.getVersion();
                
                connection.addHeader(CheetahConstants.INTERFACE_ATTRIBUTE_NAME, interfaze);
                connection.addHeader(CheetahConstants.SECRET_KEY_ATTRIBUTE_NAME, secretKey);
                connection.addHeader(CheetahConstants.VERSION_ATTRIBUTE_NAME, String.valueOf(version));

                return connection;
            }
        };
    }
    
    protected void setReferenceConfigMap(Map<String, ReferenceConfig> referenceConfigMap) {
        this.referenceConfigMap = referenceConfigMap;
    }
}