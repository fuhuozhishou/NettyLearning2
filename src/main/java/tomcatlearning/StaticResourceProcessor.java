package tomcatlearning;

import java.io.IOException;

/**
 * @author zhangym
 * @version 1.0  2018/7/8
 */
public class StaticResourceProcessor {
    public void process(Request request, Response response){
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
