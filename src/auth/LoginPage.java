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

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + System.getenv("WTDB_HOST") + "/" +
                            System.getenv("WTDB_NAME"),
                    System.getenv("WTDB_USER"),
                    System.getenv("WTDB_PASSWORD"));
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.length() == 0 || password.length() == 0) {
                errorLabel.setText("Username and Password are required!");
            } else {
                if ("Chris".equals(username) && "123".equals(password)) {
                    new UserView();

                } else {
                    errorLabel.setText("Incorrect credentials");
                }
            }

        } else if (e.getSource() == toRegisterButton) {
            loginWindow.dispose();
            new RegisterPage();
        }
    }
}
