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

public class AInterfaceImpl implements AInterface {
    public AInterfaceImpl() {

    }
    
    @Override
    public void asyncToA(String traceId, String path) {
        path += " -> A";
        
        if (Constants.PRINT) {
            System.out.println("异步：A端收到B端消息：" + path);
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}