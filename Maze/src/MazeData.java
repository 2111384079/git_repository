import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class MazeData {

    public static final char ROAD = ' ';
    public static final char WALL = '#';

    private int N, M;
    public char[][] maze;
    public boolean[][] visited;
    public boolean[][] inMist;
    public boolean[][] path;
    public boolean[][] result;
    public int count;
    private int entranceX, entranceY;
    private int exitX, exitY;

    // 自动生成的迷宫初始化
    public MazeData(int N, int M) {
        // 行列数必须为奇数
        if (N % 2 == 0 || M % 2 == 0)
            throw new IllegalArgumentException("Our Maze Generalization Algorihtm requires the width and height of the maze are odd numbers");

        this.N = N;
        this.M = M;

        maze = new char[N][M];
        visited = new boolean[N][M];
        inMist = new boolean[N][M];
        path = new boolean[N][M];
        result = new boolean[N][M];

        // 迷宫初始化为单元格
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (i % 2 == 1 && j % 2 == 1)
                    maze[i][j] = ROAD;
                else
                    maze[i][j] = WALL;

                // 初始时，全部单元格未访问，都 inMist
                visited[i][j] = false;
                inMist[i][j] = true;
                path[i][j] = false;
                result[i][j] = false;
            }
        }
        // 出、入口坐标
        entranceX = 1;
        entranceY = 0;
        exitX = N - 2;
        exitY = M - 1;

        // 入口和出口
        maze[entranceX][entranceY] = ROAD;
        maze[exitX][exitY] = ROAD;
    }

    // 手动输入迷宫的初始化
    public MazeData(String filename){

        if(filename == null)
            throw new IllegalArgumentException("Filename can not be null!");

        Scanner scanner = null;
        try{
            File file = new File(filename);
            if(!file.exists())
                throw new IllegalArgumentException("File " + filename + " doesn't exist");

            FileInputStream fis = new FileInputStream(file);
            scanner = new Scanner(new BufferedInputStream(fis), "UTF-8");

            // 读取第一行
            String nmline = scanner.nextLine();
            String[] nm = nmline.trim().split("\\s+");
            //System.out.print(nm[0] + ' ' + nm[1]);

            N = Integer.parseInt(nm[0]);
            // System.out.println("N = " + N);
            M = Integer.parseInt(nm[1]);
            // System.out.println("M = " + M);

            // 读取后续的N行
            visited = new boolean[N][M];
            path = new boolean[N][M];
            result = new boolean[N][M];
            maze = new char[N][M];
            inMist = new boolean[N][M];

            for(int i = 0 ; i < N ; i ++){
                String line = scanner.nextLine();

                // 每行保证有M个字符
                if(line.length() != M)
                    throw new IllegalArgumentException("Maze file " + filename + " is invalid");
                for(int j = 0 ; j < M ; j ++){
                    maze[i][j] = line.charAt(j);
                    visited[i][j] = false;
                    path[i][j] = false;
                    result[i][j] = false;
                    inMist[i][j] =false;
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally {
            if(scanner != null)
                scanner.close();
        }

        entranceX = 1;
        entranceY = 0;
        exitX = N - 2;
        exitY = M - 1;
    }

    public int N() {
        return N;
    }

    public int M() {
        return M;
    }

    public int getEntranceX() {
        return entranceX;
    }

    public int getEntranceY() {
        return entranceY;
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }

    //获取符号ROAD/WALL
    public char getMaze(int i, int j) {
        if (!inArea(i, j))
            throw new IllegalArgumentException("i or j is out of index in getMaze!");
        return maze[i][j];
    }

    // 判断坐标范围在迷宫内
    public boolean inArea(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < M;
    }

    // 打开迷雾
    public void openMist(int x, int y) {
        if (!inArea(x, y))
            throw new IllegalArgumentException("x or y is out of index in openMist function!");

        // 自己和周围共8位置全部打开迷雾，使得单元格可以显示
        for (int i = x - 1; i <= x + 1; i++)
            for (int j = y - 1; j <= y + 1; j++)
                if (inArea(i, j))
                    inMist[i][j] = false;
    }

    public void printMaze() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++)
                System.out.print(maze[i][j]);
            System.out.println();
        }
    }
}
