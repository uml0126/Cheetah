package com.rex.cheetah.common.util;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;

public class FileUtil {
    public static void forceDelete(File file) throws IOException {
        if (file.exists()) {
            SecureRandom random = new SecureRandom();
            @SuppressWarnings("resource")
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel channel = raf.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
            // Overwrite with zeros
            while (buffer.hasRemaining()) {
                buffer.put((byte) 0);
            }
            buffer.force();
            buffer.rewind();
            // Overwrite with ones
            while (buffer.hasRemaining()) {
                buffer.put((byte) 0xFF);
            }
            buffer.force();
            buffer.rewind();
            // Overwrite with random data; one byte at a time
            byte[] data = new byte[1];
            while (buffer.hasRemaining()) {
                random.nextBytes(data);
                buffer.put(data[0]);
            }
            buffer.force();
            file.delete();
        }
    }
    
    public static String validatePath(String path) {
        path = path.replace("\\", "/");
        if (!path.endsWith("/")) {
            path += "/";
        }
        
        return path;
    }
}