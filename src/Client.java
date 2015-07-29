/**
 * Created by Pomeo on 27.07.2015.
 */

//import java.io.*;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

class Client {

    public static void main(String args[]) {
        try
        {
            short port = 3000;
            Socket socket = new Socket("localhost", port);

            String send = "";
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            GraphicFieldGame graphicFieldGame = new GraphicFieldGame();
            graphicFieldGame.start();

            boolean isGameOver = false;

            while(!isGameOver) {

                if (graphicFieldGame.hasKeyEvents()) {

                    KeyEvent event = graphicFieldGame.getEventFromTop();
                    if (event.getKeyCode() == KeyEvent.VK_ESCAPE | event.getKeyCode() == KeyEvent.VK_LEFT | event.getKeyCode() == KeyEvent.VK_RIGHT
                            | event.getKeyCode() == KeyEvent.VK_UP | event.getKeyCode() == KeyEvent.VK_DOWN){
                        if (event.getKeyChar() == KeyEvent.VK_ESCAPE){
                            send = "exit";
                        }
                        else if (event.getKeyCode() == KeyEvent.VK_LEFT){
                            send = Move.LEFT;
                        }
                        else if (event.getKeyCode() == KeyEvent.VK_RIGHT){
                            send = Move.RIGT;
                        }
                        else if (event.getKeyCode() == KeyEvent.VK_UP){
                            send = Move.UP;
                        }
                        else if (event.getKeyCode() == KeyEvent.VK_DOWN){
                            send = Move.DOWN;
                        }
                        System.out.println(send);
                        out.write(send.getBytes());
                        out.flush();
                        if (send.equals("exit")){
                            break;
                        }
                    }
                }
                if (in.available() > 0){
                    byte[] bytes = new byte[in.available()];
                    in.read(bytes);
                    String data = new String(bytes);
                    System.out.println("Client get date:" + data);
                    graphicFieldGame.refreshGraphicFieldGame(data);
                }
            }
            in.close();
            out.close();
            socket.close();
        }
        catch(Exception e){
            System.out.println("init error: "+e);
        }
    }
}