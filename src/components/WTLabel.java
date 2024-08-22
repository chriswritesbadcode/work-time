package components;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;

public class WTLabel extends JLabel {

    // public WTLabel(String text, boolean heading, String size) {
    public WTLabel(String text, boolean heading) {
        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        // this.setFont(new Font("Verdana", (heading) ? Font.BOLD : Font.PLAIN,
        // (size.equals("sm")) ? 10 : (size.equals("md")) ? 15 : (size.equals("lg")) ?
        // 20 : 5));
        this.setFont(new Font("Verdana", (heading) ? Font.BOLD : Font.PLAIN, 15));

    }
}
