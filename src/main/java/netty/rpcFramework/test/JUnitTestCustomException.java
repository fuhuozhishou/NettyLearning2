package netty.rpcFramework.test;

/**
 * Created by F on 2018/5/8.
 */
public class JUnitTestCustomException extends RuntimeException {
    private static final long serialVersionUID = 591530421634999576L;

    public JUnitTestCustomException(){
        super("CustomException");
    }
}
