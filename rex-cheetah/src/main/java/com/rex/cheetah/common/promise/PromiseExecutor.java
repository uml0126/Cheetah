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

public class PromiseExecutor extends PromiseEntity<Void> {

    public Deferred<Void, Exception, Void> execute() {
        return resolve(null);
    }
}