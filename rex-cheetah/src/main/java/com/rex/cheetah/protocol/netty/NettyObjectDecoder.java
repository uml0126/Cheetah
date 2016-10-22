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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

import com.rex.cheetah.serialization.SerializerExecutor;

public class NettyObjectDecoder extends LengthFieldBasedFrameDecoder {
    // 半包解码器。超出maxMessageSize上限，会抛出异常
    public NettyObjectDecoder(int maxMessageSize) {
        super(maxMessageSize, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext context, ByteBuf in) throws Exception {
        Object object = null;
        ByteBuf buf = null;
        try {
            Object decode = super.decode(context, in);
            if (decode == null) {
                return null;
            }
            
            buf = (ByteBuf) decode;

            int startIndex = buf.readerIndex();
            int endIndex = startIndex + buf.readableBytes();
            buf.markReaderIndex();

            byte[] bytes = new byte[endIndex - startIndex];
            buf.readBytes(bytes);
                              
            object = SerializerExecutor.deserialize(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            ReferenceCountUtil.release(buf);
        }
        
        return object;
    }
}