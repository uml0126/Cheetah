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

import com.rex.cheetah.common.promise.PromiseEntity;
import com.rex.cheetah.protocol.ProtocolRequest;

public class ResponseAsyncEntity implements Serializable {    
    private static final long serialVersionUID = -253171631908650621L;
    
    private ProtocolRequest request;
    private PromiseEntity<?> promise;

    public ResponseAsyncEntity() {
        
    }

    public ProtocolRequest getRequest() {
        return request;
    }

    public void setRequest(ProtocolRequest request) {
        this.request = request;
    }
    
    public PromiseEntity<?> getPromise() {
        return promise;
    }

    public void setPromise(PromiseEntity<?> promise) {
        this.promise = promise;
    }
}