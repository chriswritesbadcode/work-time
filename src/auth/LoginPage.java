package auth;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

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

    WTLabel loginHeading = new WTLabel("Login to continue", true, "lg", "b");

    WTLabel usernameLabel = new WTLabel("Username:", false, "sm", "b");
    WTTextField usernameField = new WTTextField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);
    WTLabel passwordLabel = new WTLabel("Password:", false, "sm", "b");
    WTPasswordField passwordField = new WTPasswordField(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT);

    WTSpacer spacer = new WTSpacer(new Dimension(Constants.SMALL_Y_SPACER_WIDTH, Constants.SMALL_Y_SPACER_HEIGHT));

    WTButton loginBtn = new WTButton("Login");
    WTButton toRegisterButton = new WTButton("Register a new account");

    WTLabel errorLabel = new WTLabel("", false, "md", "r");

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

        loginPanel.add(errorLabel);

        loginWindow.add(loginPanel);
        loginWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.length() == 0 || password.length() == 0) {
                errorLabel.setText("Username and Password are required!");
            } else {
                System.out.println("TRYING TO CONNECT");
                try {
                    Connection con = DriverManager.getConnection(
                            "jdbc:mysql://" + System.getenv("WTDB_HOST") + "/" + System.getenv("WTDB_NAME"),
                            System.getenv("WTDB_USER"), System.getenv("WTDB_PASSWORD"));
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = " + "'" + username + "'");
                    if (!rs.isBeforeFirst()) {
                        errorLabel.setText("Username does not exist!");
                        System.out.println("Username does not exist!");
                    } else {
                        while (rs.next()) {
                            if (username.equals(rs.getString(2)) && password.equals(rs.getString(3))) {
                                System.out.println("Correct credentials");
                                new UserView();
                            } else {
                                errorLabel.setText("Incorrect credentials");
                                System.out.println("INCORRECT CREDS");
                            }
                        }
                    }

                    con.close();
                } catch (Exception err) {
                    System.out.println(err);
                }

            }

        } else if (e.getSource() == toRegisterButton) {
            loginWindow.dispose();
            new RegisterPage();
        }
    }
}
