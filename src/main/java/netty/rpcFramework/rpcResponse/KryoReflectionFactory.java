package netty.rpcFramework.rpcResponse;

import com.esotericsoftware.kryo.Serializer;
import de.javakaffee.kryoserializers.*;
import netty.rpcFramework.pojo.RpcRequest;
import netty.rpcFramework.serializer.RpcRequestSerializer;
import netty.rpcFramework.serializer.RpcResponseSerializer;

import java.lang.reflect.InvocationHandler;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by F on 2018/5/1.
 */
public class KryoReflectionFactory extends KryoReflectionFactorySupport{
    public KryoReflectionFactory(){
        setRegistrationRequired(false);
        setReferences(true);
        register(RpcRequest.class, new RpcRequestSerializer());
        register(RpcResponse.class, new RpcResponseSerializer());
        register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
        register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
        register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
        register(Collections.singletonList("").getClass(), new CollectionsSingletonListSerializer());
        register(Collections.singleton("").getClass(), new CollectionsSingletonSetSerializer());
        register(Collections.singletonMap("", "").getClass(), new CollectionsSingletonMapSerializer());
        register(Pattern.class, new RegexSerializer());
        register(BitSet.class, new BitSetSerializer());
        register(URI.class, new URISerializer());
        register(UUID.class, new UUIDSerializer());
        register(GregorianCalendar.class, new GregorianCalendarSerializer());
        register(InvocationHandler.class, new JdkProxySerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(this);
        SynchronizedCollectionsSerializer.registerSerializers(this);
    }
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Serializer<?> getDefaultSerializer(Class clazz){
        if(EnumSet.class.isAssignableFrom(clazz))
            return new EnumSetSerializer();
        if(EnumMap.class.isAssignableFrom(clazz))
            return new EnumMapSerializer();
        if(Collections.class.isAssignableFrom(clazz))
            return new CopyForIterateCollectionSerializer();
        if(Map.class.isAssignableFrom(clazz))
            return new CopyForIterateMapSerializer();
        if(Date.class.isAssignableFrom(clazz))
            return new DateSerializer(clazz);
        if(SubListSerializers.ArrayListSubListSerializer.canSerialize(clazz)
                || SubListSerializers.JavaUtilSubListSerializer.canSerialize(clazz))
            return  SubListSerializers.createFor(clazz);
        return super.getDefaultSerializer(clazz);

    }
}
