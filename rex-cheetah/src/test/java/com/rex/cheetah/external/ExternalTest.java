package com.rex.cheetah.external;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExternalTest {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:netty-external-context.xml");

        InjectionService injectionService = (InjectionService) applicationContext.getBean("injectionService");
        System.out.println("injectionService : " + injectionService.getUser("Zhangsan"));

        AutowireService autowireService = (AutowireService) applicationContext.getBean("autowireService");
        System.out.println("autowireService : " + autowireService.getUser("Lisi"));
    }
}