package main.java.utils;

import main.java.network.Connection;
import main.java.network.Message;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ScreenUtils {

    static boolean isSharing = false;

    public static void sendScreenshot(Connection connection, boolean screenShare) {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenshot = null;
        try {
            screenshot = new Robot().createScreenCapture(screenRect);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        int mouseX = MouseInfo.getPointerInfo().getLocation().x;
        int mouseY = MouseInfo.getPointerInfo().getLocation().y;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = null;
        try {
            ImageIO.write(screenshot, "jpg", baos);
            baos.flush();
            imageBytes = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String command = screenShare ? "screenshare" : "screen";
        connection.sendMessage(new Message(command, imageBytes, mouseX, mouseY));
    }

    public static void startScreenShare(Connection connection) {
        isSharing = true;
        ClientUtils.startOnNewThread(new ScreenSharer(connection));
    }

    public static void stopScreenShare() {
        isSharing = false;
    }
}
