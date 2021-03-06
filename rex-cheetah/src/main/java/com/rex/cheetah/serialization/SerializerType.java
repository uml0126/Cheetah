package com.rex.cheetah.serialization;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

public enum SerializerType {
    FST_BINARY("fstBinarySerializer"),
    KRYO_BINARY("kryoBinarySerializer"),
    JDK_BINARY("jdkBinarySerializer"),   
    JACKSON_JSON("jacksonJsonSerializer"),
    ALI_JSON("aliJsonSerializer"),
    FST_JSON("fstJsonSerializer");

    private String value;

    private SerializerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static SerializerType fromString(String value) {
        for (SerializerType type : SerializerType.values()) {
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