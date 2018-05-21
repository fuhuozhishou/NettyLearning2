package netty.rpcFramework.rpcResponse;

import com.esotericsoftware.kryo.Kryo;
import de.javakaffee.kryoserializers.KryoReflectionFactorySupport;

/**
 * Created by F on 2018/5/1.
 */
public class KryoHolder {
    private static ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>(){
        protected Kryo initialValue(){
            //最终用于序列化和反序列化的kryo对象是通过KryoReflectionFactorySupport()创建的
            return new KryoReflectionFactory();

        }
    };

    public static Kryo get(){
        return kryoThreadLocal.get();
    }
}
