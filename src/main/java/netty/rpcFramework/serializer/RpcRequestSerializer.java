package netty.rpcFramework.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import netty.rpcFramework.pojo.RpcRequest;

/**
 * Created by F on 2018/5/6.
 */
public class RpcRequestSerializer  extends Serializer<RpcRequest> {

    @Override
    public void write(Kryo kryo, Output output, RpcRequest rpcRequest) {
        output.writeInt(rpcRequest.getId());
        output.writeByte(rpcRequest.getMethodName().length());
        output.write(rpcRequest.getMethodName().getBytes());
        kryo.writeClassAndObject(output, rpcRequest.getArgs());
    }

    @Override
    public RpcRequest read(Kryo kryo, Input input, Class<RpcRequest> type) {
        RpcRequest rpcRequest = null;
        int id = input.readInt();
        byte methodLength = input.readByte();
        byte[] methodBytes = input.readBytes(methodLength);
        String methodName = new String(methodBytes);
        Object[] args = (Object[])kryo .readClassAndObject(input);

        rpcRequest = new RpcRequest(id, methodName, args);
        return rpcRequest;
    }
}
