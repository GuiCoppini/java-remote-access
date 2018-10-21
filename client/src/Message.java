import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message implements Serializable {

    private String command;

    private List<Serializable> arguments;
    private BufferedImage screenshot;

    public Message(String command, Serializable... arguments) {
        this.command = command;
        this.arguments = new ArrayList<>();
        Collections.addAll(this.arguments, arguments);
    }

    public Message(String command, BufferedImage image) {
        this.command = command;
        this.screenshot = image;
    }

    public BufferedImage getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(BufferedImage screenshot) {
        this.screenshot = screenshot;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<Serializable> getArguments() {
        return arguments;
    }

    public void setArguments(List<Serializable> arguments) {
        this.arguments = arguments;
    }
}
