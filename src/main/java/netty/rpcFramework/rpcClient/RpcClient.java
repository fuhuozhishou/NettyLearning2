package netty.rpcFramework.rpcClient;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.rpcFramework.decoder.NettyKryoDecoder;
import netty.rpcFramework.encoder.NettyKryoEncoder;
import netty.rpcFramework.handler.RpcClientChannelInactiveListener;
import netty.rpcFramework.handler.RpcClientDispatchHandler;
import netty.rpcFramework.handler.RpcClientResponseHandler;
import netty.rpcFramework.pojo.RpcRequest;
import netty.rpcFramework.pojo.RpcRequestWrapper;

import javax.xml.ws.RequestWrapper;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by F on 2018/4/19.
 */
public class RpcClient implements InvocationHandler {
    private long timeoutMills = 0;
    private RpcInvokeHook rpcInvokeHook = null;
    private String host;
    private int port;
    private TestInterface testInterface;
    private RpcClientResponseHandler rpcClientResponseHandler;
    private RpcClientChannelInactiveListener rpcClientChannelInactiveListener;
    private Bootstrap bootstrap;
    private Channel channel;
    private AtomicInteger invokeIdGenerator = new AtomicInteger(0);

    public RpcClient(long timeoutMills, RpcInvokeHook rpcInvokeHook, String host, int port, int threads){
        this.timeoutMills = timeoutMills;
        this.rpcInvokeHook = rpcInvokeHook;
        this.host = host;
        this.port = port;
        this.rpcClientResponseHandler = new RpcClientResponseHandler(threads);
        rpcClientChannelInactiveListener = new RpcClientChannelInactiveListener() {
            @Override
            public void onInactive() {
                System.out.println("connection with server is closed.");
                System.out.println("try to reconnect to the server.");
                channel = null;
                while(channel == null){
                    channel = tryConnect();
                }
            }
        };
    }

    /**
     * 生成一个自增id的RpcFuture，并在RpcClientResponseHandler里注册，以便于在服务
     * 器返回结果的时候，识别指定的RpcFuture，并将id，methodName,args封装成RpcRequest
     * 写入通道发送到服务器
     * @param methodName 调用方法的方法名
     * @param args 调用方法的参数
     * @return
     */
    public RpcFuture call(String methodName, Object... args){
        if(rpcInvokeHook != null){
            rpcInvokeHook.beforeInvoke(methodName, args);
        }

        RpcFuture rpcFuture = new RpcFuture();
        int id = invokeIdGenerator.addAndGet(1);
        rpcClientResponseHandler.register(id, rpcFuture);

        RpcRequest rpcRequest = new RpcRequest(id, methodName, args);
        if(channel != null){
            channel.writeAndFlush(rpcRequest);
        } else
            return null;

        return rpcFuture;
    }

    /**
     * InvocationHandler 的接口方法，动态代理调用方法的实际执行过程
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        RpcFuture rpcFuture = call(method.getName(), args);
        Object result;
        if(timeoutMills == 0){
            result = rpcFuture.get();
        }else{
            result = rpcFuture.get(timeoutMills);
        }

        if(rpcInvokeHook != null){
            rpcInvokeHook.afterInvoke(method.getName(), args);
        }
        return result;
    }

    /**
     * Netty初始化设置并连接
     */
    public void connect(){
        bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        protected void initChannel(Channel ch)throws Exception{
                            ch.pipeline().addLast(new NettyKryoDecoder(),
                                    new RpcClientDispatchHandler(rpcClientResponseHandler,
                                            rpcClientChannelInactiveListener),new NettyKryoEncoder());
                        }
                    });
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);

            while (channel == null){
                channel = tryConnect();
            }
        }catch(Exception e ){
            e.printStackTrace();
        }

    }

    /**
     * 尝试连接，连接失败后，10秒后尝试重连。
     * @return
     */
    private Channel tryConnect(){
        try{
            System.out.println("Try to connect to [" + host + ":" + port + "].");
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            if(channelFuture.isSuccess()){
                System.out.println("Connect to [" + host + ":"+ port + "] succeed." );
                return channelFuture.channel();
            }else{
                System.out.println("Connect to [" + host + ":" + port + "] failed.");
                System.out.println("Try to reconnect in 10 seconds.");
                return null;
            }
        }catch (Exception e){
            System.out.println("Connect to [" + host + ":" + port + "] failed.");
            System.out.println("Try to reconnect in 10 seconds.");
            try{
                Thread.sleep(10000);
            }catch (InterruptedException e1){
                e1.printStackTrace();
            }
            return null;
        }
    }
    public class TestThread extends Thread{
        String methodName;
        Object[] args;;
        RpcFuture rpcFuture;

        public TestThread(RpcFuture rpcFuture, String methodName, Object[] args){
            this.rpcFuture = rpcFuture;
            this.args = args;
            this.methodName = methodName;
        }

        public void run(){
            try{
                Thread.sleep(2000);
                int parameterCount = args.length;
                Method method;
                if(parameterCount > 0){
                    Class<?>[] parameterTypes = new Class[args.length];
                    for(int i = 0; i < parameterCount; i++){
                        parameterTypes[i] = args[i].getClass();
                    }
                    method = testInterface.getClass().getMethod(methodName, parameterTypes);
                }else{
                    method = testInterface.getClass().getMethod(methodName);
                }
                rpcFuture.setResult(method.invoke(testInterface, args));
            }catch (IllegalAccessException e){
                e.printStackTrace();
            } catch (IllegalArgumentException e){
                e.printStackTrace();
            }catch (InvocationTargetException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }catch (NoSuchMethodException e){
                e.printStackTrace();
            }catch (SecurityException e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        RpcInvokeHook hook = new RpcInvokeHook() {
            public void beforeInvoke(String method, Object[] args)
            {
                System.out.println("before invoke in client" + method);
            }

            public void afterInvoke(String method, Object[] args)
            {
                System.out.println("after invoke in client" + method);
            }
        };

        TestInterface testInterface
                = RpcClientProxyBuilder.create(TestInterface.class)
                .timeout(0)
                .threads(4)
                .hook(hook)
                .connect("127.0.0.1", 3721)
                .build();

        for(int i=0; i<10; i++) {
            System.out.println("invoke result = " + testInterface.testMethod01());
        }
    }
}
