package netty.rpcFramework.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.rpcFramework.pojo.RpcRequest;
import netty.rpcFramework.pojo.RpcRequestWrapper;

/**
 * 从channel中读取客户端发来的RpcRequest请求
 * Created by F on 2018/5/6.
 */
public class RpcServerDispatchHandler extends ChannelInboundHandlerAdapter{
    private RpcServerRequestHandler rpcServerRequestHandler;
    public RpcServerDispatchHandler(
            RpcServerRequestHandler rpcServerRequestHandler) {
        this.rpcServerRequestHandler = rpcServerRequestHandler;
    }

    /**
     * 从channel中读取客户端发来的RpcRequest请求，并封装成RpcRequestWrapper交由
     * Handler处理
     * @param ctx 通道处理器上下文
     * @param msg 消息请求
     * @throws Exception
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        RpcRequest rpcRequest = (RpcRequest)msg;
        System.out.println("收到请求id: " + rpcRequest.getId());
        RpcRequestWrapper rpcRequestWrapper = new RpcRequestWrapper(rpcRequest,
                ctx.channel());
        rpcServerRequestHandler.addRequest(rpcRequestWrapper);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception{
    }
}
