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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private List<User> users;

    public UserServiceImpl() {
        users = new ArrayList<User>();
        users.add(EntityFactory.createUser1());
        users.add(EntityFactory.createUser2());
    }

    @Override
    public User getUser(String name) {     
        for (User user : users) {
            if (StringUtils.equals(user.getName(), name)) {
                LOG.info("服务端-返回处理结果：返回值=User name={}, address={}, phone={}", user.getName(), user.getAddress(), user.getPhone());
                return user;
            }
        }
        
        return null;
    }
    
    @Override
    public User getUser(String name, int age) {   
        for (User user : users) {
            if (StringUtils.equals(user.getName(), name) && user.getAge() == age) {
                LOG.info("服务端-返回处理结果：返回值=User name={}, address={}, phone={}", user.getName(), user.getAddress(), user.getPhone());
                return user;
            }
        }
        
        return null;
    }

    @Override
    public List<User> getUsers() {
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    @Override
    public void refreshUsers() {
        LOG.info("服务端-不返回处理结果");
        
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}