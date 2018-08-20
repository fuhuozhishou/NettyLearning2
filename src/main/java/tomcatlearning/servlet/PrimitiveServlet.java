package tomcatlearning.servlet;

import tomcatlearning.Response;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zhangym
 * @version 1.0  2018/7/8
 */
public class PrimitiveServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

        System.out.println("from service");
        PrintWriter printWriter = servletResponse.getWriter();
        printWriter.println("Hello. Roses are red.");
        printWriter.print("Violets are blue.");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

        System.out.println("destroy");
    }
}
