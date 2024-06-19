//迷宫生成和寻路算法的实现，
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AlgoVisualizer extends Application {

    private static int DELAY = 5;
    private static int blockSide = 8;

    private MazeData data;
    private AlgoFrame frame;
    private static final int d[][] = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // 上，右，下，左

    private List<List<Position>> allPaths;

    public AlgoVisualizer() {
    }

    @Override
    public void start(Stage primaryStage) {


        int sceneHeight = 101 * blockSide;
        int sceneWidth = 101 * blockSide;

        frame = new AlgoFrame("Random Maze Generation Visualization", sceneWidth, sceneHeight);
        frame.start(primaryStage);

        // 按钮事件处理
        frame.getManualButton().setOnAction(e -> manualInput(primaryStage));
        frame.getAutoDFButton().setOnAction(e->autoDFGenerate());
        frame.getAutoBFButton().setOnAction(e -> autoBFGenerate());
        frame.getDfsButton().setOnAction(e->runDfs());
        frame.getBfsButton().setOnAction(e->runBfs());
        frame.getClearButton().setOnAction(e->Clear());

    }

    //手动输入生成迷宫
    private void manualInput(Stage primaryStage) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择输入文件");
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {

            // 清除当前画布内容
            clearCanvas();

            // 读取文件内容并重新初始化迷宫数据
            data = new MazeData(file.getAbsolutePath());

            // 重新计算画布大小
            int sceneHeight = data.N() * blockSide;
            int sceneWidth = data.M() * blockSide;
            frame.setCanvasWidth(sceneWidth);
            frame.setCanvasHeight(sceneHeight);

            Platform.runLater(() -> {
                frame.render(data);
            });

        }
    }

    //BFS自动生成迷宫
    private void autoBFGenerate() {
        // 清除当前画布内容
        clearCanvas();

        //重新初始化迷宫
        data = new MazeData(101,101);

        /*Platform.runLater(() -> {
            frame.render(data); // 重新渲染
        });*/

        //初始化迷宫绘制
        new Thread(() -> {
            generateBFMaze();

        }).start();
    }

    //DFS自动生成迷宫
    private void autoDFGenerate(){
        clearCanvas();
        data = new MazeData(101,101);
        Platform.runLater(() -> {
            frame.render(data); // 重新渲染
        });
        new Thread(()->{
                generateDFMaze();
        }).start();
    }

    //清除画布
    private void clearCanvas() {
        Platform.runLater(() -> {
            frame.getCanvas().getGraphicsContext2D().clearRect(0, 0, frame.getCanvas().getWidth(), frame.getCanvas().getHeight());
        });
    }

    private void Clear(){
        clearCanvas();
        data = null;
    }

    //更新迷宫数据
    private void setData(int x, int y) {
        if (data.inArea(x, y)) {
            data.maze[x][y] = MazeData.ROAD; // 打通墙壁
        }
        Platform.runLater(() -> frame.render(data)); // 实时更新绘制迷宫
        AlgoVisHelper.pause(DELAY);
    }

    private void setData(int x, int y, boolean isPath) {
        if (data.inArea(x, y)) {
            data.path[x][y] = isPath;//路径显示
        }
        Platform.runLater(() -> frame.render(data));
        AlgoVisHelper.pause(DELAY);
    }

    //DFS生成迷宫
    private void generateDFMaze() {
        setData(-1, -1);

        // 从起点开始
        Position first = new Position(data.getEntranceX(), data.getEntranceY() + 1);
        data.visited[first.getX()][first.getY()] = true;
        data.openMist(first.getX(), first.getY());
        dfsGenerate(first);

        setData(-1, -1);
    }

    private void dfsGenerate(Position pos) {
        // 随机打乱方向数组
        shuffleDirections();

        for (int[] dir : d) {
            //新坐标
            int newX = pos.getX() + dir[0] * 2;
            int newY = pos.getY() + dir[1] * 2;

            if (data.inArea(newX, newY) && !data.visited[newX][newY] && data.maze[newX][newY] == MazeData.ROAD) {
                data.visited[newX][newY] = true;
                data.openMist(newX, newY);
                setData(pos.getX() + dir[0], pos.getY() + dir[1]); // 打通墙壁并绘制显示
                dfsGenerate(new Position(newX, newY));
            }
        }
    }

    //打乱方向数组
    private void shuffleDirections() {
            Random rand = new Random();
            for (int i = 0; i < d.length; i++) {
                int j = rand.nextInt(d.length);
                int[] temp = d[i];
                d[i] = d[j];
                d[j] = temp;
            }
    }

    //BFS生成迷宫
    private void generateBFMaze() {
        setData(-1, -1);

        RandomQueue<Position> queue = new RandomQueue<>();
        Position first = new Position(data.getEntranceX(), data.getEntranceY() + 1);
        queue.add(first);
        data.visited[first.getX()][first.getY()] = true;
        data.openMist(first.getX(), first.getY());

        while (queue.size() != 0) {
            //随机移除队列元素
            Position curPos = queue.remove();
            shuffleDirections();

            for (int i = 0; i < 4; i++) {
                int newX = curPos.getX() + d[i][0] * 2;
                int newY = curPos.getY() + d[i][1] * 2;

                if (data.inArea(newX, newY) && !data.visited[newX][newY] && data.maze[newX][newY] == MazeData.ROAD) {
                    queue.add(new Position(newX, newY));
                    data.visited[newX][newY] = true;
                    data.openMist(newX, newY);
                    setData(curPos.getX() + d[i][0], curPos.getY() + d[i][1]);//打通墙壁并绘制显示
                }
            }
        }

        setData(-1, -1);
    }


    //Dfs寻路
    private void runDfs() {
        if (data == null) {
            Platform.runLater(() -> frame.showAlert("请先生成迷宫，才能进行寻路"));
            return;
        }

        new Thread(() -> {
            resetPathData();
            allPaths = new ArrayList<>();
            List<Position> currentPath = new ArrayList<>();
            findAllPaths(data.getEntranceX(), data.getEntranceY(), currentPath);
            displayAllPaths();
        }).start();
    }

    //重置路径数据
    private void resetPathData() {
        for (int i = 0; i < data.N(); i++) {
            for (int j = 0; j < data.M(); j++) {
                data.visited[i][j] = false;
                data.path[i][j] = false;
                data.result[i][j] = false;
            }
        }
        data.count = 0;

    }

    //Dfs寻路
    private void findAllPaths(int x, int y, List<Position> currentPath) {
        if (!data.inArea(x, y)) {
            throw new IllegalArgumentException("x,y are out of index in findAllPaths function!");
        }

        data.visited[x][y] = true;
        currentPath.add(new Position(x, y));
        setData(x, y, true);

        if (x == data.getExitX() && y == data.getExitY()) {
            allPaths.add(new ArrayList<>(currentPath));
            data.count++;//迷宫路径数加1
        }
        else {
            for (int i = 0; i < 4; i++) {
                int newX = x + d[i][0];
                int newY = y + d[i][1];
                if (data.inArea(newX, newY) && data.getMaze(newX, newY) == MazeData.ROAD &&
                        !data.visited[newX][newY])
                {
                    findAllPaths(newX, newY, currentPath);
                }
            }
        }

        currentPath.remove(currentPath.size() - 1);
        data.visited[x][y] = false;
        setData(x, y, false);
    }

    //展示所有路径
    private void displayAllPaths() {
        for (int i = 0; i < allPaths.size(); i++) {
            List<Position> path = allPaths.get(i);
            //第一条路径的显示
            for (int j = 0; j < path.size(); j++) {
                Position pos = path.get(j);
                setData(pos.getX(), pos.getY(), true); // 设置路径为真，显示路径
            }

            if (data.count > 1)
            {
                AlgoVisHelper.pause(1000); // 暂停一段时间以显示每条路径
                for (int j = 0; j < path.size(); j++)
                {
                    Position pos = path.get(j);
                    setData(pos.getX(), pos.getY(), false); // 清除路径，准备显示下一条路径
                }
                data.count--;//防止最后一条显示被重置
            }

            else {
                break;
            }
        }
    }


    //BFS寻路
    private void runBfs(){
        if(data == null){
            Platform.runLater(()->frame.showAlert("请先生成迷宫，才能进行寻路"));
            return;
        }

        new Thread(()->{
            resetPathData();//重置迷宫信息
            runBfsAlgorithm();
        }).start();
    }

    //BFS寻路
    private void runBfsAlgorithm(){

        setData(-1, -1, false);

        LinkedList<Position> queue = new LinkedList<>();
        Position entrance = new Position(data.getEntranceX(), data.getEntranceY());
        queue.addLast(entrance);
        data.visited[entrance.getX()][entrance.getY()] = true;

        boolean isSolved = false;//是否找到路径

        while(queue.size() != 0){
            Position curPos = queue.pop();
            setData(curPos.getX(), curPos.getY(), true);

            //找寻出口的标志isSolved
            if(curPos.getX() == data.getExitX() && curPos.getY() == data.getExitY()){
                isSolved = true;
                findPath(curPos);
                break;
            }

            //未寻到出口则继续遍历
            for(int i = 0 ; i < 4  ; i ++){
                int newX = curPos.getX() + d[i][0];
                int newY = curPos.getY() + d[i][1];

                if(data.inArea(newX, newY)
                        && !data.visited[newX][newY]
                        && data.getMaze(newX, newY) == MazeData.ROAD){
                    queue.addLast(new Position(newX, newY, curPos));
                    data.visited[newX][newY] = true;
                }
            }

        }

        if(!isSolved)
            System.out.println("The maze has no Solution!");

        //更新显示
        setData(-1, -1, false);
    }

    private void findPath(Position des){

        Position cur = des;

        //回溯标记出寻找到的最短路径
        while(cur != null){
            data.result[cur.getX()][cur.getY()] = true;
            cur = cur.getPrev();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}