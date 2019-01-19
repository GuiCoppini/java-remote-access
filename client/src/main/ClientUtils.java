package main;

public class ClientUtils {
    static void startOnNewThread(Runnable r) {
        new Thread(r).start();
    }
}
