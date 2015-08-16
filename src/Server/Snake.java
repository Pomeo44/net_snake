package server;

import java.util.LinkedList;

public class Snake {

    private int number;
    private Command command;
    private LinkedList<Coordinates> body;
    private Coordinates oldTail;
    private boolean isGameOver;
    private boolean snakeWin;

    Snake(int number) {
        this.number = number;
        this.command = Command.MOVE_UP;
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

    public void setCommand(Command command) {
        if (command == Command.MOVE_UP && this.command == Command.MOVE_DOWN){
            return;
        }
        if (command == Command.MOVE_DOWN && this.command == Command.MOVE_UP){
            return;
        }
        if (command == Command.MOVE_RIGHT && this.command == Command.MOVE_LEFT){
            return;
        }
        if (command == Command.MOVE_LEFT && this.command == Command.MOVE_RIGHT){
            return;
        }
        this.command = command;
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

        if (command == Command.MOVE_LEFT){
            body.addFirst(new Coordinates(head.x - 1, head.y));
        }
        else if (command == Command.MOVE_RIGHT){
            body.addFirst(new Coordinates(head.x + 1, head.y));
        }
        else if (command == Command.MOVE_UP){
            body.addFirst(new Coordinates(head.x, head.y - 1));
        }
        else if (command.equals(Command.MOVE_DOWN)){
            body.addFirst(new Coordinates(head.x, head.y + 1));
        }
    }

    public void moveBack(){
        body.removeFirst();
        body.addLast(oldTail);
    }
}
