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

import org.apache.commons.lang3.StringUtils;

public class WebServiceEntity implements Serializable {
    private static final long serialVersionUID = 8598019385005343221L;
    
    private String path;
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (path.indexOf("/") > -1) {
            path = path.replace("/", "");
        }

        this.path = path;
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        
        if (path != null) {
            result = 37 * result + path.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WebServiceEntity)) {
            return false;
        }

        WebServiceEntity webServiceEntity = (WebServiceEntity) object;
        if (StringUtils.equals(this.path, webServiceEntity.path)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(", path=");
        builder.append(path);

        return builder.toString();
    }
}