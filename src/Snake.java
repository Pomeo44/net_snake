
import java.util.ArrayList;

/**
 * Created by Pomeo on 27.07.2015.
 */
public class Snake {

    private int number;
    private String move;
    private ArrayList<Integer[]> body;
    private boolean isGameOver;

    Snake(int number) {
        this.number = number;
        this.move = Move.UP;
        this.isGameOver = false;
    }

    public int getNumber() {
        return number;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public ArrayList<Integer[]> getBody() {
        return body;
    }

    public void setMove(String move) {
        /*if (move.equals(Move.UP) & this.move.equals(Move.DOWN)){
            return;
        }//какая то ошибка
        if (move.equals(Move.DOWN) & this.move.equals(Move.UP)){
            return;
        }*/
        if (move.equals(Move.RIGT) & this.move.equals(Move.LEFT)){
            return;
        }
        if (move.equals(Move.LEFT) & this.move.equals(Move.RIGT)){
            return;
        }
        this.move = move;
    }

    public void setBody(ArrayList<Integer[]> body) {
        this.body = body;
    }

    public void addFoodInSnake(){

    }

    public void move(){

        for (int i = body.size() - 1; i > 0; i--){
            body.set(i, body.get(i-1));
        }
        if (move.equals(Move.LEFT)){
            Integer[] head = body.get(0).clone();
            head[0]--;
            body.set(0, head);
        }
        else if (move.equals(Move.RIGT)){
            Integer[] head = body.get(0).clone();
            head[0]++;
            body.set(0, head);
        }
        else if (move.equals(Move.UP)){
            Integer[] head = body.get(0).clone();
            head[1]--;
            body.set(0, head);
        }
        else if (move.equals(Move.DOWN)){
            Integer[] head = body.get(0).clone();
            head[1]++;
            body.set(0, head);
        }

    }

    public void moveBack(){

        /*for (int i = body.size() - 1; i > 0; i--){
            body.set(i, body.get(i-1));
        }
        if (move.equals(Move.LEFT)){
            Integer[] head = body.get(0).clone();
            head[0]--;
            body.set(0, head);
        }
        else if (move.equals(Move.RIGT)){
            Integer[] head = body.get(0).clone();
            head[0]++;
            body.set(0, head);
        }
        else if (move.equals(Move.UP)){
            Integer[] head = body.get(0).clone();
            head[1]--;
            body.set(0, head);
        }
        else if (move.equals(Move.DOWN)){
            Integer[] head = body.get(0).clone();
            head[1]++;
            body.set(0, head);
        }*/

    }
}
