package server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FieldGame extends Thread{

    private int width;
    private int height;
    private ArrayList<Snake> snakes;
    private Set<Integer[]> xyFoods;
    private int maxVolumeFood;

    private String dataFieldForClient;
    private long stateDataField;

    private boolean isGameEnd;

    public FieldGame(int height, int width, int maxVolumeFood) {
        this.height = height;
        this.width = width;
        this.snakes = new ArrayList<>();
        this.xyFoods = new HashSet<Integer[]>();
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
        ArrayList<Integer[]> body = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            Integer[] xy = {width/2, height/2 + i};
            body.add(xy);
        }
        snake.setBody(body);
        return snake;
    }

    private void makeDataFieldForClient(){
        dataFieldForClient = "";
        dataFieldForClient += "{";
        for (Integer[] xyFood:xyFoods){
            dataFieldForClient += xyFood[0] + ":" + xyFood[1] + ";";
        }
        dataFieldForClient += "}";
        for (Snake snake:snakes){
            dataFieldForClient += "[";
            ArrayList<Integer[]> bodySnake = snake.getBody();
            for (Integer[] xy:bodySnake){
                dataFieldForClient += xy[0] + ":" + xy[1] + ";";
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
        if (xyFoods.size() < maxVolumeFood){
            Random random = new Random();
            int x;
            int y;
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
            } while (!xyFree(x,y));
            Integer[] xy = {x,y};
            xyFoods.add(xy);
        }
    }

    private boolean xyFree(int x, int y){
        boolean result = true;
        for (Snake snake:snakes){
            ArrayList<Integer[]> bodySnake = snake.getBody();
            for (Integer[] xy:bodySnake){
                if (x == xy[0] & y == xy[1]){
                    result = false;
                }
            }
        }
        return result;
    }

    private void checkFieldSGame(){

        for (Snake snake:snakes){
            Integer[] xyHead = snake.getBody().get(0);
            //1 out border
            if ((xyHead[0] < 0 | xyHead[0] > width) | xyHead[1] < 0 | xyHead[1] > height){
                snake.setIsGameOver(true);
                snake.moveBack();
            }
            //2 eat food
            for (Integer[] xyFood:xyFoods){
                if (xyHead[0] == xyFood[0] & xyHead[1] == xyFood[1]){
                    snake.addFoodInSnake();
                    xyFoods.remove(xyFood);
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
