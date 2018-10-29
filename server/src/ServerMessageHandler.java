import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ServerMessageHandler {
    static void handleIncomingMessage(Message message, ClientConnection c) {

        switch(message.getCommand()) {
            case "screen":
                receiveScreenshot(message);
                break;
            case "print":
                System.out.println(message.getArguments().get(0));
                break;

        }

    }

    static void receiveScreenshot(Message message) {
        System.out.println("SCREENZAO CHEGOU HEIN");
        JFrame window = new JFrame("An Image On Screen");

        try {
            byte[] imageBytes = (byte[]) message.getArguments().get(0);
            int mouseX = (int) message.getArguments().get(1);
            int mouseY = (int) message.getArguments().get(2);


            InputStream in = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(in);

            ImageScreen image = new ImageScreen(bufferedImage);

            ClassLoader classLoader = ServerMessageHandler.class.getClassLoader();
            BufferedImage cursor = ImageIO.read(classLoader.getResourceAsStream("cursor.png"));

            Graphics2D graphics2D = image.getImage().createGraphics();
            graphics2D.drawImage(cursor, mouseX, mouseY, 16, 16, null);

            window.add(image);

            window.setLocationRelativeTo(null);
            window.pack();
            window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            window.setVisible(true);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
