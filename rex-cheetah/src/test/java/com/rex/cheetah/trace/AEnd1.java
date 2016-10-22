package com.rex.cheetah.trace;

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
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.trace.service.BInterface1;
import com.rex.cheetah.trace.service.Constants;

public class AEnd1 {    
    // 跨服务器两次异步调用(和AEnd2组成集群)
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        System.setProperty(CheetahConstants.PORT_PARAMETER_NAME, "1000");
        
        // ApplicationContext applicationContext = new FileSystemXmlApplicationContext("file://192.168.15.82\\Cheetah\\Trace\\trace-a-context.xml"); 
        // ApplicationContext applicationContext = new ClassPathXmlApplicationContext("http://www.rex.com/Cheetah/Trace/trace-a-context.xml");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:trace-a-context.xml");
        BInterface1 bInterface1 = (BInterface1) applicationContext.getBean("bInterface1");
        for (int i = 0; i < Constants.COUNT; i++) {
            String traceId = "A1(" + i + ")";
            bInterface1.asyncToB(traceId, traceId);
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}