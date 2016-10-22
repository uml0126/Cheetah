package com.rex.cheetah.event.smtp;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.Properties;

import com.sun.mail.util.MailSSLSocketFactory;

public class SmtpSslExecutor extends SmtpExecutor {

    public SmtpSslExecutor(String host, String user, String password) {
        super(host, user, password);
    }

    @Override
    protected Properties createProperties() {
        Properties properties = super.createProperties();
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");  
        properties.put("mail.smtp.starttls.enable", "true");
        
        MailSSLSocketFactory sslSocketFactory = null;
        try {
            sslSocketFactory = new MailSSLSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }  
        sslSocketFactory.setTrustAllHosts(true);  
        properties.put("mail.smtp.ssl.socketFactory", sslSocketFactory);  

        return properties;
    }
}