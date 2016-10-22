package com.rex.cheetah.testcase.time;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.time.SystemClock;

public class SystemClockTest {
    private static final Logger LOG = LoggerFactory.getLogger(SystemClockTest.class);
    
    @Test
    public void test() throws Exception {
        long precision = 10L;
        SystemClock clock = new SystemClock(precision);

        TimeUnit.MILLISECONDS.sleep(precision * 2);

        long nowFromSystem = System.currentTimeMillis();
        long nowFromClock = clock.now();
        
        LOG.info("{}", nowFromClock - nowFromSystem);
    }
}