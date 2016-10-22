package com.rex.cheetah.common.thread;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Maps;
import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.entity.ApplicationType;
import com.rex.cheetah.common.entity.ThreadQueueType;
import com.rex.cheetah.common.entity.ThreadRejectedPolicyType;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.util.ClassUtil;
import com.rex.cheetah.common.util.StringUtil;

public class ThreadPoolFactory {
    private static CheetahProperties properties;
    
    private static Map<String, ThreadPoolExecutor> threadPoolServerExecutorMap = Maps.newConcurrentMap();
    private static Map<String, ThreadPoolExecutor> threadPoolClientExecutorMap = Maps.newConcurrentMap();
    
    public static void initialize(CheetahProperties properties) {
        ThreadPoolFactory.properties = properties;
    }
    
    public static ThreadPoolExecutor createThreadPoolDefaultExecutor(String interfaze) {
        return createThreadPoolExecutor(interfaze, 
                CheetahConstants.CPUS * 1, 
                CheetahConstants.CPUS * 2, 
                15 * 60 * 1000,
                false);
    }
    
    public static ThreadPoolExecutor createThreadPoolDefaultExecutor() {
        return createThreadPoolExecutor(CheetahConstants.CPUS * 1, 
                CheetahConstants.CPUS * 2, 
                15 * 60 * 1000, 
                false);
    }
        
    public static ThreadPoolExecutor createThreadPoolServerExecutor(String interfaze) {
        try {
            return createThreadPoolExecutor(threadPoolServerExecutorMap, 
                    properties.getBoolean(CheetahConstants.THREAD_POOL_MULTI_MODE_ATTRIBUTE_NAME) ? interfaze : properties.getString(CheetahConstants.NAMESPACE_ELEMENT_NAME) + "-" + ApplicationType.SERVICE, 
                    CheetahConstants.CPUS * properties.getInteger(CheetahConstants.THREAD_POOL_SERVER_CORE_POOL_SIZE_ATTRIBUTE_NAME), 
                    CheetahConstants.CPUS * properties.getInteger(CheetahConstants.THREAD_POOL_SERVER_MAXIMUM_POOL_SIZE_ATTRIBUTE_NAME), 
                    properties.getLong(CheetahConstants.THREAD_POOL_SERVER_KEEP_ALIVE_TIME_ATTRIBUTE_NAME),
                    properties.getBoolean(CheetahConstants.THREAD_POOL_SERVER_ALLOW_CORE_THREAD_TIMEOUT_ATTRIBUTE_NAME));
        } catch (Exception e) {
            throw new IllegalArgumentException("Properties maybe isn't initialized");
        }
    }
    
    public static ThreadPoolExecutor createThreadPoolClientExecutor(String interfaze) {
        try {
            return createThreadPoolExecutor(threadPoolClientExecutorMap, 
                    properties.getBoolean(CheetahConstants.THREAD_POOL_MULTI_MODE_ATTRIBUTE_NAME) ? interfaze : properties.getString(CheetahConstants.NAMESPACE_ELEMENT_NAME) + "-" + ApplicationType.REFERENCE, 
                    CheetahConstants.CPUS * properties.getInteger(CheetahConstants.THREAD_POOL_CLIENT_CORE_POOL_SIZE_ATTRIBUTE_NAME), 
                    CheetahConstants.CPUS * properties.getInteger(CheetahConstants.THREAD_POOL_CLIENT_MAXIMUM_POOL_SIZE_ATTRIBUTE_NAME), 
                    properties.getLong(CheetahConstants.THREAD_POOL_CLIENT_KEEP_ALIVE_TIME_ATTRIBUTE_NAME),
                    properties.getBoolean(CheetahConstants.THREAD_POOL_CLIENT_ALLOW_CORE_THREAD_TIMEOUT_ATTRIBUTE_NAME));
        } catch (Exception e) {
            throw new IllegalArgumentException("Properties maybe isn't initialized");
        }
    }
    
    public static ThreadPoolExecutor createThreadPoolExecutor(final Map<String, ThreadPoolExecutor> threadPoolExecutorMap, final String interfaze, final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final boolean allowCoreThreadTimeOut) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(interfaze);
        if (threadPoolExecutor == null) {
            threadPoolExecutor = createThreadPoolExecutor(interfaze, corePoolSize, maximumPoolSize, keepAliveTime, allowCoreThreadTimeOut);
            threadPoolExecutorMap.put(interfaze, threadPoolExecutor);
        } 

        return threadPoolExecutor;
    }
    
    public static ThreadPoolExecutor createThreadPoolExecutor(final String interfaze, final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final boolean allowCoreThreadTimeOut) {
        final String threadName = StringUtil.firstLetterToUpper(ClassUtil.convertBeanName(interfaze));
        
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                createBlockingQueue(),
                new ThreadFactory() {
                    private AtomicInteger number = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, threadName + "-" + number.getAndIncrement());
                    }
                },
                createRejectedPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);
        
        return threadPoolExecutor;
    }
    
    public static ThreadPoolExecutor createThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, boolean allowCoreThreadTimeOut) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                createBlockingQueue(),
                createRejectedPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);
        
        return threadPoolExecutor;
    }
    
    private static BlockingQueue<Runnable> createBlockingQueue() {
        String queue = properties.getString(CheetahConstants.THREAD_POOL_QUEUE_ATTRIBUTE_NAME);
        ThreadQueueType queueType = ThreadQueueType.fromString(queue);
        
        int queueCapacity = CheetahConstants.CPUS * properties.getInteger(CheetahConstants.THREAD_POOL_QUEUE_CAPACITY_ATTRIBUTE_NAME);
        
        switch (queueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<Runnable>(queueCapacity);
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<Runnable>(queueCapacity);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<Runnable>();
        }
        
        return null;
    }
    
    private static RejectedExecutionHandler createRejectedPolicy() {
        String rejectedPolicy = properties.getString(CheetahConstants.THREAD_POOL_REJECTED_POLICY_ATTRIBUTE_NAME);
        ThreadRejectedPolicyType rejectedPolicyType = ThreadRejectedPolicyType.fromString(rejectedPolicy);
        
        switch (rejectedPolicyType) {
            case BLOCKING_POLICY_WITH_REPORT:
                return new BlockingPolicyWithReport();
            case CALLER_RUNS_POLICY_WITH_REPORT:
                return new CallerRunsPolicyWithReport();
            case ABORT_POLICY_WITH_REPORT:
                return new AbortPolicyWithReport();
            case REJECTED_POLICY_WITH_REPORT:
                return new RejectedPolicyWithReport();
            case DISCARDED_POLICY_WITH_REPORT:
                return new DiscardedPolicyWithReport();
        }
        
        return null;
    }
}