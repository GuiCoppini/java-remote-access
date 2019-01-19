package main;

import java.io.*;

public class ResourcesUtils {
    public static File getFile(String fileName, String suffix) throws IOException {
        String fullName = fileName + "." + suffix;
        InputStream initialStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fullName);

        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        File targetFile = File.createTempFile(fileName, suffix);
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);

        return targetFile;
    }
}
