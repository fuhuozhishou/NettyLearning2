package netty.rpcFramework.rpcClient;

/**
 * Created by F on 2018/4/19.
 */
public interface RpcInvokeHook {
    void beforeInvoke(String methodName, Object[] args);
    void afterInvoke(String methodName, Object[] args);
}
