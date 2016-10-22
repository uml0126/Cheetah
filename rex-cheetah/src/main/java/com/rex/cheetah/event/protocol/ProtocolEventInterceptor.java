package com.rex.cheetah.event.protocol;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Rex
 * @email uml0126@126.com
 * @version 1.0
 */

import com.google.common.eventbus.Subscribe;
import com.rex.cheetah.event.eventbus.EventControllerFactory;
import com.rex.cheetah.event.eventbus.EventControllerType;

public abstract class ProtocolEventInterceptor {
    public ProtocolEventInterceptor() {
        EventControllerFactory.getSingletonController(EventControllerType.ASYNC).register(this);
    }

    @Subscribe
    public void listen(ProtocolEvent event) {
        onEvent(event);
    }
    
    protected abstract void onEvent(ProtocolEvent event);
}