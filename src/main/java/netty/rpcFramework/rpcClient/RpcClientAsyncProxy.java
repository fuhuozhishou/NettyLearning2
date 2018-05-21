package netty.rpcFramework.rpcClient;

/**
 * Created by F on 2018/4/19.
 */
public class RpcClientAsyncProxy {
    private RpcClient rpcClient;
    public RpcClientAsyncProxy(RpcClient rpcClient){
        this.rpcClient = rpcClient;
    }

    public RpcFuture call(String methodName, Object... args){
        return (RpcFuture)rpcClient.call(methodName, args);
    }
}
