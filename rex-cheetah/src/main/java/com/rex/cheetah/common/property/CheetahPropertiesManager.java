package com.rex.cheetah.common.property;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.spi.SpiLoader;

public class CheetahPropertiesManager {
    private static final Logger LOG = LoggerFactory.getLogger(CheetahPropertiesManager.class);
    private static final String DEFAULT_PATH = "cheetah.properties";
    private static final String EXT_PATH = "cheetah-ext.properties";

    private static CheetahProperties properties;
    private static CheetahProperties extProperties;
    private static CheetahProperties remoteProperties;

    static {
        initializeDefaultProperties();
        initializeExtProperties();
    }

    private static void initializeDefaultProperties() {
        try {
            LOG.info("Parse default property config file [{}]", DEFAULT_PATH);

            properties = new CheetahProperties(DEFAULT_PATH);
        } catch (Exception e) {
            LOG.error("Parse default property config file failed for [{}]", DEFAULT_PATH, e);
            e.printStackTrace();
        }
    }

    private static void initializeExtProperties() {
        try {
            LOG.info("Parse ext property config file [{}]", EXT_PATH);

            extProperties = new CheetahProperties(EXT_PATH);
        } catch (Exception e) {
            LOG.warn("Parse ext property config file failed for [{}], maybe file doesn't exist, ignore", EXT_PATH);
        }

        if (properties != null && extProperties != null) {
            LOG.info("Merge ext property configs of [{}] to default property configs", EXT_PATH);

            try {
                properties.mergeProperties(extProperties);
            } catch (Exception e) {
                LOG.warn("Merge ext property configs failed", e);
            }
        }
    }
    
    public static CheetahPropertiesExecutor initializePropertiesExecutor() {
        CheetahPropertiesExecutor propertiesExecutor = null;
        try {
            propertiesExecutor = SpiLoader.load(CheetahPropertiesExecutor.class);
            
            LOG.info("Cheetah properties executor is loaded from spi, class={}", propertiesExecutor.getClass().getName());
        } catch (Exception e) {
            LOG.info("Cheetah properties executor isn't defined from spi, so use Register Center as remote properties storage");
        }
        
        return propertiesExecutor;
    }
    
    public static void initializeRemoteProperties(CheetahPropertiesExecutor propertiesExecutor, ApplicationEntity applicationEntity) throws Exception {        
        if (propertiesExecutor == null) {
            return;
        }
        
        String property = propertiesExecutor.retrieveProperty(applicationEntity);
        if (StringUtils.isEmpty(property)) {
            if (extProperties != null) {
                property = extProperties.getContent();

                propertiesExecutor.persistProperty(property, applicationEntity);
            } else {
                LOG.warn("Local property configs are null, persistence is failed, ignore");
            }
        }

        if (StringUtils.isNotEmpty(property)) {
            remoteProperties = new CheetahProperties(property.getBytes());
            
            LOG.info("Merge remote property configs to default property configs");
            LOG.info("---------------- Remote Property Config ----------------\r\n{}", remoteProperties.getContent());
            LOG.info("--------------------------------------------------------");
            
            try {
                properties.mergeProperties(remoteProperties);
            } catch (Exception e) {
                LOG.warn("Merge remote property configs failed", e);
            }
        } else {
            LOG.warn("Remote property configs are null, use default configs");
        }
    }

    public static CheetahProperties getProperties() {
        return properties;
    }
    
    public static CheetahProperties getExtProperties() {
        return extProperties;
    }
    
    public static CheetahProperties getRemoteProperties() {
        return remoteProperties;
    }
}