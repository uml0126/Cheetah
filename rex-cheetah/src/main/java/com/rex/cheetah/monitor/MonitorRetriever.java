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

import java.util.List;

import com.rex.cheetah.common.entity.MonitorStat;

public interface MonitorRetriever {
    
    // 解析Json成MonitorStat对象
    MonitorStat create(String monitorStatJson);

    // 根据时间排序
    void sort(List<MonitorStat> monitorStatList);
}