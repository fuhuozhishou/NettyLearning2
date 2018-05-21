package netty.rpcFramework.rpcServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.rpcFramework.decoder.NettyKryoDecoder;
import netty.rpcFramework.encoder.NettyKryoEncoder;
import netty.rpcFramework.handler.RpcServerDispatchHandler;
import netty.rpcFramework.handler.RpcServerRequestHandler;
import netty.rpcFramework.pojo.RpcRequest;
import netty.rpcFramework.rpcClient.RpcInvokeHook;
import netty.rpcFramework.rpcClient.TestInterface;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by F on 2018/4/19.
 */
public class RpcServer {
    private Class<?> interfaceClass;
    private Object serviceProvider;

    private int port;
    private int threads;
    private RpcInvokeHook rpcInvokeHook;

    private RpcServerRequestHandler rpcServerRequestHandler;

    protected RpcServer(Class<?> interfaceClass, Object serviceProvider, int port,
                        int threads, RpcInvokeHook rpcInvokeHook){
        this.interfaceClass = interfaceClass;
        this.serviceProvider = serviceProvider;
        this.port = port;
        this.threads = threads;
        this.rpcInvokeHook = rpcInvokeHook;

        rpcServerRequestHandler = new RpcServerRequestHandler(interfaceClass, serviceProvider, threads,
                rpcInvokeHook);
        rpcServerRequestHandler.start();

    }
    public void start()
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(new NettyKryoDecoder(),
                                    new RpcServerDispatchHandler(rpcServerRequestHandler),
                                    new NettyKryoEncoder());
                        }
                    });
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(port);
            channelFuture.sync();
            Channel channel = channelFuture.channel();
            System.out.println("RpcServer started.");
            System.out.println(interfaceClass.getSimpleName() + " in service.");
            channel.closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        /*System.out.println("RpcServer started.");
        System.out.println(interfaceClass.getSimpleName() + " in service.");*/
    }

    public void stop()
    {
        //TODO add stop codes here
        System.out.println("server stop success!");
    }

    public static void main(String[] args){
        TestInterface testInterface = new TestInterface() {
            public String testMethod01()
            {
                return "return from server";
            }
        };

        RpcInvokeHook hook = new RpcInvokeHook() {
            public void beforeInvoke(String methodName, Object[] args)
            {
                System.out.println("beforeInvoke in server" + methodName);
            }

            public void afterInvoke(String methodName, Object[] args)
            {
                System.out.println("afterInvoke in server" + methodName);
            }
        };

        RpcServer rpcServer = RpcServerBuilder.create()
                .serviceInterface(TestInterface.class)
                .serviceProvider(testInterface)
                .threads(4)
                .hook(hook)
                .bind(3721)
                .build();
        rpcServer.start();
    }

}
