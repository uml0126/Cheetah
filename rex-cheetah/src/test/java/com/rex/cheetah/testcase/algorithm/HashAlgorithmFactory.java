package com.rex.cheetah.testcase.algorithm;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import com.rex.cheetah.common.entity.ApplicationEntity;

public class HashAlgorithmFactory {
    public static List<ApplicationEntity> getNodeList(int count, int index) {
        List<ApplicationEntity> nodeList = new ArrayList<ApplicationEntity>();
        for (int i = index; i < index + count; i++) {
            ApplicationEntity node = new ApplicationEntity();
            node.setApplication("application");
            node.setGroup("group");
            node.setHost(i + "." + i + "." + i + "." + i);
            node.setPort(i);
            nodeList.add(node);
        }

        return nodeList;
    }
}