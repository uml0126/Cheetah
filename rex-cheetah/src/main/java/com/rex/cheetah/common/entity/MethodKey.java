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

import com.rex.cheetah.common.util.ClassUtil;

public class MethodKey implements Serializable {
    private static final long serialVersionUID = 3458411349312814646L;
    
    private String method;
    private String parameterTypes;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String parameterTypes) {
        if (StringUtils.isNotEmpty(parameterTypes)) {
            parameterTypes = parameterTypes.replace(" ", "");
        }
        this.parameterTypes = parameterTypes;
    }
    
    public MethodKey clone() {
        MethodKey methodKey = new MethodKey();

        methodKey.setMethod(method);
        methodKey.setParameterTypes(parameterTypes);

        return methodKey;
    }
    
    public static MethodKey create(String method, Class<?>[] parameterClasses) {
        return create(method, toParameterTypes(parameterClasses));
    }

    public static MethodKey create(String method, String parameterTypes) {
        MethodKey methodKey = new MethodKey();

        methodKey.setMethod(method);
        methodKey.setParameterTypes(parameterTypes);

        return methodKey;
    }
    
    public static String toParameterTypes(Class<?>[] parameterClasses) {
        return ClassUtil.convertParameter(parameterClasses);
    }

    @Override
    public int hashCode() {
        int result = 17;
        
        if (method != null) {
            result = 37 * result + method.hashCode();
        }
        
        if (parameterTypes != null) {
            result = 37 * result + parameterTypes.hashCode();
        }
        
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MethodKey)) {
            return false;
        }
        
        MethodKey methodKey = (MethodKey) object;
        if (StringUtils.equals(this.method, methodKey.method)
                && StringUtils.equals(this.parameterTypes, methodKey.parameterTypes)) {
            return true;
        }
        
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("method=");
        builder.append(method);
        builder.append(", parameterTypes=");
        builder.append(parameterTypes);
        
        return builder.toString();
    }
}