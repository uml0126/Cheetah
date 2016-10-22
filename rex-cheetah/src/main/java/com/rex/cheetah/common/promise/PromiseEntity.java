package com.rex.cheetah.common.promise;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PromiseEntity<T> extends DeferredObject<T, Exception, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(PromiseEntity.class);
    
    @Override
    public Deferred<T, Exception, Void> reject(Exception exception) {
        LOG.error("Promise chain invoking is terminated", exception);
        
        return super.reject(exception);
    }
}