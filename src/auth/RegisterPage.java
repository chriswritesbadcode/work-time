package auth;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import components.WTButton;
import components.WTLabel;
import components.WTPanel;
import components.WTPasswordField;
import components.WTSpacer;
import components.WTTextField;
import components.WTWindow;
import consts.Constants;

public class RegisterPage implements ActionListener {
    WTWindow registerWindow = new WTWindow("Work Time Register", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true);
    WTPanel registerPanel = new WTPanel();

    WTLabel registerHeading = new WTLabel("Register a new account", true, "lg", "b");

    WTLabel nameLabel = new WTLabel("Name:", false, "sm", "b");
    WTTextField nameField = new WTTextField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);
    WTLabel userNameLabel = new WTLabel("Username:", false, "sm", "b");
    WTTextField userNameField = new WTTextField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);
    WTLabel passwordLabel = new WTLabel("Password:", false, "sm", "b");
    WTPasswordField passwordField = new WTPasswordField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);

    WTSpacer spacer = new WTSpacer(new Dimension(Constants.SMALL_Y_SPACER_WIDTH, Constants.SMALL_Y_SPACER_HEIGHT));

    WTButton registerButton = new WTButton("Register");
    WTButton toLoginButton = new WTButton("Login to an existing account");

    public RegisterPage() {
        registerPanel.add(registerHeading);

        registerPanel.add(nameLabel);
        registerPanel.add(nameField);
        registerPanel.add(userNameLabel);
        registerPanel.add(userNameField);
        registerPanel.add(passwordLabel);
        registerPanel.add(passwordField);

        registerPanel.add(spacer);

        registerPanel.add(registerButton);
        registerButton.addActionListener(this);
        registerPanel.add(toLoginButton);
        toLoginButton.addActionListener(this);

        registerWindow.add(registerPanel);
        registerWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            System.out.println("Register clicked");
        } else if (e.getSource() == toLoginButton) {
            registerWindow.dispose();
            new LoginPage();
        }
    }
}
