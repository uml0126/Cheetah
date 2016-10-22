package com.rex.cheetah.event.eventbus;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.Collection;

import com.google.common.eventbus.EventBus;

public class EventControllerImpl implements EventController {
    private EventBus eventBus;
    
    public EventControllerImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void register(Object object) {
        eventBus.register(object);
    }

    @Override
    public void unregister(Object object) {
        eventBus.unregister(object);
    }

    @Override
    public void post(Event event) {
        eventBus.post(event);
    }

    @Override
    public void post(Collection<? extends Event> events) {
        for (Event event : events) {
            eventBus.post(event);
        }
    }
}