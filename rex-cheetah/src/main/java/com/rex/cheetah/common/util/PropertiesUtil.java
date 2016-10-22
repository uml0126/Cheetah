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
import java.net.URL;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class PropertiesUtil {
    public static String read(String path, String encoding) throws Exception {
        return read(new PropertiesConfiguration(path), encoding);
    }
    
    public static String read(PropertiesConfiguration configuration, String encoding) throws Exception {
        String content = null;
        
        File file = configuration.getFile();
        if (file != null) {
            content = FileUtils.readFileToString(file, encoding);
        } else {
            URL url = configuration.getURL();
            content = IOUtils.toString(url, encoding);
        }

        return content;
    }
}