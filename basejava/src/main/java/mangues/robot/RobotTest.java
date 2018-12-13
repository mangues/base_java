package mangues.robot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RobotTest {

    public static void main(String[] args) throws AWTException, InterruptedException, IOException {

        Robot robot = new Robot();
        //设置Robot产生一个动作后的休眠时间,否则执行过快
        robot.setAutoDelay(1000);
        //获取屏幕分辨率
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("height==" + d.getHeight());
        System.out.println("width==" + d.getWidth());

        //以屏幕的尺寸创建个矩形
        Rectangle screenRect = new Rectangle(d);
        //截图（截取整个屏幕图片）
        BufferedImage bufferedImage = robot.createScreenCapture(screenRect);
        //保存截图
        File file = new File("screenRect.png");
        ImageIO.write(bufferedImage, "png", file);


    }

}
