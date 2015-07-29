import javax.print.attribute.SetOfIntegerSyntax;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Pomeo on 28.07.2015.
 */
public class FieldGame extends Thread{

    private int width;
    private int height;
    private ArrayList<Snake> snakes;
    private Set<Integer[]> foodXY;
    private int maxVolumeFood;

    private String dataFieldForClient;
    private long stateDataField;

    private boolean isGameEnd;

    public FieldGame(int height, int width, int maxVolumeFood) {
        this.height = height;
        this.width = width;
        this.snakes = new ArrayList<Snake>();
        this.foodXY = new HashSet<>();
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
                    snake.move();
                }
                addFieldFood();
                if (snakes.size() > 0){
                    makeDataFieldForClient();
                    stateDataField++;
                }
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
        for (Integer[] xy:foodXY){
            dataFieldForClient += xy[0] + ":" + xy[1] + ";";
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
        if (foodXY.size() < maxVolumeFood ){
            Random random = new Random();
            int x;
            int y;
            do {
                x = random.nextInt(width);
                y = random.nextInt(height);
            } while (!xyFree(x,y));
            Integer[] xy = {x,y};
            foodXY.add(xy);
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
}
