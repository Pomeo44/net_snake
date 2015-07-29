package server;

import java.net.*;
import java.util.HashSet;
import java.util.Set;


public class Server {

    private static FieldGame fieldGame;
    private static Set<ClientWorker> clientWorkers = new HashSet<ClientWorker>();

    public static void main(String args[])
    {
        ServerSocket serverSoket;
        try
        {
            int clientNumber = 0;
            int port = 3000;
            serverSoket = new ServerSocket(port);
            System.out.println("Server is started");

            fieldGame = new FieldGame(40,40,3);

            Socket clientSocket;
            while(true)
            {
                System.out.println("Wait connect...");
                clientSocket = serverSoket.accept();
                clientWorkers.add(new ClientWorker(clientSocket, clientNumber));
                System.out.println("New ClientWorker creat");
                clientNumber++;
            }

        }
        catch(Exception e){
            System.out.println("Server init error: "+e);
        }
    }

    public static FieldGame getFieldGame() {
        return fieldGame;
    }
}