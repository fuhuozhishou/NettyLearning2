package concurrent.chapter5;

import java.math.BigInteger;

/**
 * Created by F on 2018/5/22.
 */
public class ExpensiveFunction implements Computable<String, BigInteger>{
    public BigInteger compute(String arg){
        try{
            Thread.sleep(10000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return new BigInteger(arg);
    }

}
