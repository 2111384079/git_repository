import java.util.ArrayList;
import java.util.LinkedList;


//RandomQueue泛类型,存储迷宫单元格信息
public class RandomQueue <E>{

    private LinkedList<E> queue;

    public RandomQueue(){

        queue = new LinkedList<E>();
    }

    //添加元素
    public void add(E e){

        queue.add(e);
    }

    public E pop(){
        return queue.pop();
    }


    //随机移除并返回队列元素
    public E remove() {
        if (queue.size() == 0)
            throw new IllegalArgumentException("There's no element to remove in Random queue");

        int randIndex = (int)(Math.random()*queue.size());

        E randElement = queue.get(randIndex);
        queue.set(randIndex,queue.get(queue.size()-1));
        queue.remove(queue.size()-1);

        return randElement;
    }

    //获取队列元素个数
    public int size(){
        return queue.size();
    }
    //判空
    public boolean empty() {
        return size() == 0;
    }
}
