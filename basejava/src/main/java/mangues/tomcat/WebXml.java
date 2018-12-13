package mangues.tomcat;

import javax.servlet.Servlet;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class WebXml {
    //项目文件夹地址
    public String projectPath = null;
    //servlet 类集合
    public Map<String, Object> servlets = new HashMap<>();

    //servlet映射
    public Map<String, Object> servletMapping = new HashMap<>();

    //servlet 实例对象集合
    public Map<String, Servlet> servletInstance = new HashMap<>();

    /**
     * 使用JDK类加载工具，加载class和jar包
     */
    public void loadServlet() throws Exception {
        //定义一个加载class的工具，告诉JVM，类在何方
        URLClassLoader loader = new URLClassLoader(new URL[]{new URL("file:" + projectPath + "\\WEB-INF\\classes")});
        for (Map.Entry<String, Object> entry : servlets.entrySet()) {
            String servletName = entry.getKey().toString();
            String servletClassName = entry.getValue().toString();

            //1.加载到jvm
            Class<?> loadClass = loader.loadClass(servletClassName);
            //2 .利用反射技术处理,创建对象
            Servlet servlet = (Servlet) loadClass.newInstance();
            servletInstance.put(servletName, servlet);
        }
    }
}
