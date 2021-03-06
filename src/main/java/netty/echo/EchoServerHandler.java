package netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by F on 2018/4/17.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter{
    int counter = 0;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception{
        /*String body = (String) msg;
        System.out.println("This is " + ++counter + " times receive client : [" +
                body + " ]");
        body += "$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);*/
        System.out.println("Server receive the msgpack message : " + msg);
        ctx.writeAndFlush(msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();//发生异常，关闭链路
    }

}
