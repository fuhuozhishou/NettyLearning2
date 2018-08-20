package concurrent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 基于信号量的资源阻塞获取
 * Created by F on 2018/5/21.
 */
public class BoundedHashSet<T> {
    private final Set<T> set;
    private final Semaphore semaphore;

    public BoundedHashSet(int bound){
        this.set = Collections.synchronizedSet(new HashSet<T>());
        semaphore = new Semaphore(bound);

    }

    /**
     * acquire方法会一直阻塞到资源池不为空
     * @param o
     * @return
     * @throws InterruptedException
     */

    public boolean add(T o) throws InterruptedException {
        semaphore.acquire();
        boolean wasAdded = false;
        try{
            wasAdded = set.add(o);
            return wasAdded;
        }finally {
            if(!wasAdded)
                semaphore.release();
        }
    }
    public boolean remove(Object o){
        boolean wasRemoved = set.remove(o);
        if(wasRemoved)
            semaphore.release();
        return wasRemoved;
    }
}
