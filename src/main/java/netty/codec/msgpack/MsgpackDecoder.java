package netty.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by F on 2018/4/17.
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf receive,
                          List<Object> arg) throws Exception{
        final byte[] array;
        final int length = receive.readableBytes();
        array = new byte[length];
        receive.getBytes(receive.readerIndex(), array, 0, length);
        MessagePack messagePack = new MessagePack();
        arg.add(messagePack.read(array));
    }
}
