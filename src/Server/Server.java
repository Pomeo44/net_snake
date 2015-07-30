package server;

import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class Server {

    private static FieldGame fieldGame;
    private static Set<ClientWorker> clientWorkers = new HashSet();

    public static void main(String args[])
    {
        ServerSocket serverSoket;
        try
        {
            int clientNumber = 0;
            int port = 3000;
            if (args.length == 1) {
                if (!args[0].isEmpty()) {
                    port = Integer.parseInt(args[0]);
                }
            }

            serverSoket = new ServerSocket(port);
            System.out.println("Server is started");

            fieldGame = new FieldGame(40, 40, 2, 3);

            Socket clientSocket;
            while(true){
                if (clientWorkers.size() < fieldGame.getMaxVolumeGamer()){
                    System.out.println("Wait connect...");
                    clientSocket = serverSoket.accept();
                    clientWorkers.add(new ClientWorker(clientSocket, clientNumber));
                    System.out.println("New ClientWorker creat");
                    clientNumber++;
                }
                else {
                    Thread.sleep(1000);
                    if (fieldGame.getIsGameEnd()){
                        Thread.sleep(3000);
                        rebootFieldGame();
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Server init error: "+e);
        }
    }

    public static FieldGame getFieldGame() {
        return fieldGame;
    }

    private static void rebootFieldGame(){
        for (ClientWorker clientWorker:clientWorkers){
            clientWorker.interrupt();
        }
        clientWorkers.clear();
        fieldGame = new FieldGame(40, 40, 2, 3);
    }
}