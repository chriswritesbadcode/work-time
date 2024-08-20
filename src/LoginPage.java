
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage implements ActionListener {

    WTWindow loginWindow = new WTWindow(
            "Work Time Login", 600, 300, true);
    WTPanel loginPanel = new WTPanel();

    WTButton loginBtn = new WTButton("Login");
    WTLabel loginHeader = new WTLabel("Please login to continue");

    LoginPage() {
        loginPanel.add(loginHeader);

        loginBtn.addActionListener(this);
        loginPanel.add(loginBtn);

        loginWindow.add(loginPanel);
        loginWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            new UserView();
        }
    }
}
