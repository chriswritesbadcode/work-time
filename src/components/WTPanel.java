package components;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import utils.WrapLayout;

public class WTPanel extends JPanel {

    public WTPanel(String layout) {
        if (layout.equals("box")) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        } else if (layout.equals("grid")) {
            this.setLayout(new GridLayout());
        } else if (layout.equals("")) {
            this.setLayout(new WrapLayout(WrapLayout.CENTER, 15, 20));
        }
        this.setBackground(new Color(133, 173, 173));
    }
}
