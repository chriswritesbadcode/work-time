package components;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class WTWindow extends JFrame {

    public WTWindow(String title, int w, int h, boolean centered, boolean closeAllOnExit) {
        this.setTitle(title);
        this.setSize(w, h);
        if (centered) {
            this.setLocationRelativeTo(null);
        }
        // this.setResizable(false);
        this.setDefaultCloseOperation(closeAllOnExit ? WTWindow.EXIT_ON_CLOSE : WTWindow.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon("src/resources/favico.png");
        this.setIconImage(icon.getImage());
    }
}
