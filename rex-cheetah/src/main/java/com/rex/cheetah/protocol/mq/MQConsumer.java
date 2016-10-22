package com.rex.cheetah.protocol.mq;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import javax.jms.BytesMessage;

import org.springframework.jms.listener.SessionAwareMessageListener;

import com.rex.cheetah.common.delegate.CheetahDelegateImpl;

public abstract class MQConsumer extends CheetahDelegateImpl implements SessionAwareMessageListener<BytesMessage> {

}