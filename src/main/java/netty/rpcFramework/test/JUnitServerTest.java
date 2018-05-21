package netty.rpcFramework.test;

import netty.rpcFramework.rpcServer.RpcServer;
import netty.rpcFramework.rpcServer.RpcServerBuilder;
import org.junit.*;

/**
 * Created by F on 2018/5/8.
 */
public class JUnitServerTest {
    @Test
    public void testServerStart(){
        JUnitTestInterfaceImpl jUnitTestInterfaceImpl = new JUnitTestInterfaceImpl();
        RpcServer rpcServer = RpcServerBuilder.create()
                .serviceInterface(JUnitTestInterface.class)
                .serviceProvider(jUnitTestInterfaceImpl)
                .threads(4)
                .bind(3721)
                .build();
        rpcServer.start();
    }
    public static void main(String[] args){
        new JUnitServerTest().testServerStart();
    }
}
