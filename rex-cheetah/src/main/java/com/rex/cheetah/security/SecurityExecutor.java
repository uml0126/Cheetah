package com.rex.cheetah.security;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rex.cheetah.common.delegate.CheetahDelegate;
import com.rex.cheetah.protocol.ProtocolRequest;
import com.rex.cheetah.protocol.ProtocolResponse;

public interface SecurityExecutor extends CheetahDelegate {
    // 鉴权
    boolean execute(ProtocolRequest request, ProtocolResponse response);

    // 鉴权    
    boolean execute(HttpServletRequest request, HttpServletResponse response) throws IOException;
}