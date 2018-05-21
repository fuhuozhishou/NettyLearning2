package netty.rpcFramework.decoder;

import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import netty.rpcFramework.rpcResponse.KryoSerializer;

/**
 * Created by F on 2018/5/6.
 */
public class NettyKryoDecoder extends LengthFieldBasedFrameDecoder{

    public NettyKryoDecoder(){
        super(1048567, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in)throws Exception{
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if(frame == null)
            return null;
        return KryoSerializer.deserialize(frame);
    }

    @Override
    protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length){
        return buffer.slice(index, length);
    }

}
