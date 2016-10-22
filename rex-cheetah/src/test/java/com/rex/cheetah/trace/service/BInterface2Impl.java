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

public class BInterface2Impl implements BInterface2 {
    private AInterface aInterface;
    
    private AtomicLong atomicLong = new AtomicLong(1);
    
    public BInterface2Impl() {

    }
    
    @Override
    public void asyncToB(String traceId, String path) {
        path += " -> B";
        
        if (Constants.PRINT) {
            System.out.println("异步：B端收到C端消息：" + path + "，并发送到A端");
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
    
    public AInterface getAInterface() {
        return aInterface;
    }

    public void setAInterface(AInterface aInterface) {
        this.aInterface = aInterface;
    }
}