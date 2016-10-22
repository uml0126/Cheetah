package com.rex.cheetah.testcase.eventbus;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import com.rex.cheetah.event.eventbus.Event;

public class MyEvent extends Event {
    
    public MyEvent(Object source) {
        super(source);
    }
}