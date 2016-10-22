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

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rex.cheetah.common.constant.CheetahConstants;

public class CEnd1 {    
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        System.setProperty(CheetahConstants.PORT_PARAMETER_NAME, "3000");
        
        // new FileSystemXmlApplicationContext("file://192.168.15.82\\Cheetah\\Trace\\trace-c-context.xml"); 
        // new ClassPathXmlApplicationContext("http://www.rex.com/Cheetah/Trace/trace-c-context.xml");
        new ClassPathXmlApplicationContext("classpath*:trace-c-context.xml");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}