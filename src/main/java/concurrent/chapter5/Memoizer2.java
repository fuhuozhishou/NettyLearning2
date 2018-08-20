package concurrent.chapter5;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by F on 2018/5/22.
 */
public class Memoizer2<A, V> implements Computable<A, V>{
    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memoizer2(Computable c){
        this.c = c;
    }

    /**
     * 采用线程安全的ConcurrentHashMap作为计算结果缓存，因此compute方法并不需要加锁，
     * 但存在重复计算的可能，即一个线程正在执行一个耗时的计算，在返回结果前另一个线程
     * 并不知道这个线程在计算什么，因而可能进行重复的计算
     * @param arg
     * @return
     * @throws InterruptedException
     */
    public V compute(A arg) throws InterruptedException{
        V result = cache.get(arg);
        if(result == null){
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
