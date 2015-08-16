package client;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JPanel {

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(420, 500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void drow(String data){
        Graphics g = getGraphics();

        if (g == null) return;
        if (data.isEmpty()) return;
        g.clearRect(0,0,420,500);

        String stringNumber = "";
        int x = 0;
        int y = 0;
        boolean isFood = false;
        boolean isSnake = false;
        char[] stateGamerAndGame = data.toCharArray();
        if (stateGamerAndGame[0] == '+'){
            g.drawString("Game play", 10, 430);
        }
        else {
            g.drawString("Game end", 10, 430);
            if (stateGamerAndGame[1] == '+'){
                g.drawString("You Win!!!", 10, 450);
            }
            else {
                g.drawString("You lose!", 10, 450);
            }
        }
        data = data.substring(2);
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
