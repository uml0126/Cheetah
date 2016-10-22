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

public class BException extends RuntimeException {
    private static final long serialVersionUID = 341354494033218328L;

    public BException() {
        super();
    }

    public BException(String message) {
        super(message);
    }

    public BException(String message, Throwable cause) {
        super(message, cause);
    }

    public BException(Throwable cause) {
        super(cause);
    }
}