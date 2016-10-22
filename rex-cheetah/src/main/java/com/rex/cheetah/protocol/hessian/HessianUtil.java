package com.rex.cheetah.protocol.hessian;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HessianUtil {
    public static boolean isConnectionException(Throwable e) {
        if (e instanceof ConnectException) {
            return true;
        } else {
            Throwable throwable = e.getCause();
            if (throwable != null) {
                return isConnectionException(throwable);
            } else {
                return false;
            }
        }
    }
    
    public static boolean available(String interfaze, String address, int connectTimeout) {
        int status = -1;
        try {
            URL url = new URL(address);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(connectTimeout);

            status = connection.getResponseCode();
        } catch (Exception e) {
            return false;
        }

        return status != -1;
    }
}