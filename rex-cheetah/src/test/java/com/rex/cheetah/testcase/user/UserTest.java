package com.rex.cheetah.testcase.user;

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
import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.ProtocolEntity;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.entity.RegistryEntity;
import com.rex.cheetah.common.entity.UserEntity;
import com.rex.cheetah.common.entity.UserFactory;
import com.rex.cheetah.common.entity.UserOperation;
import com.rex.cheetah.common.object.ObjectPoolFactory;
import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesManager;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.registry.RegistryExecutor;
import com.rex.cheetah.registry.RegistryInitializer;
import com.rex.cheetah.registry.zookeeper.ZookeeperRegistryExecutor;
import com.rex.cheetah.registry.zookeeper.ZookeeperRegistryInitializer;

public class UserTest {
    private static final Logger LOG = LoggerFactory.getLogger(UserTest.class);

    @Test
    public void test() throws Exception {
        CheetahProperties properties = CheetahPropertiesManager.getProperties();
        ObjectPoolFactory.initialize(properties);
        ThreadPoolFactory.initialize(properties);
        // RedisSentinelPoolFactory.initialize(properties);

        RegistryEntity registryEntity = new RegistryEntity();
        registryEntity.setAddress("localhost:2181");
        
        ProtocolEntity protocolEntity = new ProtocolEntity();
        protocolEntity.setType(ProtocolType.NETTY);

        RegistryInitializer registryInitializer = new ZookeeperRegistryInitializer();
        registryInitializer.start(registryEntity, properties);
        
        RegistryExecutor registryExecutor = new ZookeeperRegistryExecutor();
        registryExecutor.setRegistryInitializer(registryInitializer);
        registryExecutor.setProtocolEntity(protocolEntity);

        UserEntity administrator = UserFactory.createAdministrator();
        registryExecutor.persistUser(administrator);
        
        UserEntity admin_user1 = UserFactory.createAdminUser("admin_user1", "111111");
        registryExecutor.persistUser(admin_user1);
        
        UserEntity admin_user2 = UserFactory.createAdminUser("admin_user2", "111111");
        registryExecutor.persistUser(admin_user2);
        
        UserEntity admin_user3 = UserFactory.createAdminUser("admin_user3", "111111");
        registryExecutor.persistUser(admin_user3);
        
        UserEntity user1 = UserFactory.createUser("user1", "111111", Arrays.asList(UserOperation.SERVICE_CONTROL, UserOperation.SERVICE_CONTROL));
        registryExecutor.persistUser(user1);
        
        UserEntity user2 = UserFactory.createUser("user2", "111111", Arrays.asList(UserOperation.REMOTE_CONTROL));
        registryExecutor.persistUser(user2);
        
        UserEntity user3 = UserFactory.createUser("user3", "111111", Arrays.asList(UserOperation.SAFETY_CONTROL));
        registryExecutor.persistUser(user3);
        
        UserEntity user4 = UserFactory.createUser("user4", "111111", Arrays.asList(UserOperation.SERVICE_CONTROL, UserOperation.REMOTE_CONTROL));
        registryExecutor.persistUser(user4);
        
        UserEntity user5 = UserFactory.createUser("user5", "111111", Arrays.asList(UserOperation.REMOTE_CONTROL, UserOperation.SAFETY_CONTROL));
        registryExecutor.persistUser(user5);
        
        UserEntity user6 = UserFactory.createUser("user6", "111111", new ArrayList<UserOperation>());
        registryExecutor.persistUser(user6);
        
        LOG.info(registryExecutor.retrieveUserList().toString());
        
        /*UserEntity admin_user4 = UserFactory.createAdminUser("admin", "111111");
        registryExecutor.persistUser(admin_user4);
        
        UserEntity user7 = UserFactory.createUser("user7", "111111", Arrays.asList(UserOperation.USER_CONTROL));
        registryExecutor.persistUser(user7);
        
        UserEntity user8 = UserFactory.createUser("admin", "111111", Arrays.asList(UserOperation.SERVICE_CONTROL));
        registryExecutor.persistUser(user8);
        
        registryExecutor.deleteUser(administrator);*/
    }
}