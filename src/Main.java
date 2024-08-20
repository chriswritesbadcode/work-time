
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {

    public static void main(String args[]) {
        JFrame window = new JFrame("Work Time");
        WTPanel panel = new WTPanel();
        JLabel text = new JLabel("Work Time");
        WTButton button = new WTButton("Start Work");

        window.setSize(600, 300);
        window.setLocationRelativeTo(null);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(text);
        panel.add(button);

        window.add(panel);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
    }
}
