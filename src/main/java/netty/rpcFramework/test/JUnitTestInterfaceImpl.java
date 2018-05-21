package netty.rpcFramework.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by F on 2018/5/8.
 */
public class JUnitTestInterfaceImpl implements JUnitTestInterface {
    @Override
    public String methodWithoutArg() {
        return "this is return from methodWithoutArg()";
    }

    @Override
    public String methodWithArgs(String arg1, int arg2) {
        return arg1 + " = " + arg2;
    }

    @Override
    public JUnitTestCustomObject methodWithCustomObject(
            JUnitTestCustomObject customObject) {
        JUnitTestCustomObject object = new JUnitTestCustomObject(customObject.getString() +
                "after", customObject.getI() + 47);
        return object;
    }

    @Override
    public List<String> methodReturnList(String arg1, String arg2) {
        return Arrays.asList(arg1, arg2);
    }

    @Override
    public void methodThrowException() {

        throw new JUnitTestCustomException();
    }

    @Override
    public void methodTimeOut() {

        try{
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void methodReturnVoid() {

        return;
    }

    @Override
    public String methodDelayOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "I have sleep 1000ms already.";
    }

    @Override
    public int methodForMultiThread(int threadId) {
        return threadId;
    }

    @Override
    public String methodForPerformance() {
        return "cao";
    }
}
