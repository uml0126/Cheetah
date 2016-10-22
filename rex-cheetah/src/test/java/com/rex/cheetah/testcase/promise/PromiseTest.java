package com.rex.cheetah.testcase.promise;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.jdeferred.AlwaysCallback;
import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.DonePipe;
import org.jdeferred.FailCallback;
import org.jdeferred.ProgressCallback;
import org.jdeferred.Promise;
import org.jdeferred.Promise.State;
import org.jdeferred.impl.DeferredObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PromiseTest {
    private static final Logger LOG = LoggerFactory.getLogger(PromiseTest.class);
    
    @Test
    public void test1() throws Exception {
        Deferred<String, String, Integer> deferred = new DeferredObject<String, String, Integer>();
        Promise<String, String, Integer> promise = deferred.promise();
        promise.done(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                LOG.info("Result : {}", result);
            }
        }).fail(new FailCallback<String>() {
            @Override
            public void onFail(String rejection) {
                System.out.println("Rejection : " + rejection);
            }
        }).progress(new ProgressCallback<Integer>() {
            @Override
            public void onProgress(Integer progress) {
                System.out.println("Progress : " + progress);
            }
        }).always(new AlwaysCallback<String, String>() {
            @Override
            public void onAlways(State state, String result, String rejection) {
                System.out.println("Always : " + result + " " + rejection);
            }
        });
        
        deferred.reject("oops");
        deferred.resolve("done");
        deferred.notify(99);
        
        System.in.read();
    }
    
    @Test
    public void test2() throws Exception {
        Deferred<String, String, Integer> deferred = new DeferredObject<String, String, Integer>();
        Promise<String, String, Integer> promise = deferred.promise();
        promise.then(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                LOG.info("Result : {}", result);
            }          
        }).then(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                LOG.info("Result : {}", result);
            }         
        }).then(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                LOG.info("Result : {}", result);
            }          
        });

        deferred.resolve("a");
        
        System.in.read();
    }
    
    @Test
    public void test3() throws Exception {
        Deferred<String, String, Integer> deferred = new DeferredObject<String, String, Integer>();
        Promise<String, String, Integer> promise = deferred.promise();
        promise.then(new DonePipe<String, String, String, Integer>() {
            @Override
            public Promise<String, String, Integer> pipeDone(String result) {
                LOG.info("Result : {}", result);
                
                return new DeferredObject<String, String, Integer>().resolve("b");
            }            
        }).then(new DonePipe<String, String, String, Integer>() {
            @Override
            public Promise<String, String, Integer> pipeDone(String result) {
                LOG.info("Result : {}", result);
                
                return new DeferredObject<String, String, Integer>().resolve("c");
            }            
        }).then(new DonePipe<String, String, String, Integer>() {
            @Override
            public Promise<String, String, Integer> pipeDone(String result) {
                LOG.info("Result : {}", result);

                return new DeferredObject<String, String, Integer>();
            }            
        });

        deferred.resolve("a");
        
        System.in.read();
    }
}