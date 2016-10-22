package com.rex.cheetah.common.callback;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

public abstract class CheetahCallback<T> {
    public void call(T result, Exception exception) {
        if (exception == null) {
            onResult(result);
        } else {
            onError(exception);
        }
    }

    public abstract void onResult(T result);
    
    public abstract void onError(Exception exception);
}