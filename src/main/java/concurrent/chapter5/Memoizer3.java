package concurrent.chapter5;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by F on 2018/5/22.
 */
public class Memoizer3<A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;
    public Memoizer3(Computable<A, V> c){
        this.c = c;
    }

    /**
     * 首先判断cache中是否有某个相应的计算开始执行，若没有则创建一个FutureTask来执行计算，
     * 并注册到Map中。由于if语句并不存在原子性(这是因为这里采用的底层Map的put方法，无法加锁来
     * 确保原子性。
     * @param arg
     * @return
     * @throws InterruptedException
     */
    public V compute(final A arg) throws InterruptedException{
        Future<V> f = cache.get(arg);
        if( f == null){
            Callable<V> eval = new Callable<V>() {
                @Override
                public V call() throws InterruptedException {
                    return c.compute(arg);
                }
            };
            FutureTask<V> futureTask = new FutureTask<>(eval);
            f = futureTask;
            cache.put(arg, futureTask);
            futureTask.run();//在这里执行c.compute
        }
        try {
            return f.get();
        }catch (ExecutionException e){
            throw new InterruptedException(e.getMessage());
        }
    }
}
