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

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {

    public static int random(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("Max value can't be less than min value");
        }
        Random random = new Random();

        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static int threadLocalRandom(int max) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        return threadLocalRandom.nextInt(max);
    }

    public static String numericRandom(int max) {
        return RandomStringUtils.randomNumeric(max);
    }

    public static String uuidRandom() {
        return UUID.randomUUID().toString();
    }
}