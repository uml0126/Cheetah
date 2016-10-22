package com.rex.cheetah.trace.service;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

public class CException extends RuntimeException {
    private static final long serialVersionUID = -4564821783582928675L;

    public CException() {
        super();
    }

    public CException(String message) {
        super(message);
    }

    public CException(String message, Throwable cause) {
        super(message, cause);
    }

    public CException(Throwable cause) {
        super(cause);
    }
}