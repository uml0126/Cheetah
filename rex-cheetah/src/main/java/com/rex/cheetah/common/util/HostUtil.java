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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HostUtil.class);
    private static final char COLON = ':';
    private static final String LOCAL_HOST = "127.0.0.1";
     
    // 多网卡中获取IP地址
    public static String getLocalhost() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            LOG.error("Socket exception", e);
        }
        while (interfaces.hasMoreElements()) {
            NetworkInterface interfaze = interfaces.nextElement();
            Enumeration<InetAddress> addresses = interfaze.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (!address.isLoopbackAddress() && address.isSiteLocalAddress() && address.getHostAddress().indexOf(COLON) == -1) {
                    return address.getHostAddress();
                }
            }
        }
        
        return LOCAL_HOST;
    }
}