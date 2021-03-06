package utils;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageScreen extends JPanel {

    public BufferedImage getImage() {
        return image;
    }

    private BufferedImage image;

    public ImageScreen(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

}