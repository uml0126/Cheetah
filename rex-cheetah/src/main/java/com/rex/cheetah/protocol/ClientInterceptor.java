package com.rex.cheetah.protocol;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.aopalliance.intercept.MethodInterceptor;

import com.rex.cheetah.common.delegate.CheetahDelegate;

public interface ClientInterceptor extends CheetahDelegate, MethodInterceptor {
    // 设置要调用的接口
    void setInterface(String interfaze);
    
    // 异步调用
    void invokeAsync(ProtocolRequest request) throws Exception;

    // 同步调用
    Object invokeSync(ProtocolRequest request) throws Exception;

    // 广播调用
    void invokeBroadcast(ProtocolRequest request) throws Exception;
}