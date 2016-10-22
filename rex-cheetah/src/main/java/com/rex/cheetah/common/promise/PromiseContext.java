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

public class PromiseContext {
    private static final ThreadLocal<PromiseEntity<?>> PROMISE = new ThreadLocal<PromiseEntity<?>>();

    public static PromiseEntity<?> currentPromise() {
        PromiseEntity<?> promise = PROMISE.get();
        PROMISE.remove();

        return promise;
    }

    @SuppressWarnings("unchecked")
    public static <T> PromiseEntity<T> currentPromise(Class<T> genericType) {
        return (PromiseEntity<T>) currentPromise();
    }

    public static void setPromise(PromiseEntity<?> promise) {
        PROMISE.set(promise);
    }
}