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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.common.property.CheetahProperties;

public class SmtpEventFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SmtpEventFactory.class);
    
    public static void initialize(CheetahProperties properties) {
        boolean smtpNotification = properties.getBoolean(CheetahConstants.SMTP_NOTIFICATION_ATTRIBUTE_NAME);
        if (smtpNotification) {
            new SmtpEventInterceptor(properties);
            
            LOG.info("Smtp mail notification is enabled...");
        }
    }
}