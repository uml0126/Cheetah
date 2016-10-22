package com.rex.cheetah.testcase.smtp;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.junit.Test;

import com.rex.cheetah.common.constant.CheetahConstants;
import com.rex.cheetah.event.smtp.SmtpExecutor;
import com.rex.cheetah.event.smtp.SmtpSslExecutor;

public class SmtpTest {
    @Test
    public void test() throws Exception {
        String host = "smtp.sohu.com";
        String user = "********@sohu.com";
        String password = "********";
        
        SmtpExecutor smtpExecutor = new SmtpExecutor(host, user, password);
        
        String from = "********@sohu.com";
        String to = "********@sohu.com";
        String cc = "";
        String bcc = "";
        String subject = "Cheetah调用异常通知";
        
        StringBuilder builder = new StringBuilder();
        builder.append("<h2>Exception Notification</h2>");
        builder.append("<br>");
        builder.append("消息1");
        builder.append("<br>");
        builder.append("消息2");
        String text = builder.toString();
        
        smtpExecutor.sendHtml(from, to, cc, bcc, subject, text, CheetahConstants.ENCODING_FORMAT);
    }
    
    @Test
    public void testSsl() throws Exception {
        String host = "smtp.qq.com";
        String user = "********@qq.com";
        String password = "********";

        SmtpSslExecutor smtpExecutor = new SmtpSslExecutor(host, user, password);
        
        String from = "********@qq.com";
        String to = "********@qq.com";
        String cc = "";
        String bcc = "";
        String subject = "Cheetah调用异常通知";

        StringBuilder builder = new StringBuilder();
        builder.append("<h2>Exception Notification</h2>");
        builder.append("<br>");
        builder.append("消息1");
        builder.append("<br>");
        builder.append("消息2");
        String text = builder.toString();
        
        smtpExecutor.sendHtml(from, to, cc, bcc, subject, text, CheetahConstants.ENCODING_FORMAT);
    }
}