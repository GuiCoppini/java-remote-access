package main.utils;

public class ServerUtils {

    public static void startOnNewThread(Runnable r) {
        new Thread(r).start();
    }
}
