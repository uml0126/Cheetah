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

public enum ActionType {
    SYSTEM("system"),
    PRODUCE("produce"),
    CONSUME("consume");

    private String value;

    private ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static ActionType fromString(String value) {
        for (ActionType type : ActionType.values()) {
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