package com.rex.cheetah.protocol.mq;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import javax.jms.Destination;

import com.rex.cheetah.common.entity.ApplicationEntity;
import com.rex.cheetah.common.entity.DestinationEntity;
import com.rex.cheetah.common.entity.DestinationType;
import com.rex.cheetah.common.util.ClassUtil;

public class MQDestinationUtil {
    public static Destination createDestination(String destinationClass, DestinationType destinationType, String interfaze, ApplicationEntity applicationEntity) throws Exception {
        DestinationEntity destinationEntity = new DestinationEntity(destinationType, interfaze, applicationEntity);

        return ClassUtil.createInstance(destinationClass, destinationEntity.toString());
    }
}