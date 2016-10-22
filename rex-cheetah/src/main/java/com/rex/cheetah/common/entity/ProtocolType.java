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

import org.apache.commons.lang3.StringUtils;

public enum ProtocolType {
    NETTY("netty"),
    HESSIAN("hessian"),
    KAFKA("kafka"),
    ACTIVE_MQ("activemq"),
    TIBCO("tibco");

    private String value;

    private ProtocolType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public boolean isLoadBalanceSupported() {
        return StringUtils.equals(value, NETTY.toString()) || StringUtils.equals(value, HESSIAN.toString());
    }
    
    public static ProtocolType fromString(String value) {
        for (ProtocolType type : ProtocolType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}