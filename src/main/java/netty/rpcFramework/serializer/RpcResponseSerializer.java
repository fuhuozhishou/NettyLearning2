package netty.rpcFramework.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import netty.rpcFramework.rpcResponse.RpcResponse;

/**
 * Created by F on 2018/5/6.
 */
public class RpcResponseSerializer extends Serializer<RpcResponse> {

    @Override
    public void write(Kryo kryo, Output output, RpcResponse rpcResponse) {
        output.writeInt(rpcResponse.getId());
        output.writeBoolean(rpcResponse.isInvokeSuccess());
        if(rpcResponse.isInvokeSuccess())
            kryo.writeClassAndObject(output, rpcResponse.getResult());
        else
            kryo.writeClassAndObject(output, rpcResponse.getThrowable());
    }

    @Override
    public RpcResponse read(Kryo kryo, Input input, Class<RpcResponse> aClass) {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setId(input.readInt());
        rpcResponse.setInvokeSuccess(input.readBoolean());
        if(rpcResponse.isInvokeSuccess())
            rpcResponse.setResult(kryo.readClassAndObject(input));
        else
            rpcResponse.setThrowable((Throwable)kryo.readClassAndObject(input));

        return rpcResponse;
    }
}
