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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rex.cheetah.common.entity.MonitorStat;
import com.rex.cheetah.serialization.SerializerExecutor;

public abstract class AbstractMonitorRetriever implements MonitorRetriever {
    public enum SortBy {
        PROCESS_START_TIME,
        DELIVER_START_TIME;
    }

    @Override
    public MonitorStat create(String monitorStatJson) {
        return SerializerExecutor.fromJson(monitorStatJson, MonitorStat.class);
    }

    @Override
    public void sort(final List<MonitorStat> monitorStatList) {
        Collections.sort(monitorStatList, new Comparator<MonitorStat>() {
            @Override
            public int compare(MonitorStat monitorStat1, MonitorStat monitorStat2) {
                SortBy sortBy = getSortBy(monitorStatList);
                switch (sortBy) {
                    case PROCESS_START_TIME:
                        return sortByProcessStartTime(monitorStat1, monitorStat2);
                    case DELIVER_START_TIME:
                        return sortByDeliverStartTime(monitorStat1, monitorStat2);
                    default:
                        return sortByProcessStartTime(monitorStat1, monitorStat2);
                }
            }
        });
    }

    private SortBy getSortBy(List<MonitorStat> monitorStatList) {
        for (MonitorStat monitorStat : monitorStatList) {
            if (monitorStat.isAsync()) {
                return SortBy.PROCESS_START_TIME;
            }
        }

        return SortBy.DELIVER_START_TIME;
    }

    private int sortByProcessStartTime(MonitorStat monitorStat1, MonitorStat monitorStat2) {
        if (monitorStat1.getProcessStartTime() > monitorStat2.getProcessStartTime()) {
            return 1;
        }

        if (monitorStat1.getProcessStartTime() == monitorStat2.getProcessStartTime()) {
            return 0;
        }

        return -1;
    }

    private int sortByDeliverStartTime(MonitorStat monitorStat1, MonitorStat monitorStat2) {
        if (monitorStat1.getDeliverStartTime() > monitorStat2.getDeliverStartTime()) {
            return 1;
        }

        if (monitorStat1.getDeliverStartTime() == monitorStat2.getDeliverStartTime()) {
            return 0;
        }

        return -1;
    }
}