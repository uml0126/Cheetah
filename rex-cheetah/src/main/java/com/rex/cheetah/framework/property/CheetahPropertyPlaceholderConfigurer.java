package com.rex.cheetah.framework.property;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.Iterator;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.rex.cheetah.common.util.MathsUtil;

public class CheetahPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private Properties properties;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties) throws BeansException {
        super.processProperties(beanFactoryToProcess, properties);
        
        this.properties = properties;
        for (Iterator<?> iterator = properties.keySet().iterator(); iterator.hasNext();) {
            Object key = iterator.next();
            String value = (String) properties.get(key);
            Long result = MathsUtil.calculate(value);
            if (result != null) {
                properties.put(key, result);
            }
        }
    }
    
    public Properties getProperties() {
        return properties;
    }
}