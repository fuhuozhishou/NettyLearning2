package netty.rpcFramework.rpcResponse;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

/**
 * Created by F on 2018/5/1.
 */
public class KryoSerializer {
    //预留头部4字节用于写入长度信息
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    public static void serialize(Object object, ByteBuf byteBuf){
        Kryo kryo = KryoHolder.get();
        int startInx = byteBuf.writerIndex();
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(byteBuf);
        try{
            byteBufOutputStream.write(LENGTH_PLACEHOLDER);
            Output output = new Output(1024*4, -1);
            output.setOutputStream(byteBufOutputStream);
            kryo.writeClassAndObject(output, object);

            output.flush();
            output.close();

            int endIndex = byteBuf.writerIndex();

            byteBuf.setInt(startInx, endIndex - startInx - 4);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static Object deserialize(ByteBuf byteBuf){
        if(byteBuf == null)
            return null;
        Input input = new Input(new ByteBufInputStream(byteBuf));
        Kryo kryo = KryoHolder.get();
        return kryo.readClassAndObject(input);
    }
}
