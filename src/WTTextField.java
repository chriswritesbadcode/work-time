
import java.awt.Dimension;

import javax.swing.JTextField;

public class WTTextField extends JTextField {

    WTTextField(int w, int h) {
        this.setMaximumSize(new Dimension(w, h));
    }
}
