package server;

import java.util.ArrayList;
import java.util.LinkedList;

public class Snake {

    private int number;
    private Move move;
    private LinkedList<Coordinates> body;
    private Coordinates oldTail;
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

    public LinkedList<Coordinates> getBody() {
        return body;
    }

    public void setMove(Move move) {
        /*if (move.equals(Move.UP) && this.move.equals(Move.DOWN)){
            return;
        }//какая то ошибка
        if (move.equals(Move.DOWN) && this.move.equals(Move.UP)){
            return;
        }*/
        if (move == Move.RIGT && this.move == Move.LEFT){
            return;
        }
        if (move == Move.LEFT && this.move == Move.RIGT){
            return;
        }
        this.move = move;
    }

    public void setBody(LinkedList<Coordinates> body) {
        this.body = body;
    }

    public void addFoodInSnake(){
        body.add(oldTail);
    }

    public void move(){
        Coordinates head = body.getFirst();
        oldTail = body.getLast();
        body.removeLast();
        body.add(new Coordinates(head.x, head.y--));

        /*if (move == Move.LEFT){
            body.addFirst(new Coordinates(head.x--, head.y));
        }
        else if (move == Move.RIGT){
            body.addFirst(new Coordinates(head.x++, head.y));
        }
        else if (move == Move.UP){
            body.add(new Coordinates(head.x, head.y--));
        }
        else if (move.equals(Move.DOWN)){
            body.addFirst(new Coordinates(head.x, head.y++));
        }*/
    }

    public void moveBack(){
        body.removeFirst();
        body.addLast(oldTail);
    }
}
