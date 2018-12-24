package com.example.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * <p>客户端业务处理类</p>
 *
 * @author xiaodongsun
 * @date 2018/12/24
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 通道就绪事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client: " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Client send msg", CharsetUtil.UTF_8));
    }

    /**
     * 读取服务端访问数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       ByteBuf buf = (ByteBuf) msg;
       System.out.println("服务器端发来的消息： " + buf.toString(CharsetUtil.UTF_8));
    }
}
