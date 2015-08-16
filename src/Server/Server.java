package server;

import java.io.IOException;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    private static FieldGame fieldGame;
    private static Set<ClientWorker> clientWorkers = new HashSet();
    private Selector clientSelector;

    public void run(int port, int threads) throws IOException, InterruptedException {

        clientSelector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(), port);
        serverSocketChannel.socket().bind(socketAddress);
        serverSocketChannel.register(clientSelector, SelectionKey.OP_ACCEPT);

        Executor executor = Executors.newFixedThreadPool(0);

        System.out.println("Server is started");
        int clientNumber = 0;
        fieldGame = new FieldGame(40, 40, 2, 3);
        System.out.println("Wait connect...");
        while(true){
            while (clientSelector.select(50) == 0);
            Set<SelectionKey> readySet = clientSelector.selectedKeys();
            for (Iterator<SelectionKey> iterator = readySet.iterator(); iterator.hasNext();){
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable() || clientWorkers.size() < fieldGame.getMaxVolumeGamer()){
                    acceptClient(serverSocketChannel, clientNumber);
                    clientNumber++;
                    System.out.println("Wait connect...");
                }
                else {
                    key.interestOps(0);
                    executor.execute(new Runnable() {
                        public void run() {
                            try {
                                handlerClient(key);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                if (clientWorkers.size() < fieldGame.getMaxVolumeGamer()){
                    Thread.sleep(1000);
                    if (fieldGame.getIsGameEnd()){
                        Thread.sleep(3000);
                        rebootFieldGame(clientSelector);
                    }
                }
            }
        }
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        int port = 3000;
        if (args.length == 1) {
            if (!args[0].isEmpty()) {
                port = Integer.parseInt(args[0]);
            }
        }
        new Server().run(port, 0);
    }

    private void acceptClient(ServerSocketChannel serverSocketChannel, int clientNumber) throws IOException {
        SocketChannel clientSoket = serverSocketChannel.accept();
        clientSoket.configureBlocking(false);
        SelectionKey key = clientSoket.register(clientSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        ClientWorker clientWorker = new ClientWorker(clientSoket, clientNumber);
        clientWorkers.add(clientWorker);
        key.attach(clientWorker);
        System.out.println("New ClientWorker creat");
    }

    void handlerClient(SelectionKey key) throws IOException {
        ClientWorker clientWorker = (ClientWorker)key.attachment();
        if (key.isReadable()){
            clientWorker.read(key);
        } else {
            clientWorker.write(key);
        }
        clientSelector.wakeup();
    }

    public static FieldGame getFieldGame() {
        return fieldGame;
    }

    private static void rebootFieldGame(Selector clientSelector){
        Set<SelectionKey> readySet = clientSelector.selectedKeys();
        for (Iterator<SelectionKey> iterator = readySet.iterator(); iterator.hasNext();){
            SelectionKey key = iterator.next();
            key.cancel();
        }
        clientWorkers.clear();
        fieldGame = new FieldGame(40, 40, 2, 3);
    }
}