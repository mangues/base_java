package mangues.tomcat;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * 加载项目
 *
 * @author mangues
 */
public class ProjectUtil {
    private static String webapps = "/Users/mangues/Desktop/dep/java_base/webapps";

    public static Map<String, WebXml> load() throws Exception {
        Map<String, WebXml> result = new HashMap<>();
        //0.war部署前解压
        //1、将工作目录下面的所有的子文件夹，当成一个项目
        File[] projects = new File(webapps).listFiles((FileFilter) file -> file.isDirectory());
        for (File projectFile : projects) {
            //2. 读取每个项目下面的xml文件
            WebXml webXml = new WebXmlUtil().loadXml(projectFile.getPath() + "\\WEB-INF\\web.xml");
            webXml.projectPath = projectFile.getPath();
            //3、已经知道有哪些servlet,需要加载到我们的jvm中来，然后给他创建对象
            webXml.loadServlet();
            //4、保存每一个对应项目的配置信息
            result.put(projectFile.getName(), webXml);
        }
        return result;
    }
}
