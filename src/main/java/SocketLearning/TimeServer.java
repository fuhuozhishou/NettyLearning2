package SocketLearning;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by F on 2018/4/13.
 */
public class TimeServer {

    public static void main(String[] args)throws IOException{
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException e){

            }
        }
        ServerSocket serverSocket = null;
        try{
            //ServerSocket监听端口
            serverSocket = new ServerSocket(port);
            System.out.println("The Time Server is start in port: " + port);
            Socket socket = null;
            //伪异步I/O，建立线程池
            /*TimeServerHandlerExecutePool singleExecutor = new
                    TimeServerHandlerExecutePool(50, 1000);*/
            while(true){
                //在3次握手建立连接后，系统才会将这个连接交给应用层ServerSocket
                //若ServerSocket还没接受这个socket，那么这个连接将被缓存，同时也将缓存发送来的数据，等待处理
                socket = serverSocket.accept();
                //singleExecutor.execute(new TimeServerHandler(socket));
                new Thread(new TimeServerHandler(socket)).start();
            }
        }finally{
            if(serverSocket != null){
                System.out.println("The time server close");
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
