package netty.rpcFramework.rpcClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by F on 2018/4/19.
 */
public class RpcFuture {
    public final static int STATE_AWAIT = 0;
    public final static int STATE_SUCCESS = 1;
    public final static int STATE_EXCEPTION = 2;

    private CountDownLatch countDownLatch;
    private Object result;
    private Throwable throwable;
    private int state;
    private RpcFutureListener rpcFutureListener = null;

    public RpcFuture(){
        countDownLatch = new CountDownLatch(1);
        state = STATE_AWAIT;
    }


    public Object get() throws Throwable{
        try{
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if(state == STATE_SUCCESS)
            return result;
        else if(state == STATE_EXCEPTION){
            throw throwable;
        }else{
            throw new RuntimeException("RpcFuture Exception!");
        }

    }

    public Object get(long timeout) throws Throwable{
        boolean awaitSuccess = true;
        try{
            awaitSuccess = countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        /*if(!awaitSuccess)
            throw new RpcTimeOutException("");*/
        if(state == STATE_SUCCESS)
            return result;
        else if(state == STATE_EXCEPTION){
            throw throwable;
        }else{
            throw new RuntimeException("RpcFuture Exception!");
        }
    }

    public void setResult(Object result){
        this.result = result;
        state = STATE_SUCCESS;

        if(rpcFutureListener != null){
            rpcFutureListener.onResult(result);
        }
        countDownLatch.countDown();
    }

    public void setThrowable(Throwable throwable){
        this.throwable = throwable;
        state = STATE_EXCEPTION;

        if(rpcFutureListener != null){
            rpcFutureListener.onException(throwable);
        }
        countDownLatch.countDown();
    }

    public boolean isDone(){
        return state != STATE_AWAIT;
    }

    public void setRpcFutureListener(RpcFutureListener rpcFutureListener){
        this.rpcFutureListener = rpcFutureListener;
    }

}
