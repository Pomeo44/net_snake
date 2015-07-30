package client;

import server.Move;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Client {

    public static void main(String args[]) throws IOException {
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        try
        {

            int port = 3000;
            socket = new Socket("localhost", port);
            in = socket.getInputStream();
            out = socket.getOutputStream();

            GraphicFieldGame graphicFieldGame = new GraphicFieldGame();
            graphicFieldGame.start();

            boolean isGameOver = false;
            String send = "";

            while(!isGameOver) {

                if (graphicFieldGame.hasKeyEvents()) {

                    KeyEvent event = graphicFieldGame.getEventFromTop();
                    switch(event.getKeyCode()) {
                        case KeyEvent.VK_ESCAPE:
                            send = "exit";
                            break;
                        case KeyEvent.VK_LEFT:
                            send = Move.LEFT.name();
                            break;
                        case KeyEvent.VK_RIGHT:
                            send = Move.RIGT.name();
                            break;
                        case KeyEvent.VK_UP:
                            send = Move.UP.name();
                            break;
                        case KeyEvent.VK_DOWN:
                            send = Move.DOWN.name();
                            break;
                        default:
                            break;
                    }
                    if (!send.isEmpty()){
                        System.out.println(send);
                        out.write(send.getBytes());
                        out.flush();
                        if (send.equals("exit")){
                            isGameOver = true;
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
            System.out.println("Client init error: "+e);
        }
        finally {
            in.close();
            out.close();
            socket.close();
        }
    }
}