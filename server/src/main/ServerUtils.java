package main;

public class ServerUtils {

    static void startOnNewThread(Runnable r) {
        new Thread(r).start();
    }
}
