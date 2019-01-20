package main.utils;

import javafx.util.Pair;

public class ServerUtils {

    public static void startOnNewThread(Runnable r) {
        new Thread(r).start();
    }

    public static void printCommandList() {
        StringBuilder commandList = new StringBuilder();
        for (Pair<String, String> c : Command.availableCommands()) {
            commandList.append("$ " + c.getKey() + " - " + c.getValue() + "\n");
        }

        print(commandList.toString());
    }

    public static void print(Object printable) {
        System.out.println(printable.toString());
        System.out.println("-------------------------------------------------");
    }
}
