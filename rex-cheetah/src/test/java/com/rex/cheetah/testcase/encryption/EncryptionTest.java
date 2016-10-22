package com.rex.cheetah.testcase.encryption;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.util.EncryptionUtil;

public class EncryptionTest {
    private static final Logger LOG = LoggerFactory.getLogger(EncryptionTest.class);

    @Test
    public void test() throws Exception {
        String password = "abcd1234";
        LOG.info("MD5={}", EncryptionUtil.encryptMD5(password));
        LOG.info("SHA={}", EncryptionUtil.encryptSHA(password));
        LOG.info("SHA256={}", EncryptionUtil.encryptSHA256(password));
        LOG.info("SHA512={}", EncryptionUtil.encryptSHA512(password));
    }
}