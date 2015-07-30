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
            String host = "localhost";
            int port = 3000;
            if (args.length == 2) {
                if (!args[0].isEmpty()) {
                    host = args[0];
                }
                if (!args[1].isEmpty()) {
                    port = Integer.parseInt(args[1]);
                }
            }
            System.out.println("Connect to " + host + ":" + port);
            socket = new Socket(host, port);
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
        catch (ConnectException e){
            System.out.println("Client not connect to server: "+e);
        }
        catch(Exception e){
            System.out.println("Client init error: "+e);
        }
        finally {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        }
    }
}