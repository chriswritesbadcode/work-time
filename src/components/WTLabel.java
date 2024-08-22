package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;

public class WTLabel extends JLabel {
    private Color fgColor;

    public WTLabel(String text, boolean heading, String size, String color) {

        switch (color) {
            case "r":
                fgColor = new Color(220, 0, 0);
                break;
            case "g":
                fgColor = new Color(0, 220, 0);
                break;
            case "bb":
                fgColor = new Color(0, 0, 220);
                break;
            default:
                fgColor = new Color(0, 0, 0);
                break;
        }

        this.setText(text);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setFont(new Font("Verdana", (heading) ? Font.BOLD : Font.PLAIN,
                (size.equals("sm")) ? 10 : (size.equals("md")) ? 15 : (size.equals("lg")) ? 20 : 50));
        this.setForeground(fgColor);
    }
}
