package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ClientWorker {

    private SocketChannel clientSocket;
    private int number;
    private Snake snake;
    private boolean clientConnected;
    private String request;
    long oldStateDataField = 0;
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    public ClientWorker(SocketChannel clientSocket, int clientNumber) {
        this.clientSocket = clientSocket;
        this.number = clientNumber;
        this.snake = Server.getFieldGame().getNewSnake(number);
        this.clientConnected = true;
    }

    public void read(SelectionKey key) throws IOException {
        try {
            if (clientSocket.read(byteBuffer) == -1){
                byteBuffer.flip();
                String data = new String(byteBuffer.array());
                System.out.println("ClientWorker " + number + " get date:" + data);
                if (data.equals(Command.EXIT.name())){

                } else if (data.equals(Command.MOVE_UP.name())){
                    snake.setCommand(Command.MOVE_UP);
                }
                else if (data.equals(Command.MOVE_DOWN.name())){
                    snake.setCommand(Command.MOVE_DOWN);
                }
                else if (data.equals(Command.MOVE_RIGHT.name())){
                    snake.setCommand(Command.MOVE_RIGHT);
                }
                else if (data.equals(Command.MOVE_LEFT.name())){
                    snake.setCommand(Command.MOVE_LEFT);
                }
                byteBuffer.clear();
            }
        } catch (IOException e) {
            System.out.println("ClientWorker init error: " + e);
            clientSocket.close();
            snake.setIsGameOver(true);
        }
    }

    public void write(SelectionKey key) throws IOException {
        try {
            long currentServerStateDataField = Server.getFieldGame().getStateDataField();
            if (oldStateDataField != currentServerStateDataField) {

                String data = Server.getFieldGame().getDataFieldForClient();
                String stateGameAndGamer = "";
                if (Server.getFieldGame().getIsGameEnd()) {
                    stateGameAndGamer += "-";
                    if (snake.isSnakeWin()) {
                        stateGameAndGamer += "+";
                    } else {
                        stateGameAndGamer += "-";
                    }
                } else {
                    stateGameAndGamer += "+-";
                }
                byteBuffer.flip();
                byteBuffer.put((stateGameAndGamer + data).getBytes());
                clientSocket.write(byteBuffer);
                byteBuffer.clear();
                oldStateDataField = currentServerStateDataField;
            }
        } catch (IOException e) {
            System.out.println("ClientWorker init error: " + e);
            clientSocket.close();
            snake.setIsGameOver(true);
        }


    }
}
