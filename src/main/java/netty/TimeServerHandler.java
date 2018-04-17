package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * Created by F on 2018/4/16.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private int counter;
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    throws Exception{

        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);*/
        String body = (String) msg;
        System.out.println("The time server receive order : " + body
        + " ; the counter is : " + ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ?
                new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        //先将发送的信息写入缓存数组中，再通过ctx.flush()写入SocketChannel中
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable){
        ctx.close();
    }
}
