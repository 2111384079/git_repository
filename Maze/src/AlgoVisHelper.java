//颜色常量定义，图像填充（颜色），线程暂停
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AlgoVisHelper {

    private AlgoVisHelper() {}

    //颜色常量定义
    public static final Color Red = Color.web("#F44336");
    public static final Color Pink = Color.web("#E91E63");
    public static final Color Purple = Color.web("#9C27B0");
    public static final Color DeepPurple = Color.web("#673AB7");
    public static final Color Indigo = Color.web("#3F51B5");
    public static final Color Blue = Color.web("#2196F3");
    public static final Color LightBlue = Color.web("#03A9F4");
    public static final Color Cyan = Color.web("#00BCD4");
    public static final Color Teal = Color.web("#009688");
    public static final Color Green = Color.web("#4CAF50");
    public static final Color LightGreen = Color.web("#8BC34A");
    public static final Color Lime = Color.web("#CDDC39");
    public static final Color Yellow = Color.web("#FFEB3B");
    public static final Color Amber = Color.web("#FFC107");
    public static final Color Orange = Color.web("#FF9800");
    public static final Color DeepOrange = Color.web("#FF5722");
    public static final Color Brown = Color.web("#795548");
    public static final Color Grey = Color.web("#9E9E9E");
    public static final Color BlueGrey = Color.web("#607D8B");
    public static final Color Black = Color.web("#000000");
    public static final Color White = Color.web("#FFFFFF");

    //颜色填充
    public static void setColor(GraphicsContext gc, Color color) {

        gc.setFill(color);
    }

    //填充图形
    public static void fillRectangle(GraphicsContext gc, int x, int y, int w, int h) {
        gc.fillRect(x, y, w, h);
    }

    //线程暂停
    public static void pause(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }
}
