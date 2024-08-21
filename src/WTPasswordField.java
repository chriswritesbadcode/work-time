import java.awt.Dimension;

import javax.swing.JPasswordField;

public class WTPasswordField extends JPasswordField {
    WTPasswordField(int w, int h) {
        this.setMaximumSize(new Dimension(w, h));
    }
}
