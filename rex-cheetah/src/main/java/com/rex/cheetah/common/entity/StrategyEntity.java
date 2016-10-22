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

import java.io.Serializable;

public class StrategyEntity implements Serializable {
    private static final long serialVersionUID = -8333154241480942547L;

    private LoadBalanceType loadBalanceType;

    public LoadBalanceType getLoadBalanceType() {
        return loadBalanceType;
    }

    public void setLoadBalanceType(LoadBalanceType loadBalanceType) {
        this.loadBalanceType = loadBalanceType;
    }

    @Override
    public int hashCode() {
        int result = 17;
        
        if (loadBalanceType != null) {
            result = 37 * result + loadBalanceType.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StrategyEntity)) {
            return false;
        }

        StrategyEntity strategyEntity = (StrategyEntity) object;
        if (this.loadBalanceType == strategyEntity.loadBalanceType) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("loadBalanceType=");
        builder.append(loadBalanceType);

        return builder.toString();
    }
}