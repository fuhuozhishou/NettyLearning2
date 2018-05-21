package netty.rpcFramework.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.rpcFramework.rpcClient.RpcClient;
import netty.rpcFramework.rpcResponse.RpcResponse;

/**
 * Created by F on 2018/5/6.
 */
public class RpcClientDispatchHandler extends ChannelInboundHandlerAdapter {
    private RpcClientResponseHandler rpcClientResponseHandler;
    private RpcClientChannelInactiveListener rpcClientChannelInactiveListener = null;
    public RpcClientDispatchHandler(
            RpcClientResponseHandler rpcClientResponseHandler,
            RpcClientChannelInactiveListener rpcClientChannelInactiveListener)
    {
        this.rpcClientResponseHandler = rpcClientResponseHandler;
        this.rpcClientChannelInactiveListener = rpcClientChannelInactiveListener;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        RpcResponse rpcResponse = (RpcResponse)msg;
        rpcClientResponseHandler.addResponse(rpcResponse);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception
    {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        if(rpcClientChannelInactiveListener != null)
            rpcClientChannelInactiveListener.onInactive();
    }
}
