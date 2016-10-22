package com.rex.cheetah.common.config;

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

import org.apache.commons.lang3.StringUtils;

public class ApplicationConfig implements Serializable {
    private static final long serialVersionUID = -6769132534750910991L;

    private String application;
    private String group;
    private int frequency;
    
    private byte[] lock = new byte[0];

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
    
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        synchronized (lock) {
            this.frequency = frequency;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (application != null) {
            result = 37 * result + application.hashCode();
        }

        if (group != null) {
            result = 37 * result + group.hashCode();
        }

        result = 37 * result + frequency;

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ApplicationConfig)) {
            return false;
        }

        ApplicationConfig applicationConfig = (ApplicationConfig) object;
        if (StringUtils.equals(this.application, applicationConfig.application)
                && StringUtils.equals(this.group, applicationConfig.group)
                && this.frequency == applicationConfig.frequency) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("application=");
        builder.append(application);
        builder.append(", group=");
        builder.append(group);
        builder.append(", frequency=");
        builder.append(frequency);
        
        return builder.toString();
    }
}