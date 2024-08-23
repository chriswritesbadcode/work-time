package auth;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import components.WTButton;
import components.WTLabel;
import components.WTPanel;
import components.WTPasswordField;
import components.WTSpacer;
import components.WTTextField;
import components.WTWindow;
import consts.Constants;
import validate.InputValidator;
import validate.PasswordHashing;

public class RegisterPage implements ActionListener {
    WTWindow registerWindow = new WTWindow("Work Time Register", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true);
    WTPanel registerPanel = new WTPanel();

    WTLabel registerHeading = new WTLabel("Register a new account", true, "lg", "b");

    WTLabel fullNameLabel = new WTLabel("Name:", false, "sm", "b");
    WTTextField fullNameField = new WTTextField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);
    WTLabel userNameLabel = new WTLabel("Username:", false, "sm", "b");
    WTTextField userNameField = new WTTextField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);
    WTLabel passwordLabel = new WTLabel("Password:", false, "sm", "b");
    WTPasswordField passwordField = new WTPasswordField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);

    WTSpacer spacer = new WTSpacer(new Dimension(Constants.SMALL_Y_SPACER_WIDTH, Constants.SMALL_Y_SPACER_HEIGHT));

    WTButton registerButton = new WTButton("Register");
    WTButton toLoginButton = new WTButton("Login to an existing account");

    WTLabel errorLabel = new WTLabel("", false, "sm", "r");

    public RegisterPage() {
        registerPanel.add(registerHeading);

        registerPanel.add(fullNameLabel);
        registerPanel.add(fullNameField);
        registerPanel.add(userNameLabel);
        registerPanel.add(userNameField);
        registerPanel.add(passwordLabel);
        registerPanel.add(passwordField);

        registerPanel.add(spacer);

        registerPanel.add(registerButton);
        registerButton.addActionListener(this);
        registerPanel.add(toLoginButton);
        toLoginButton.addActionListener(this);

        registerPanel.add(errorLabel);

        registerWindow.add(registerPanel);
        registerWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String fullName = fullNameField.getText();
            String userName = userNameField.getText();
            String password = new String(passwordField.getPassword());
            String invalidMsg = "Invalid format at ";

            if (!InputValidator.validateFullName(fullName)) {
                errorLabel.setText(invalidMsg + "Full Name, 2 to 50 characters!");
            } else if (!InputValidator.validateUserName(userName)) {
                errorLabel.setText(invalidMsg + "Username, 4 to 20 characters, lowercase!");
            } else if (!InputValidator.validatePasword(password)) {
                errorLabel.setText(invalidMsg + "Password, 4 digits only!");
            } else {
                try {
                    Connection con = DriverManager.getConnection(Constants.DB_HOST,
                            Constants.DB_USER, Constants.DB_PASSWORD);

                    Statement doesUsernameExistStmt = con.createStatement();
                    ResultSet usernameSearchSet = doesUsernameExistStmt
                            .executeQuery("SELECT * FROM users WHERE username = " + "'" + userName + "'");
                    if (usernameSearchSet.isBeforeFirst()) {
                        errorLabel.setText("Username exists!");
                    } else {
                        byte[] salt = PasswordHashing.generateSalt();
                        String hashedPassword = PasswordHashing.hashPassword(password, salt);

                        PreparedStatement pstmt = con
                                .prepareStatement("INSERT INTO users(full_name, username, password) VALUES(?,?, ?)");
                        pstmt.setString(1, fullName);
                        pstmt.setString(2, userName);
                        pstmt.setString(3, hashedPassword);
                        pstmt.execute();

                        registerWindow.dispose();
                        new UserView();
                    }
                    con.close();
                } catch (Exception err) {
                    System.out.println(err);
                }
            }
        } else if (e.getSource() == toLoginButton) {
            registerWindow.dispose();
            new LoginPage();
        }
    }
}
