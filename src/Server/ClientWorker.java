package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Pomeo on 28.07.2015.
 */
public class ClientWorker  extends Thread {

    private Socket soket;
    private int number;
    private Server.Snake snake;

    public ClientWorker(Socket clientSocket, int clientNumber) {
        this.soket = clientSocket;
        this.number = clientNumber;
        this.snake = Server.getFieldGame().getNewSnake(number);
        start();
    }

    public void run() {
        try {
            InputStream in = soket.getInputStream();
            long oldStateDataField = 0;
            while (true){
                if (isInterrupted()){
                    break;
                }
                if (in.available() > 0){
                    byte[] bytes = new byte[in.available()];
                    in.read(bytes);
                    String data = new String(bytes);
                    System.out.println("ClientWorker " + number + " get date:" + data);
                    if (data.equals("exit")){
                        break;
                    }
                    if (data.equals(Move.UP)){
                        snake.setMove(Move.UP);
                    }
                    else if (data.equals(Move.DOWN)){
                        snake.setMove(Move.DOWN);
                    }
                    else if (data.equals(Move.RIGT)){
                        snake.setMove(Move.RIGT);
                    }
                    else if (data.equals(Move.LEFT)){
                        snake.setMove(Move.LEFT);
                    }
                }
                if (oldStateDataField != Server.getFieldGame().getStateDataField()){
                    updataGraphicFieldGame(Server.getFieldGame().getDataFieldForClient());
                    oldStateDataField = Server.getFieldGame().getStateDataField();
                }
            }
            in.close();
            soket.close();
        }
        catch (IOException e) {
            System.out.println("ClientWorker init error: " + e);
        }

    }

    private void updataGraphicFieldGame(String data){
        OutputStream out = null;
        try {
            out = soket.getOutputStream();
            System.out.println("Out client " + data);
            out.write(data.getBytes());

        } catch (IOException e) {
            System.out.println("ClientWorker init error updata client: " + e);
        }

    }
}
