package netty.rpcFramework.handler;

import netty.rpcFramework.rpcClient.RpcFuture;
import netty.rpcFramework.rpcResponse.RpcResponse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * 请求响应线程
 * Created by F on 2018/5/6.
 */
public class RpcClientResponseHandlerRunnable implements Runnable {
    private ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap;
    private BlockingQueue<RpcResponse> responseQueue;

    public RpcClientResponseHandlerRunnable(ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap,
                                            BlockingQueue<RpcResponse> responseQueue){
        this.invokeIdRpcFutureMap = invokeIdRpcFutureMap;
        this.responseQueue = responseQueue;
    }
    @Override
    public void run() {
        while(true){
            try{
                RpcResponse rpcResponse = responseQueue.take();

                int id = rpcResponse.getId();
                RpcFuture rpcFuture = invokeIdRpcFutureMap.remove(id);

                if(rpcResponse.isInvokeSuccess())
                    rpcFuture.setResult(rpcResponse.getResult());
                else
                    rpcFuture.setThrowable(rpcResponse.getThrowable());
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
