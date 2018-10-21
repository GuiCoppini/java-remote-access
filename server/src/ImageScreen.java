import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageScreen extends JPanel {

    private BufferedImage image;

    public ImageScreen(File file) {
        try {
            this.image = ImageIO.read(file);
        } catch (IOException ex) {
            // handle exception...
        }
    }

    public ImageScreen(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }

}