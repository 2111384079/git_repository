//迷宫的绘制，主要用于界面设计
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AlgoFrame extends Application {

    private int canvasWidth;
    private int canvasHeight;
    private Canvas canvas;
    private Button manualButton;
    private Button autoBFButton;
    private Button autoDFButton;
    private Button dfsButton;
    private Button bfsButton;
    private Button clearButton;

    //可色设置退出程序的按钮->private Button ExitButton;

    public AlgoFrame(String title, int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }
    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Maze Visualization");

        // 创建画布
        canvas = new Canvas(canvasWidth, canvasHeight);

        // 创建按钮
        manualButton = new Button("手动输入");
        autoDFButton = new Button(("DFS自动生成"));
        autoBFButton = new Button("BFS自动生成");
        dfsButton = new Button("DFS寻路");
        bfsButton = new Button("BFS最短寻路");
        clearButton = new Button("清除画面");

        // 设置按钮大小
        manualButton.setPrefWidth(150);
        manualButton.setPrefHeight(50);
        autoDFButton.setPrefWidth(150);
        autoDFButton.setPrefHeight(50);
        autoBFButton.setPrefWidth(150);
        autoBFButton.setPrefHeight(50);
        dfsButton.setPrefWidth(150);
        dfsButton.setPrefHeight(50);
        bfsButton.setPrefWidth(150);
        bfsButton.setPrefHeight(50);
        clearButton.setPrefWidth(150);
        clearButton.setPrefHeight(50);

        // 创建按钮容器
        VBox buttonBox = new VBox(20, manualButton,autoDFButton,autoBFButton,dfsButton,bfsButton,clearButton);
        buttonBox.setPrefHeight(canvasHeight); // 设置 VBox 的高度与画布相同
        buttonBox.setAlignment(Pos.CENTER); // 按钮在 VBox 中垂直居中对齐
        buttonBox.setPadding(new Insets(10)); // 设置 VBox 的内边距，以增加按钮与画布之间的间距
        buttonBox.setMinWidth(200); // 设置 VBox 的最小宽度，确保按钮有足够的空间

        // 创建主面板并添加组件
        BorderPane mainPane = new BorderPane();
        mainPane.setPrefHeight(canvasHeight);
        mainPane.setPrefWidth(canvasWidth + 200);
        mainPane.setCenter(new StackPane(canvas)); // 将画布容器放在中央区域
        mainPane.setRight(buttonBox); // 将按钮容器放在右侧区域
        BorderPane.setMargin(buttonBox, new Insets(0, 10, 0, 10)); // 确保 VBox 的外边距

        // 创建场景并添加主面板
        Scene scene = new Scene(mainPane, canvasWidth + 200, canvasHeight);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    //绘图
    public void render(MazeData data) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int w = canvasWidth / data.M();
        int h = canvasHeight / data.N();

        for (int i = 0; i < data.N(); i++) {
            for (int j = 0; j < data.M(); j++) {
                if (data.inMist[i][j]) {
                    AlgoVisHelper.setColor(gc, AlgoVisHelper.White);//迷雾
                } else if (data.maze[i][j] == MazeData.WALL) {
                    AlgoVisHelper.setColor(gc, AlgoVisHelper.Yellow);//墙壁绘制
                } else {
                    AlgoVisHelper.setColor(gc, AlgoVisHelper.White);//通路绘制
                }

                if (data.path[i][j])
                    AlgoVisHelper.setColor(gc, AlgoVisHelper.Blue);//路径显示

                if(data.result[i][j])
                    AlgoVisHelper.setColor(gc, AlgoVisHelper.Red);//BFS的路径显示

                AlgoVisHelper.fillRectangle(gc, j * w, i * h, w, h);
            }
        }
    }

    // 获取 Canvas 对象
    public Canvas getCanvas() {
        return canvas;
    }


    // Getter for buttons
    public Button getManualButton() {
        return manualButton;
    }

    public Button getAutoDFButton(){
        return autoDFButton;
    }
    public Button getClearButton(){return clearButton;}

    public Button getAutoBFButton() {
        return autoBFButton;
    }

    public Button getDfsButton() {
        return dfsButton;
    }

    public Button getBfsButton() {
        return bfsButton;
    }

    public void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}