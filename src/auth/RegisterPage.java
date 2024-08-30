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
import components.WTOptionPane;
import components.WTPanel;
import components.WTPasswordField;
import components.WTSpacer;
import components.WTTextField;
import components.WTWindow;
import consts.Constants;
import state.AppState;
import utils.GeneralUtils;
import validate.InputValidator;
import validate.PasswordHashing;

public class RegisterPage implements ActionListener {

    WTWindow registerWindow = new WTWindow("Work Time Register", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true,
            true);
    WTPanel registerPanel = new WTPanel("box");

    WTLabel registerHeading = new WTLabel("Register a new account", true, "lg", "b", 'c');

    WTLabel fullNameLabel = new WTLabel("Name:", false, "sm", "b", 'c');
    WTTextField fullNameField = new WTTextField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);
    WTLabel userNameLabel = new WTLabel("Username:", false, "sm", "b", 'c');
    WTTextField userNameField = new WTTextField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);
    WTLabel passwordLabel = new WTLabel("Password:", false, "sm", "b", 'c');
    WTPasswordField passwordField = new WTPasswordField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);

    WTSpacer spacer = new WTSpacer(new Dimension(Constants.SMALL_Y_SPACER_WIDTH, Constants.SMALL_Y_SPACER_HEIGHT));

    WTButton registerButton = new WTButton("Register");
    WTButton toLoginButton = new WTButton("Login to an existing account");

    WTLabel errorLabel = new WTLabel("", false, "sm", "r", 'c');

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

        GeneralUtils.addListenerForEnter(registerButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AppState state = AppState.getInstance();

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
                                .prepareStatement(
                                        "INSERT INTO users(full_name, username, password) VALUES(?,?, ?)",
                                        PreparedStatement.RETURN_GENERATED_KEYS);
                        pstmt.setString(1, fullName);
                        pstmt.setString(2, userName);
                        pstmt.setString(3, hashedPassword);
                        pstmt.executeUpdate();

                        ResultSet newUserSet = pstmt.getGeneratedKeys();
                        if (newUserSet.next()) {
                            int generatedId = newUserSet.getInt(1);
                            state.setUserId(generatedId);
                        }

                        state.setFullName(fullName);
                        state.setUserName(userName);
                        state.setRole("agent");

                        registerWindow.dispose();
                        new UserView();
                    }
                    con.close();
                } catch (Exception err) {
                    WTOptionPane.showMessageBox("Error in register page: " + err);
                }
            }
        } else if (e.getSource() == toLoginButton) {
            registerWindow.dispose();
            new LoginPage();
        }
    }
}
