package com.rex.cheetah.protocol;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import com.rex.cheetah.common.config.ReferenceConfig;
import com.rex.cheetah.common.util.RandomUtil;

public class ProtocolRequest extends ProtocolMessage {
    private static final long serialVersionUID = 3399899702039468806L;
    
    private ReferenceConfig referenceConfig;
    
    public ProtocolRequest() {
        String messageId = RandomUtil.uuidRandom();
        
        super.setMessageId(messageId);
    }
    
    // Request的MessageId自动产生，不需要设置
    @Override
    public void setMessageId(String messageId) {
        
    }

    public ReferenceConfig getReferenceConfig() {
        return referenceConfig;
    }

    public void setReferenceConfig(ReferenceConfig referenceConfig) {
        this.referenceConfig = referenceConfig;
    }
}