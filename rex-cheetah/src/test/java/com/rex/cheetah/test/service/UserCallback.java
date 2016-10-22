package com.rex.cheetah.test.service;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.callback.CheetahCallback;

public class UserCallback extends CheetahCallback<List<User>> {
    private static final Logger LOG = LoggerFactory.getLogger(UserCallback.class);
    
    @Override
	public void onResult(List<User> result) {
        LOG.info("客户端-异步回调结果：返回值={}", result);
	}

    @Override
    public void onError(Exception exception) {
        LOG.info("客户端-异步回调结果：异常=", exception);
    }
}