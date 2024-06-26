import javafx.geometry.Pos;

public class Position {

    private int x,y;
    private Position prev;//存储前一个位置

    //x为行号，y是列号
    public Position(int x,int y,Position prev){
        this.x = x;
        this.y = y;
        this.prev = prev;
    }

    public Position(int x,int y){
        this(x,y,null);
    }

    public int getX(){return x;}
    public int getY(){return y;}

    public Position getPrev(){
        return prev;
    }

}
