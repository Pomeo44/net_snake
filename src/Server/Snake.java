package server;

import java.util.LinkedList;

public class Snake {

    private int number;
    private Move move;
    private LinkedList<Coordinates> body;
    private Coordinates oldTail;
    private boolean isGameOver;
    private boolean snakeWin;

    Snake(int number) {
        this.number = number;
        this.move = Move.UP;
        this.isGameOver = false;
        this.snakeWin = false;
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

    public boolean isSnakeWin() {
        return snakeWin;
    }

    public void setSnakeWin(boolean snakeWin) {
        this.snakeWin = snakeWin;
    }

    public LinkedList<Coordinates> getBody() {
        return body;
    }

    public void setMove(Move move) {
        if (move == Move.UP && this.move == Move.DOWN){
            return;
        }
        if (move == Move.DOWN && this.move == Move.UP){
            return;
        }
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

        if (move == Move.LEFT){
            body.addFirst(new Coordinates(head.x - 1, head.y));
        }
        else if (move == Move.RIGT){
            body.addFirst(new Coordinates(head.x + 1, head.y));
        }
        else if (move == Move.UP){
            body.addFirst(new Coordinates(head.x, head.y - 1));
        }
        else if (move.equals(Move.DOWN)){
            body.addFirst(new Coordinates(head.x, head.y + 1));
        }
    }

    public void moveBack(){
        body.removeFirst();
        body.addLast(oldTail);
    }
}
