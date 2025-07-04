package com.lcx.netty.handlerDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author : lichangxin
 * @create : 2024/7/16 14:27
 * @description
 */
public class CustomerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//        Integer data = null;
//        if (byteBuf.readableBytes() >= 4) {
//            data = byteBuf.readInt();
//        }
        list.add(byteBuf);
    }
}
