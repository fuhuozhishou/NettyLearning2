package netty.rpcFramework.encoder;

import com.esotericsoftware.kryo.KryoSerializable;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.rpcFramework.rpcResponse.KryoSerializer;

/**
 * Created by F on 2018/5/6.
 */
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        KryoSerializer.serialize(msg, out);
    }
}
