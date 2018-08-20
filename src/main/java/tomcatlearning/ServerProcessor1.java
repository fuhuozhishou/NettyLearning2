package tomcatlearning;


import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * @author zhangym
 * @version 1.0  2018/7/8
 */
public class ServerProcessor1 {
    public void process(Response response, Request request){
        String uri = request.getUri();
        String servletName = uri.substring(uri.indexOf("/") + 1);
        URLClassLoader loader = null;
        //构造类加载器
        try {
            URL[] urls = new URL[1];
            //指定URLStreamHandler的null为了区分调用不同的方法
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            //由org.apache.catalina.startup.ClassLoaderFactory的createClassLoader方法产生
            String repository = (new URL("file", null, classPath.getCanonicalPath())).toString();
            //urls[0] = new URL(null, repository, streamHandler);

            urls[0] = new URL("file:D:\\NettyLearning\\");
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        Class myClass = null;
        try {
            myClass = loader.loadClass("tomcatlearning.servlet.PrimitiveServlet");
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        Servlet servlet = null;
        try {
            servlet = (Servlet) myClass.newInstance();
            servlet.service( (ServletRequest) request,  (ServletResponse)response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
    }
}
