package utils;

import javax.swing.*;
import java.awt.*;

public class TextScreen extends JFrame {

    JTextField field = new JTextField();

    public TextScreen() {

        this.getContentPane().setLayout(new FlowLayout());

        field.setEditable(false);

        add(field);

        this.pack();

        this.setVisible(true);
    }

    public void append(String text) {
        this.field.setText(this.field.getText() + text);
        this.pack();
    }
}
