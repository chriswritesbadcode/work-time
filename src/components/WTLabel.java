package components;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;

public class WTLabel extends JLabel {

    public WTLabel(String text) {
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setFont(new Font("Serif", Font.BOLD, 20));
    }
}
