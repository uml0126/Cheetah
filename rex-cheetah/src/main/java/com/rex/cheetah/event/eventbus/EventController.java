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

public interface EventController {
    void register(Object object);

    void unregister(Object object);

    void post(Event event);

    void post(Collection<? extends Event> events);
}