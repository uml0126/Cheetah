package com.rex.cheetah.common.entity;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.rex.cheetah.common.property.CheetahProperties;

public class MQEntity implements Serializable {
    private static final long serialVersionUID = -6314150329885961836L;

    private CheetahProperties properties;
    
    private String queueId;
    private String topicId;
    private String jndiInitialContextFactoryId;
    private String initialConnectionFactoryId;
    private String pooledConnectionFactoryId;
    
    private Map<String, CheetahProperties> propertiesMap;
        
    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }
    
    public String getQueueClass() {
        return properties.getString(queueId);
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
    
    public String getTopicClass() {
        return properties.getString(topicId);
    }

    public String getJndiInitialContextFactoryId() {
        return jndiInitialContextFactoryId;
    }

    public void setJndiInitialContextFactoryId(String jndiInitialContextFactoryId) {
        this.jndiInitialContextFactoryId = jndiInitialContextFactoryId;
    }
    
    public String getJndiInitialContextFactoryClass() {
        return properties.getString(jndiInitialContextFactoryId);
    }

    public String getInitialConnectionFactoryId() {
        return initialConnectionFactoryId;
    }

    public void setInitialConnectionFactoryId(String initialConnectionFactoryId) {
        this.initialConnectionFactoryId = initialConnectionFactoryId;
    }
    
    public String getInitialConnectionFactoryClass() {
        return properties.getString(initialConnectionFactoryId);
    }
    
    public String getPooledConnectionFactoryId() {
        return pooledConnectionFactoryId;
    }

    public void setPooledConnectionFactoryId(String pooledConnectionFactoryId) {
        this.pooledConnectionFactoryId = pooledConnectionFactoryId;
    }
    
    public String getPooledConnectionFactoryClass() {
        return properties.getString(pooledConnectionFactoryId);
    }
    
    public CheetahProperties getProperties() {
        return properties;
    }
    
    public void extractProperties(CheetahProperties properties, ProtocolType protocolType) {
        this.properties = properties;
        this.propertiesMap = properties.extractProperties(protocolType.toString());
    }
    
    public Map<String, CheetahProperties> getPropertiesMap() {        
        return propertiesMap;
    }
    
    @Override
    public int hashCode() {
        int result = 17;

        if (queueId != null) {
            result = 37 * result + queueId.hashCode();
        }

        if (topicId != null) {
            result = 37 * result + topicId.hashCode();
        }

        if (jndiInitialContextFactoryId != null) {
            result = 37 * result + jndiInitialContextFactoryId.hashCode();
        }
        
        if (initialConnectionFactoryId != null) {
            result = 37 * result + initialConnectionFactoryId.hashCode();
        }
        
        if (pooledConnectionFactoryId != null) {
            result = 37 * result + pooledConnectionFactoryId.hashCode();
        }
        
        if (propertiesMap != null) {
            result = 37 * result + propertiesMap.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MQEntity)) {
            return false;
        }

        MQEntity mqEntity = (MQEntity) object;
        if (StringUtils.equals(this.queueId, mqEntity.queueId)
                && StringUtils.equals(this.topicId, mqEntity.topicId)
                && StringUtils.equals(this.jndiInitialContextFactoryId, mqEntity.jndiInitialContextFactoryId)
                && StringUtils.equals(this.initialConnectionFactoryId, mqEntity.initialConnectionFactoryId)
                && StringUtils.equals(this.pooledConnectionFactoryId, mqEntity.pooledConnectionFactoryId)
                && this.propertiesMap.equals(mqEntity.propertiesMap)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("queueId=");
        builder.append(queueId);
        builder.append(", topicId=");
        builder.append(topicId);
        builder.append(", jndiInitialContextFactoryId=");
        builder.append(jndiInitialContextFactoryId);
        builder.append(", initialConnectionFactoryId=");
        builder.append(initialConnectionFactoryId);
        builder.append(", pooledConnectionFactoryId=");
        builder.append(pooledConnectionFactoryId);
        builder.append(", propertiesMap=");
        builder.append(propertiesMap);
        
        return builder.toString();
    }
}