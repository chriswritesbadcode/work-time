package auth;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import consts.Constants;
import components.WTButton;
import components.WTLabel;
import components.WTPanel;
import components.WTPasswordField;
import components.WTSpacer;
import components.WTTextField;
import components.WTWindow;

public class LoginPage implements ActionListener {

    WTWindow loginWindow = new WTWindow(
            "Work Time Login", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true);

    WTPanel loginPanel = new WTPanel();

    WTLabel loginHeading = new WTLabel("Login to continue", true);

    WTLabel usernameLabel = new WTLabel("Username:", false);
    WTTextField usernameField = new WTTextField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);
    WTLabel passwordLabel = new WTLabel("Password:", false);
    WTPasswordField passwordField = new WTPasswordField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);

    WTSpacer spacer = new WTSpacer(new Dimension(Constants.SMALL_Y_SPACER_WIDTH, Constants.SMALL_Y_SPACER_HEIGHT));

    WTButton loginBtn = new WTButton("Login");
    WTButton toRegisterButton = new WTButton("Register a new account");

    public LoginPage() {
        loginPanel.add(loginHeading);

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        loginPanel.add(spacer);

        loginPanel.add(loginBtn);
        loginBtn.addActionListener(this);
        loginPanel.add(toRegisterButton);
        toRegisterButton.addActionListener(this);

        loginWindow.add(loginPanel);
        loginWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {

            System.out.println(usernameField.getText());
            System.out.println(passwordField.getPassword());
            new UserView();
        } else if (e.getSource() == toRegisterButton) {
            loginWindow.dispose();
            new RegisterPage();
        }
    }
}
