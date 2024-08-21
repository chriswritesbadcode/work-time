package components;

import java.awt.Component;
import javax.swing.JButton;

public class WTButton extends JButton {

    public WTButton(String text) {
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setFocusable(false);
    }
}
