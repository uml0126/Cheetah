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

import com.rex.cheetah.common.container.CacheContainer;
import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.ProtocolEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.entity.WebServiceEntity;

public class HessianUrlUtil {

    // 转化为URL
    // 例如http://192.168.0.1:8080/Cheetah/hessian/com.rex.cheetah.xxxService，
    // 带interface全路径是为了客户端把interface传送获取，便于解析密钥等缓存
    public static String toUrl(String interfaze, ApplicationEntity applicationEntity, CacheContainer cacheContainer) {
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
        WebServiceEntity webServiceEntity = cacheContainer.getWebServiceEntity();
        String host = applicationEntity.getHost();
        int port = applicationEntity.getPort();
        ProtocolType type = protocolEntity.getType();
        String path = webServiceEntity.getPath();

        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(host);
        builder.append(":");
        builder.append(port);
        builder.append("/");
        builder.append(path);
        builder.append("/");
        builder.append(type);
        builder.append("/");
        builder.append(interfaze);

        return builder.toString();
    }

    public static String toUrl(Class<?> interfaze, ApplicationEntity applicationEntity, CacheContainer cacheContainer) {
        String interfaceName = interfaze.getName();

        return toUrl(interfaceName, applicationEntity, cacheContainer);
    }
    
    // 萃取Interface
    // 例如，http://192.168.0.1:8080/Cheetah/hessian/com.rex.cheetah.xxxService，
    // 萃取为com.rex.cheetah.xxxService
    public static String extractInterface(String url) {        
        return url.substring(url.lastIndexOf("/") + 1);
    }
}