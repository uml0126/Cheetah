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

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class IOUtil {
    public static String read(String path, String encoding) throws Exception {
        String content = null;
        
        InputStream inputStream = null;
        try {
            inputStream = IOUtil.class.getClassLoader().getResourceAsStream(path);

            content = IOUtils.toString(inputStream, encoding);
            
            /*List<String> lines = IOUtils.readLines(inputStream, encoding);
            StringBuilder builder = new StringBuilder();
            for (String line : lines) {
                builder.append(line + "\n");
            }

            content = builder.toString();*/
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
        
        return content;
    }
}