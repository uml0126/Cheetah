package com.rex.cheetah.external;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;

import com.rex.cheetah.test.service.User;
import com.rex.cheetah.test.service.UserService;

public class AutowireServiceImpl implements AutowireService {
    @Autowired
    private UserService userService;

    @Override
    public User getUser(String name) {
        return userService.getUser(name);
    }
}