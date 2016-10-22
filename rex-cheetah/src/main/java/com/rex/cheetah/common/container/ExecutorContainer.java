package com.rex.cheetah.common.container;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.List;

import com.rex.cheetah.monitor.MonitorExecutor;
import com.rex.cheetah.protocol.ClientExecutor;
import com.rex.cheetah.protocol.ClientExecutorAdapter;
import com.rex.cheetah.protocol.ClientInterceptorAdapter;
import com.rex.cheetah.protocol.ServerExecutor;
import com.rex.cheetah.protocol.ServerExecutorAdapter;
import com.rex.cheetah.registry.RegistryExecutor;
import com.rex.cheetah.registry.RegistryInitializer;
import com.rex.cheetah.security.SecurityExecutor;
import com.rex.cheetah.cluster.consistency.ConsistencyExecutor;
import com.rex.cheetah.cluster.loadbalance.LoadBalanceExecutor;

public class ExecutorContainer {
    // 服务端启动适配器
    private ServerExecutorAdapter serverExecutorAdapter;
    
    // 客户端启动适配器
    private ClientExecutorAdapter clientExecutorAdapter;
    
    // 客户端调用拦截适配器
    private ClientInterceptorAdapter clientInterceptorAdapter;
    
    // 服务端启动器
    private ServerExecutor serverExecutor;
    
    // 客户端启动器
    private ClientExecutor clientExecutor;
    
    // 注册中心初始器
    private RegistryInitializer registryInitializer;
    
    // 注册中心执行器
    private RegistryExecutor registryExecutor;
    
    // 集群同步器
    private ConsistencyExecutor consistencyExecutor;
    
    // 负载均衡器
    private LoadBalanceExecutor loadBalanceExecutor;
    
    // 安全控制器
    private SecurityExecutor securityExecutor;
    
    // 监控器
    private List<MonitorExecutor> monitorExecutors;
    
    public ServerExecutorAdapter getServerExecutorAdapter() {
        return serverExecutorAdapter;
    }

    public void setServerExecutorAdapter(ServerExecutorAdapter serverExecutorAdapter) {
        this.serverExecutorAdapter = serverExecutorAdapter;
    }

    public ClientExecutorAdapter getClientExecutorAdapter() {
        return clientExecutorAdapter;
    }

    public void setClientExecutorAdapter(ClientExecutorAdapter clientExecutorAdapter) {
        this.clientExecutorAdapter = clientExecutorAdapter;
    }

    public ClientInterceptorAdapter getClientInterceptorAdapter() {
        return clientInterceptorAdapter;
    }

    public void setClientInterceptorAdapter(ClientInterceptorAdapter clientInterceptorAdapter) {
        this.clientInterceptorAdapter = clientInterceptorAdapter;
    }

    public ServerExecutor getServerExecutor() {
        return serverExecutor;
    }

    public void setServerExecutor(ServerExecutor serverExecutor) {
        this.serverExecutor = serverExecutor;
    }

    public ClientExecutor getClientExecutor() {
        return clientExecutor;
    }

    public void setClientExecutor(ClientExecutor clientExecutor) {
        this.clientExecutor = clientExecutor;
    }
    
    public RegistryInitializer getRegistryInitializer() {
        return registryInitializer;
    }

    public void setRegistryInitializer(RegistryInitializer registryInitializer) {
        this.registryInitializer = registryInitializer;
    }
    
    public RegistryExecutor getRegistryExecutor() {
        return registryExecutor;
    }

    public void setRegistryExecutor(RegistryExecutor registryExecutor) {
        this.registryExecutor = registryExecutor;
    }
    
    public ConsistencyExecutor getConsistencyExecutor() {
        return consistencyExecutor;
    }

    public void setConsistencyExecutor(ConsistencyExecutor consistencyExecutor) {
        this.consistencyExecutor = consistencyExecutor;
    }

    public LoadBalanceExecutor getLoadBalanceExecutor() {
        return loadBalanceExecutor;
    }

    public void setLoadBalanceExecutor(LoadBalanceExecutor loadBalanceExecutor) {
        this.loadBalanceExecutor = loadBalanceExecutor;
    }
    
    public SecurityExecutor getSecurityExecutor() {
        return securityExecutor;
    }

    public void setSecurityExecutor(SecurityExecutor securityExecutor) {
        this.securityExecutor = securityExecutor;
    }

    public List<MonitorExecutor> getMonitorExecutors() {
        return monitorExecutors;
    }

    public void setMonitorExecutors(List<MonitorExecutor> monitorExecutors) {
        this.monitorExecutors = monitorExecutors;
    }
}