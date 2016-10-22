package com.rex.cheetah.protocol.netty;

/**
 * <p>Title: Rex Cheetah</p>
 * <p>Description: Rex Cheetah For Distribution</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Rex</p>
 * @author Wang Bo
 * @email uml0126@126.com
 * @version 1.0
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;

import java.io.Serializable;

import com.rex.cheetah.serialization.SerializerExecutor;

public class NettyObjectEncoder extends MessageToByteEncoder<Object> {
    public NettyObjectEncoder() {
        
    }
    
    @Override
    protected void encode(ChannelHandlerContext context, Object object, ByteBuf buf) throws Exception {
        byte[] bytes = null;
        try {
            if (buf == null) {
                return;
            }
            
            if (!(object instanceof Serializable)) {
                return;
            }
            bytes = SerializerExecutor.serialize((Serializable) object);
            
			buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            ReferenceCountUtil.release(bytes);
        }
    }
}