package netty.rpcFramework.rpcClient;

import java.lang.reflect.Proxy;

/**
 * Created by F on 2018/4/19.
 */
public class RpcClientProxyBuilder {
    public static class ProxyBuilder<T>{
        private Class<T> clazz;
        private RpcClient rpcClient;

        private long timeoutMills = 0;
        private RpcInvokeHook rpcInvokeHook = null;
        private String host;
        private int port;
        private int threads;

        private ProxyBuilder(Class<T> clazz){
            this.clazz = clazz;
        }

        public ProxyBuilder<T> timeout(long timeoutMills){
            this.timeoutMills = timeoutMills;
            if(timeoutMills < 0)
                throw new IllegalArgumentException("timeoutMills can not be minus!");
            return this;
        }
        public ProxyBuilder<T> hook(RpcInvokeHook hook){
            this.rpcInvokeHook = hook;
            return this;
        }

        public ProxyBuilder<T> threads(int threads){
            this.threads = threads;
            return this;
        }

        /**
         * only set
         * @param host
         * @param port
         * @return
         */
        public ProxyBuilder<T> connect(String host, int port){
            this.host = host;
            this.port = port;
            return this;
        }

        /**
         * build the synchronous proxy. In synchronous way, Thread will be blocked until
         * get the result or timeout.
         * @return
         */
        @SuppressWarnings("unchecked")
        public T build(){
            rpcClient = new RpcClient(timeoutMills, rpcInvokeHook, host, port, threads);
            rpcClient.connect();

            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, rpcClient );
        }

        public RpcClientAsyncProxy buildAsyncProxy(){
            rpcClient = new RpcClient(timeoutMills, rpcInvokeHook, host, port, threads);
            rpcClient.connect();
            return new RpcClientAsyncProxy(rpcClient);
        }
    }
    public static <T> ProxyBuilder<T> create(Class<T> targetClass){
        return new ProxyBuilder<>(targetClass);
    }
}
