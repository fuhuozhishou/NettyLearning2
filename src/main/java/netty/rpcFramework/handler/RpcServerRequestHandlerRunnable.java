package netty.rpcFramework.handler;

import com.esotericsoftware.reflectasm.MethodAccess;
import io.netty.channel.Channel;
import netty.rpcFramework.pojo.RpcRequestWrapper;
import netty.rpcFramework.rpcClient.RpcInvokeHook;
import netty.rpcFramework.rpcResponse.RpcResponse;

import java.util.concurrent.BlockingQueue;

/**
 * Rpc服务端处理请求线程
 * Created by F on 2018/5/6.
 */
public class RpcServerRequestHandlerRunnable implements Runnable {
    private Class<?> interfaceClass;
    private Object serviceProvider;
    private RpcInvokeHook rpcInvokeHook;
    private BlockingQueue<RpcRequestWrapper> requestQueue;
    private RpcRequestWrapper rpcRequestWrapper;

    //使用MethodAccess，虽然获取此对象较慢，但调用invoke()方法较原生jdk快
    private MethodAccess methodAccess;
    private String lastMethodName = "";
    private int lastMethodIndex;

    public RpcServerRequestHandlerRunnable(Class<?> interfaceClass,
                                          Object serviceProvider, RpcInvokeHook rpcInvokeHook,
                                          BlockingQueue<RpcRequestWrapper> requestQueue)
    {
        this.interfaceClass = interfaceClass;
        this.serviceProvider = serviceProvider;
        this.rpcInvokeHook = rpcInvokeHook;
        this.requestQueue = requestQueue;

        methodAccess = MethodAccess.get(interfaceClass);
    }

    /**
     *
     */
    public void run(){
        while(true){
            try{
                rpcRequestWrapper = requestQueue.take();

                String methodName = rpcRequestWrapper.getMethodName();
                Object[] args = rpcRequestWrapper.getArgs();

                if(rpcInvokeHook != null)
                    rpcInvokeHook.beforeInvoke(methodName, args);

                Object result = null;
                if(!methodName.equals(lastMethodName)){
                    lastMethodIndex = methodAccess.getIndex(methodName);
                    lastMethodName = methodName;
                }

                result = methodAccess.invoke(serviceProvider, lastMethodIndex, args);

                Channel channel = rpcRequestWrapper.getChannel();
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setId(rpcRequestWrapper.getId());
                rpcResponse.setResult(result);
                rpcResponse.setInvokeSuccess(true);
                channel.writeAndFlush(rpcResponse);

                if(rpcInvokeHook != null)
                    rpcInvokeHook.afterInvoke(methodName, args);
            }catch (Exception e){
                Channel channel = rpcRequestWrapper.getChannel();
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setId(rpcRequestWrapper.getId());
                rpcResponse.setThrowable(e);
                rpcResponse.setInvokeSuccess(false);
                channel.writeAndFlush(rpcResponse);
            }
        }
    }
}
