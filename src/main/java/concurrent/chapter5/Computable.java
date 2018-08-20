package concurrent.chapter5;

/**
 * Created by F on 2018/5/22.
 */
public interface Computable<A,V> {
    V compute(A arg) throws InterruptedException;
}
