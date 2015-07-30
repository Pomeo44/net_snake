package server;

import java.util.*;

public class FieldGame extends Thread{

    private int width;
    private int height;
    private ArrayList<Snake> snakes;
    private Set<Coordinates> coordinatesFoods;
    private int maxVolumeFood;

    private String dataFieldForClient;
    private long stateDataField;

    private boolean isGameEnd;

    public FieldGame(int height, int width, int maxVolumeFood) {
        this.height = height;
        this.width = width;
        this.snakes = new ArrayList<>();
        this.coordinatesFoods = new HashSet<>();
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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Snake getNewSnake(int number){
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
                if (x == coordinates.x & y == coordinates.y){
                    result = false;
                }
            }
        }
        return result;
    }

    private void checkFieldSGame(){

        for (Snake snake:snakes){
            Coordinates coordinatesHead = snake.getBody().getFirst();
            //1 out border
            if ((coordinatesHead.x < 0 || coordinatesHead.x > width) | coordinatesHead.y < 0 | coordinatesHead.y > height){
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
            /*for (Snake snakeOther:snakes){
                for (Integer[] xyElementBody:snakeOther.getBody()) {
                    if (xyHead.equals(xyElementBody)){
                        continue;
                    }
                    if (xyHead[0] == xyElementBody[0] | xyHead[1] == xyElementBody[1]){
                        if (snakeOther.getBody().get(0).equals(xyElementBody)){ //two snakes clashed heads
                            snake.setIsGameOver(true);
                            snakeOther.setIsGameOver(true);
                        }
                        else {
                            snake.setIsGameOver(true);
                            snake.moveBack();
                        }
                    }
                }
            }*/
        }
    }
}
