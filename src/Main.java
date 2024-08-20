
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    public static void main(String args[]) {
        JFrame window = new JFrame("Work Time");
        JPanel panel = new JPanel();
        JLabel text = new JLabel("Work Time");
        JButton button = new JButton();

        window.setSize(300, 300);
        window.setLocationRelativeTo(null);

        panel.add(button);
        panel.add(text);

        window.add(panel);

        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}
