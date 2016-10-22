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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.net.SocketAddress;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rex.cheetah.common.container.ExecutorContainer;
import com.rex.cheetah.common.entity.ProtocolType;
import com.rex.cheetah.common.thread.ThreadPoolFactory;
import com.rex.cheetah.event.protocol.ProtocolEventFactory;
import com.rex.cheetah.protocol.ProtocolMessage;
import com.rex.cheetah.protocol.ProtocolRequest;
import com.rex.cheetah.protocol.ProtocolResponse;
import com.rex.cheetah.protocol.ServerExecutorAdapter;

public class NettyServerHandler extends SimpleChannelInboundHandler<ProtocolRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(NettyServerHandler.class);

    private ExecutorContainer executorContainer;
    private boolean transportLogPrint;

    public NettyServerHandler(ExecutorContainer executorContainer, boolean transportLogPrint) {
        this.executorContainer = executorContainer;
        this.transportLogPrint = transportLogPrint;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext context, final ProtocolRequest request) throws Exception {
        final String interfaze = request.getInterface();
        ThreadPoolFactory.createThreadPoolServerExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (transportLogPrint) {
                    boolean heartbeat = request.isHeartbeat();
                    if (!heartbeat) {
                        LOG.info("Request from client={}, service={}", getRemoteAddress(context), interfaze);
                    }
                }
                
                ProtocolResponse response = new ProtocolResponse();
                try {
                    ServerExecutorAdapter serverExecutorAdapter = executorContainer.getServerExecutorAdapter();
                    serverExecutorAdapter.handle(request, response);
                } catch (Exception e) {
                    LOG.error("Server handle failed", e);
                } finally {
                    try {
                        boolean feedback = request.isFeedback();
                        if (feedback) {
                            context.writeAndFlush(response);
                        }
                    } catch(Exception e) {
                        LOG.error("Channel write and flush failed", e);
                    } finally {
                        ReferenceCountUtil.release(request);
                    }
                }

                return null;
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        // LOG.error("Unexpected exception from downstream, cause={}", cause.getMessage(), cause);
        LOG.warn("Unexpected exception from downstream, remote address={}, cause={}", getRemoteAddress(context), cause.getClass().getName(), cause);
        
        ProtocolMessage message = new ProtocolMessage();
        message.setFromUrl(getRemoteAddress(context).toString());
        message.setToUrl(getLocalAddress(context).toString());
        message.setException((Exception) cause);
        ProtocolEventFactory.postServerSystemEvent(ProtocolType.NETTY, message);
    }
    
    public SocketAddress getLocalAddress(ChannelHandlerContext context) {
        return context.channel().localAddress();
    }
    
    public SocketAddress getRemoteAddress(ChannelHandlerContext context) {
        return context.channel().remoteAddress();
    }
}