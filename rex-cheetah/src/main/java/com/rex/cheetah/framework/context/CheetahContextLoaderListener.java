package com.rex.cheetah.framework.context;

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

import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.rex.cheetah.common.entity.ServiceEntity;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.framework.bean.ApplicationFactoryBean;
import com.rex.cheetah.protocol.hessian.HessianServletGenerator;

public class CheetahContextLoaderListener extends ContextLoaderListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        
        String path = event.getServletContext().getRealPath("");
        
        generateHessianServlet(applicationContext, path);
    }
    
    private void generateHessianServlet(ApplicationContext applicationContext, String path) {
        ApplicationFactoryBean applicationFactoryBean = applicationContext.getBean(ApplicationFactoryBean.class);
        
        Map<String, ServiceEntity> serviceEntityMap = applicationFactoryBean.getCacheContainer().getServiceEntityMap();
        CheetahProperties properties = applicationFactoryBean.getProperties();
        
        HessianServletGenerator servletGenerator = new HessianServletGenerator(serviceEntityMap, properties, path);
        servletGenerator.generate();
    }
}