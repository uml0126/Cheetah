package com.rex.cheetah.serialization.compression;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

public enum CompressorType {
    QUICK_LZ_COMPRESSOR("quickLzCompressor");

    private String value;

    private CompressorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static CompressorType fromString(String value) {
        for (CompressorType type : CompressorType.values()) {
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