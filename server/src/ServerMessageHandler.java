import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

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
            InputStream in = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(in);

            window.add(new ImageScreen(bufferedImage));

            window.setLocationRelativeTo(null);
            window.pack();
            window.setVisible(true);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
