package client;

import javax.swing.*;
import java.awt.event.*;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class GraphicFieldGame extends Thread
{
    private Queue<KeyEvent> keyEvents = new ArrayBlockingQueue<KeyEvent>(100);
    private JFrame frame;
    private MyFrame myFrame;

    public GraphicFieldGame() {
        this.myFrame = new MyFrame();
    }

    @Override
    public void run()
    {
        frame = new JFrame("KeyPress");
        frame.setTitle("NetSnake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.add(myFrame);
        frame.setVisible(true);

        frame.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                //do nothing
            }

            @Override
            public void focusLost(FocusEvent e)
            {

            }
        });

        frame.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                keyEvents.add(e);
            }
        });
    }

    public boolean hasKeyEvents()
    {
        return !keyEvents.isEmpty();
    }

    public KeyEvent getEventFromTop()
    {
        return keyEvents.poll();
    }

    public void refreshGraphicFieldGame(String data){
        this.myFrame.drow(data);
    }
}
