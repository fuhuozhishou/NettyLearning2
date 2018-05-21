package netty.rpcFramework.rpcClient;

/**
 * Created by F on 2018/4/19.
 */
public interface RpcFutureListener {
    void onResult(Object result);
    void onException(Throwable throwable);
}
