package netty.echo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.pojo.UserInfo;

/**
 * Created by F on 2018/4/17.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter{

    //private int counter;
    //static final String ECHO_REQ = "Hi,welcome to Netty.$_";
    private final int sendNumber;

    public EchoClientHandler(int sendNumber){
        this.sendNumber = sendNumber;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        /*for(int i = 0; i < 10; i++){
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }*/

        //写入对象必须要再pojo之前加@Message
        UserInfo[] infos = UserInfo();
        for(UserInfo infoE : infos){
            ctx.write(infoE);
        }
        ctx.flush();
    }

    private UserInfo[] UserInfo(){
        UserInfo[] userInfos = new UserInfo[sendNumber];
        UserInfo userInfo = null;
        for (int i = 0; i < sendNumber; i++){
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("AQWEQE" + i);
            userInfos[i] = userInfo;
        }
        return userInfos;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception{
        System.out.println("Client receive the msgpack message : " + msg);
        //ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }

}
