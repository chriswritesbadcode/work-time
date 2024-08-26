package components;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class WTPanel extends JPanel {

    public WTPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(133, 173, 173));
    }
}
