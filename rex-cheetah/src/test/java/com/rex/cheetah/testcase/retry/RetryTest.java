package com.rex.cheetah.testcase.retry;

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
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;

public class RetryTest {
    private static CheetahProperties properties = CheetahPropertiesManager.getProperties();
    private int index = 0;

    @Test
    public void test() throws Exception {
        System.out.println(invoke1(properties.getInteger(CheetahConstants.LOAD_BALANCE_RETRY_TIMES_ATTRIBUTE_NAME)));
        //invoke2(properties.getInteger(CheetahConstants.RETRY_TIMES_ATTRIBUTE_NAME));
    }

    protected String invoke1(int invokeRetryTimes) {
        System.out.println("Touch Server...");
        invokeRetryTimes--;

        if (invokeRetryTimes > -1) {
            System.out.println("Retry times = " + (properties.getInteger(CheetahConstants.LOAD_BALANCE_RETRY_TIMES_ATTRIBUTE_NAME) - invokeRetryTimes));

            return invoke1(invokeRetryTimes);
        }

        return "result";
    }

    protected void invoke2(int invokeRetryTimes) {
        System.out.println("Touch Server...");
        invokeRetryTimes--;

        if (loop() < 7) {
            if (invokeRetryTimes > -1) {
                System.out.println("Retry times = " + (properties.getInteger(CheetahConstants.LOAD_BALANCE_RETRY_TIMES_ATTRIBUTE_NAME) - invokeRetryTimes));

                invoke2(invokeRetryTimes);
            }
        } else {
            System.out.println("result"); 
        }
    }

    private int loop() {
        return index++;
    }
}