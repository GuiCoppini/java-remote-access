import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ScreenUtils {

    public static boolean isSharing = false;

    public static void sendScreenshot(Connection connection, boolean screenShare) throws AWTException, IOException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenshot = new Robot().createScreenCapture(screenRect);

        int mouseX = MouseInfo.getPointerInfo().getLocation().x;
        int mouseY = MouseInfo.getPointerInfo().getLocation().y;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenshot, "png", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();

        String command = screenShare ? "screenshare" : "screen";
        connection.sendMessage(new Message(command, imageBytes, mouseX, mouseY));
    }

    public static void startScreenShare(Connection connection) {
        isSharing = true;
        new Thread(new ScreenSharer(connection)).start();
    }

    public static void stopScreenShare() {
        isSharing = false;
    }
}
