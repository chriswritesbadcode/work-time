package components;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;

public class WTLabel extends JLabel {

    public WTLabel(String text, boolean heading) {
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setFont(new Font("Verdana", (heading) ? Font.BOLD : Font.PLAIN, 20));
    }
}
