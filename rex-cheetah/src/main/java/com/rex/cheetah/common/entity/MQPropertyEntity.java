package com.rex.cheetah.common.entity;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.rex.cheetah.common.property.CheetahProperties;
import com.rex.cheetah.common.property.CheetahPropertiesEntity;
import com.rex.cheetah.protocol.ProtocolException;

public class MQPropertyEntity extends CheetahPropertiesEntity {
    private static final long serialVersionUID = 2169644660559195520L;

    private String interfaze;
    private String server;

    private MQEntity mqEntity;

    public MQPropertyEntity(String interfaze, String server, MQEntity mqEntity) {
        super(mqEntity.getProperties());

        this.interfaze = interfaze;
        this.server = server;
        this.mqEntity = mqEntity;
    }

    public MQEntity getMQEntity() {
        return mqEntity;
    }

    @Override
    public CheetahProperties getSubProperties() throws Exception {
        Map<String, CheetahProperties> propertiesMap = mqEntity.getPropertiesMap();

        // 无法从配置文件找到MQ的服务器配置，抛出异常
        if (MapUtils.isEmpty(propertiesMap)) {
            throw new ProtocolException("No Server configs can be retrieved in config file");
        }

        CheetahProperties mqProperties = null;
        if (StringUtils.isEmpty(server)) {
            // 如果Service或者Reference节点未配置MQ服务器，而配置文件配置了多于1个的配置，就无法确认选用哪个配置，抛出异常
            if (propertiesMap.size() > 1) {
                throw new ProtocolException("Server must be specified, because more than 1 server configs are retrieved in config file, interface=" + interfaze);
            }
            Iterator<CheetahProperties> iterator = propertiesMap.values().iterator();
            if (iterator.hasNext()) {
                mqProperties = iterator.next();
            }
        } else {
            mqProperties = propertiesMap.get(server);
            // 如果Service或者Reference节点配置了MQ服务器，而配置文件缺找不到相应的配置，抛出异常
            if (mqProperties == null) {
                throw new ProtocolException("No Server configs can't be retrieved in config file, server=" + server + ", interface=" + interfaze);
            }
        }

        return mqProperties;
    }
}