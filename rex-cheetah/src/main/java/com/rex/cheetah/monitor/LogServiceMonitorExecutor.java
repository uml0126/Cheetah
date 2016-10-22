package com.rex.cheetah.monitor;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.serialization.SerializerExecutor;

public class LogServiceMonitorExecutor extends AbstractMonitorExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(LogServiceMonitorExecutor.class);

    @Override
    public void execute(MonitorStat monitorStat) throws Exception {
        executeJson(monitorStat);
    }
    
    protected void executeString(MonitorStat monitorStat) {
        String value = monitorStat.toString();
        String exception = monitorStat.getException();
        if (exception != null) {
            LOG.error(value + "\r\n{}", exception);
        } else {
            LOG.info(value);
        }
    }
    
    protected void executeJson(MonitorStat monitorStat) {
        String value = SerializerExecutor.toJson(monitorStat);
        String exception = monitorStat.getException();
        if (exception != null) {
            LOG.error(value);
        } else {
            LOG.info(value);
        }
    }
}