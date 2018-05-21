package netty.rpcFramework.handler;

import netty.rpcFramework.rpcClient.RpcFuture;
import netty.rpcFramework.rpcResponse.RpcResponse;

import java.util.concurrent.*;

/**
 * Created by F on 2018/5/6.
 */
public class RpcClientResponseHandler {
    private ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap =
            new ConcurrentHashMap<>();

    private ExecutorService threadPool;
    private BlockingQueue<RpcResponse> responseQueue = new LinkedBlockingQueue<>();

    public RpcClientResponseHandler(int threads){
        threadPool = Executors.newFixedThreadPool(threads);
        for(int i = 0; i < threads; i++){
            threadPool.execute(new RpcClientResponseHandlerRunnable(invokeIdRpcFutureMap,
                    responseQueue));
        }
    }

    public void register(int id, RpcFuture rpcFuture){
        invokeIdRpcFutureMap.put(id, rpcFuture);
    }

    public void addResponse(RpcResponse rpcResponse){
        responseQueue.add(rpcResponse);
    }
}
