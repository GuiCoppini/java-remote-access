package main.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
