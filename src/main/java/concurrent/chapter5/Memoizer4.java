package concurrent.chapter5;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by F on 2018/5/22.
 */
public class Memoizer4<A, V> implements Computable<A, V> {
    private final ConcurrentHashMap<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;
    public Memoizer4(Computable<A, V> c){
        this.c = c;
    }

    /**
     * 仍存在缓存污染问题，即由于计算可能被取消或失败，导致缓存中存放为future而非结果。
     * 以至于后续无法更新的新的计算结果。
     * @param arg
     * @return
     * @throws InterruptedException
     */
    @Override
    public V compute(final A arg) throws InterruptedException{
        while(true){
            Future<V> f = cache.get(arg);
            if( f == null){
                Callable<V> eval = new Callable<V>() {
                    @Override
                    public V call() throws InterruptedException {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> futureTask = new FutureTask<>(eval);
                f = cache.putIfAbsent(arg, futureTask);
                if(f == null){
                    f = futureTask;
                    futureTask.run();
                }
            }
            try {
                return f.get();
            }catch (CancellationException e){
                cache.remove(arg, f);
            } catch (ExecutionException e){
                throw new InterruptedException(e.getMessage());
            }

        }
    }
}
