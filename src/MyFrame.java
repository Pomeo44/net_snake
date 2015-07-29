import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by Pomeo on 28.07.2015.
 */
public class MyFrame extends JPanel {

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void drow(String data){
        Graphics g = getGraphics();

        g.clearRect(0,0,500,500);

        String stringNumber = "";
        int x = 0;
        int y = 0;
        boolean isFood = false;
        boolean isSnake = false;
        for (char c : data.toCharArray()) {
            if (c == '[' | c == ']' | c == '{' | c == '}') {
                if (c == '{'){
                    isFood = true;
                    isSnake = false;
                }
                if (c == '['){
                    isFood = false;
                    isSnake = true;
                }
                continue;
            }
            if (c == ':') {
                x = Integer.parseInt(stringNumber);
                stringNumber = "";
                continue;
            }
            if (c == ';') {
                y = Integer.parseInt(stringNumber);
                if (isFood){
                    g.setColor(Color.RED);
                }
                if (isSnake){
                    g.setColor(Color.BLACK);
                }
                g.fillOval(x * 10, y * 10, 10, 10);
                stringNumber = "";
                continue;
            }
            stringNumber += c;
        }
    }
}
