package com.rex.cheetah.protocol.redis.sentinel;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.DestinationEntity;

public class RedisDestinationUtil {
    public static DestinationEntity createDestinationEntity(String interfaze, ApplicationEntity applicationEntity) {
        DestinationEntity destinationEntity = new DestinationEntity(interfaze, applicationEntity);

        return destinationEntity;
    }
}