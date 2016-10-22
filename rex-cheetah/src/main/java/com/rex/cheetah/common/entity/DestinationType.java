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

public enum DestinationType {
    // JMS类型的MQ
    REQUEST_QUEUE_ASYNC("request-queue-async"),
    REQUEST_QUEUE_SYNC("request-queue-sync"),
    RESPONSE_QUEUE_ASYNC("response-queue-async"),
    RESPONSE_QUEUE_SYNC("response-queue-sync"),
    RESPONSE_TOPIC_ASYNC("response-topic-async"),
    
    // 非JMS类型的MQ，例如Kafka
    REQUEST_QUEUE("request-queue"),
    RESPONSE_QUEUE("response-queue"),
    RESPONSE_TOPIC("response-topic");

    private String value;

    private DestinationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static DestinationType fromString(String value) {
        for (DestinationType type : DestinationType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}