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

import com.facebook.util.compress.QuickLz;

public class CompressorExecutor {
    public static byte[] compress(byte[] bytes) {
        return compress(bytes, CompressorFactory.getCompressorType());
    }

    public static byte[] decompress(byte[] bytes) {
        return decompress(bytes, CompressorFactory.getCompressorType());
    }

    public static byte[] compress(byte[] bytes, CompressorType type) {
        if (type == CompressorType.QUICK_LZ_COMPRESSOR) {
            return QuickLz.compress(bytes);
        } else {
            throw new CompressorException("Invalid compressor type : " + type);
        }
    }

    public static byte[] decompress(byte[] bytes, CompressorType type) {
        if (type == CompressorType.QUICK_LZ_COMPRESSOR) {
            return QuickLz.decompress(bytes);
        } else {
            throw new CompressorException("Invalid compressor type : " + type);
        }
    }
}