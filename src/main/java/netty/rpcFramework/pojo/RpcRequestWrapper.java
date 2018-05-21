package netty.rpcFramework.pojo;


import io.netty.channel.Channel;

/**
 * Created by F on 2018/5/6.
 */
public class RpcRequestWrapper  {
    //private RpcRequest rpcRequest;
    int id;
    String methodName;
    Object[] args;
    private Channel channel;

    public RpcRequestWrapper(RpcRequest rpcRequest, Channel channel){
        this.channel = channel;
        this.id = rpcRequest.getId();
        this.args = rpcRequest.getArgs();
        this.methodName = rpcRequest.getMethodName();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }



}
