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

public enum RegistryType {
    ZOOKEEPER("zookeeper");

    private String value;

    private RegistryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static RegistryType fromString(String value) {
        for (RegistryType type : RegistryType.values()) {
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