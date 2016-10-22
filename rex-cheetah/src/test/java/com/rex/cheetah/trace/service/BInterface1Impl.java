package com.rex.cheetah.trace.service;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class BInterface1Impl implements BInterface1 {
    private AInterface aInterface;
    private CInterface cInterface;
    
    private AtomicLong atomicLong = new AtomicLong(1);
    
    public BInterface1Impl() {

    }
    
    @Override
    public void asyncToB(String traceId, String path) {
        path += " -> B";
        
        if (Constants.PRINT) {
            System.out.println("异步：B端收到A端消息：" + path + "，并发送到C端");
        }
        
        if (atomicLong.getAndAdd(1) % 17 == 0) {
            throw new BException("B端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        cInterface.asyncToC(traceId, path);
    }
    
    @Override
    public String syncToB(String traceId, String path) {
        path += " -> B";
        
        if (Constants.PRINT) {
            System.out.println("同步：B端收到A端消息：" + path + "，并发送到C端");
        }
        
        if (atomicLong.getAndAdd(1) % 17 == 0) {
            throw new BException("B端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        path = cInterface.syncToC(traceId, path);
        
        path += " -> B";
        
        if (Constants.PRINT) {
            System.out.println("同步：B端收到C端消息：" + path + "，并返回到A端");
        }
        
        if (atomicLong.getAndAdd(1) % 7 == 0) {
            throw new BException("B端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return path;
    }
    
    @Override
    public String[] async1ToB(String traceId, String path) {
        path += " -> B";
        
        if (Constants.PRINT) {
            System.out.println("异步链式：B端收到A端消息：" + path + "，并Callback到A端");
        }
        
        if (atomicLong.getAndAdd(1) % 17 == 0) {
            throw new BException("B端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return new String[] {traceId, path};
    }
    
    @Override
    public void async2ToB(String traceId, String path) {
        path += " -> B";
        
        if (Constants.PRINT) {
            System.out.println("异步：B端收到A端消息：" + path + "，并发送到C端");
        }
        
        if (atomicLong.getAndAdd(1) % 17 == 0) {
            throw new BException("B端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        path = cInterface.syncToC(traceId, path);
        
        if (Constants.PRINT) {
            System.out.println("同步：B端收到C端消息：" + path + "，并返回到A端");
        }
        
        if (atomicLong.getAndAdd(1) % 7 == 0) {
            throw new BException("B端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        aInterface.asyncToA(traceId, path);
    }
    
    @Override
    public String[] sync1ToB(String[] result) {
        String traceId = result[0];
        String path = result[1] + " -> B";
        
        if (Constants.PRINT) {
            System.out.println("同步链式：B端收到A端消息：" + path + "，并返回到A端");
        }
        
        if (atomicLong.getAndAdd(1) % 17 == 0) {
            throw new BException("B端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return new String[] {traceId, path};
    }
    
    public AInterface getAInterface() {
        return aInterface;
    }

    public void setAInterface(AInterface aInterface) {
        this.aInterface = aInterface;
    }

    public CInterface getCInterface() {
        return cInterface;
    }

    public void setCInterface(CInterface cInterface) {
        this.cInterface = cInterface;
    }
}