package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

/**
 * Created by F on 2018/4/16.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
    //private final ByteBuf firstMessage;
    private int counter;
    private byte[] req;

    public TimeClientHandler(){
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator"))
                .getBytes();
        /*firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);*/

    }

    public void channelActive(ChannelHandlerContext ctx){
        ByteBuf message = null;
        for(int i = 0; i < 100; i++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }

        //ctx.writeAndFlush(firstMessage);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg)throws  Exception{
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");*/
        String body = (String) msg;
        System.out.println("Now is :" + body + " ; the counter is :" + ++counter);

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        logger.warning("Unexpected exception from downstream : "
                + cause.getMessage());
        ctx.close();
    }

}
