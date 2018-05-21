package netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.internal.chmv8.ForkJoinTask;
import netty.codec.msgpack.MsgpackDecoder;
import netty.codec.msgpack.MsgpackEncoder;

import java.nio.ByteBuffer;

/**
 * Created by F on 2018/4/17.
 */
public class EchoClient {
    private final String host;
    private final int port;
    private final int sendNumber;
    public EchoClient(String host, int port, int sendNumber){
        this.host = host;
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public void run() throws Exception{

        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast("frameDecoder", new
                                    LengthFieldBasedFrameDecoder(65535, 0 , 2,0,2));
                            ch.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
                            ch.pipeline().addLast("frameEncoder", new
                                    LengthFieldPrepender(2));
                            ch.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
                            //ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            //ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            //ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler(sendNumber));
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();

            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        new EchoClient("127.0.0.1", port,1000).run();

    }
}
