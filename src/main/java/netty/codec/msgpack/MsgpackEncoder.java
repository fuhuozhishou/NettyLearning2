package netty.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;


/**
 * Created by F on 2018/4/17.
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object>{
    @Override
    protected void encode(ChannelHandlerContext ctx, Object arg1, ByteBuf arg2)
        throws Exception{
        MessagePack messagePack = new MessagePack();
        byte[] raw = messagePack.write(arg1);
        arg2.writeBytes(raw);
    }

}
