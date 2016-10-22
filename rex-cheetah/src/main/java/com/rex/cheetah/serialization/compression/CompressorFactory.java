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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.property.CheetahProperties;

public class CompressorFactory {
    private static final Logger LOG = LoggerFactory.getLogger(CompressorFactory.class);

    private static CompressorType compressorType = CompressorType.QUICK_LZ_COMPRESSOR;
    private static boolean compress;

    public static void initialize(CheetahProperties properties) {
        String compressor = properties.getString(CheetahConstants.COMPRESSOR_ATTRIBUTE_NAME);
        try {
            compressorType = CompressorType.fromString(compressor);
        } catch (Exception e) {
            LOG.warn("Invalid compressor={}, use default={}", compressor, compressorType);
        }
        LOG.info("Compressor is {}", compressorType);
        
        compress = properties.getBoolean(CheetahConstants.COMPRESS_ATTRIBUTE_NAME);
        LOG.info("Compress is {}", compress);
    }

    public static CompressorType getCompressorType() {
        return compressorType;
    }

    public static void setCompressorType(CompressorType compressorType) {
        CompressorFactory.compressorType = compressorType;
    }
    
    public static boolean isCompress() {
        return compress;
    }
    
    public static void setCompress(boolean compress) {
        CompressorFactory.compress = compress;
    }
}