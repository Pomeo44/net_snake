package server;

import java.util.*;

public class FieldGame extends Thread{

    private int width;
    private int height;
    private ArrayList<Snake> snakes;
    private Set<Coordinates> coordinatesFoods;
    private int maxVolumeFood;
    private int maxVolumeGamer;

    private String dataFieldForClient;
    private long stateDataField;

    private boolean isGameEnd;

    public FieldGame(int height, int width, int maxVolumeGamer, int maxVolumeFood) {
        this.height = height;
        this.width = width;
        this.snakes = new ArrayList<>();
        this.coordinatesFoods = new HashSet<>();
        this.maxVolumeGamer = maxVolumeGamer;
        this.maxVolumeFood = maxVolumeFood;
        this.isGameEnd = false;
        this.stateDataField = 0;
        start();
    }

    @Override
    public void run() {
        while (!isGameEnd){
            try {
                for (Snake snake:snakes){
                    if (!snake.isGameOver()){
                        snake.move();
                    }
                }
                checkFieldSGame();
                addFieldFood();
                if (snakes.size() > 0){
                    makeDataFieldForClient();
                    stateDataField++;
                }
                isGameEnd = isGameEnd();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        woWin();
        makeDataFieldForClient();
        stateDataField++;
   }

    private boolean isGameEnd(){
        boolean result = true;
        if (snakes.size() == maxVolumeGamer){
            for (Snake snake:snakes){
                if (!snake.isGameOver()){
                    result = false;
                }
            }
        }
        else {
            result = false;
        }
        return result;
    }

    public boolean getIsGameEnd(){
        return isGameEnd;
    }

    public int getMaxVolumeGamer() {
        return maxVolumeGamer;
    }

    public Snake getNewSnake(int number){
        //данный метод помести в этот класс, чтобы при возможности, можно было реализовать появление змеи в любом месте поля
        Snake snake = new Snake(number);
        snakes.add(snake);
        LinkedList<Coordinates> body = new LinkedList<>();
        for (int i = 0; i < 3; i++){
            Coordinates coordinates = new Coordinates(width/2, height/2 + i);
            body.addLast(coordinates);
        }
        snake.setBody(body);
        return snake;
    }

    private void makeDataFieldForClient(){
        dataFieldForClient = "";
        dataFieldForClient += "{";
        for (Coordinates coordinatesFood:coordinatesFoods){
            dataFieldForClient += coordinatesFood.x + ":" + coordinatesFood.y + ";";
        }
        dataFieldForClient += "}";
        for (Snake snake:snakes){
            dataFieldForClient += "[";
            LinkedList<Coordinates> bodySnake = snake.getBody();
            for (Coordinates coordinates:bodySnake){
                dataFieldForClient += coordinates.x + ":" + coordinates.y + ";";
            }
            dataFieldForClient += "]";
            System.out.println(dataFieldForClient);
        }
    }

    public long getStateDataField() {
        return stateDataField;
    }

    public String getDataFieldForClient() {
        return dataFieldForClient;
    }

    private void addFieldFood(){
        if (coordinatesFoods.size() < maxVolumeFood){
            Random random = new Random();
            int x;
            int y;
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
            } while (!xyFree(x,y));
            coordinatesFoods.add(new Coordinates(x, y));
        }
    }

    private boolean xyFree(int x, int y){
        boolean result = true;
        for (Snake snake:snakes){
            LinkedList<Coordinates> bodySnake = snake.getBody();
            for (Coordinates coordinates:bodySnake){
                if (x == coordinates.x && y == coordinates.y){
                    result = false;
                }
            }
        }
        return result;
    }

    private void checkFieldSGame(){

        for (Snake snake:snakes){
            if (snake.isGameOver()) {
                continue;
            }
            Coordinates coordinatesHead = snake.getBody().getFirst();
            //1 out border
            if ((coordinatesHead.x < 0 || coordinatesHead.x > width) || coordinatesHead.y < 0 || coordinatesHead.y > height){
                snake.setIsGameOver(true);
                snake.moveBack();
            }
            //2 eat food
            for (Coordinates coordinatesFood:coordinatesFoods){
                if (coordinatesHead.x == coordinatesFood.x && coordinatesHead.y == coordinatesFood.y){
                    snake.addFoodInSnake();
                    coordinatesFoods.remove(coordinatesFood);
                    addFieldFood();
                    break;
                }
            }
            //3 other snake
            for (Snake snakeOther:snakes){
                for (Coordinates coordinatesElementBody:snakeOther.getBody()) {
                    if (coordinatesHead.equals(coordinatesElementBody)){
                        continue;
                    }
                    if (coordinatesHead.x == coordinatesElementBody.x && coordinatesHead.y == coordinatesElementBody.y){
                        if (snakeOther.getBody().getFirst().equals(coordinatesElementBody) && !snakeOther.isGameOver()){ //two snakes clashed heads
                            snake.setIsGameOver(true);
                            snakeOther.setIsGameOver(true);
                        }
                        else {
                            snake.setIsGameOver(true);
                            snake.moveBack();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void woWin(){
        int maxEtFood = 0;
        Snake winSnake = null;
        for (Snake snake:snakes){
            if (snake.getBody().size() > maxEtFood){
                maxEtFood = snake.getBody().size();
                winSnake = snake;
            }
        }
        winSnake.setSnakeWin(true);
    }
}