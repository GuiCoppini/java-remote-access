package utils;

import java.util.Map;

public class ServerUtils {

    public static void startOnNewThread(Runnable r) {
        new Thread(r).start();
    }

    public static void printCommandList() {
        StringBuilder commandList = new StringBuilder();
        for (Map.Entry<String, String> entry : Command.availableCommands().entrySet()) {
            commandList.append("$ " + entry.getKey() + " - " + entry.getValue() + "\n");
        }

        print(commandList.toString());
    }

    public static void print(Object printable) {
        System.out.println(printable.toString());
        System.out.println(ConsoleColors.GREEN + "-------------------------------------------------" + ConsoleColors.RESET);
    }
}
