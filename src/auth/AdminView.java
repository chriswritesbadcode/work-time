package auth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import components.UserReport;
import components.WTButton;
import components.WTLabel;
import components.WTPanel;
import components.WTWindow;
import consts.Constants;
import state.AppState;

public class AdminView implements ActionListener {
    AppState state = AppState.getInstance();

    WTWindow adminWindow = new WTWindow("Work Time - Admin", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true,
            true);

    WTPanel adminPanel = new WTPanel();

    WTLabel adminHeading = new WTLabel("Admin Dashboard", true, "lg", "b", 'c');
    WTLabel errorLabel = new WTLabel("", false, "md", "b", 'c');

    public AdminView() {
        adminPanel.add(adminHeading);

        try {

            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);
            Statement getUserSTMT = con.createStatement();
            ResultSet usersRS = getUserSTMT.executeQuery("SELECT * FROM users WHERE role != 'sagent'");
            if (!usersRS.isBeforeFirst()) {
                errorLabel.setText("No users");
            } else {
                while (usersRS.next()) {
                    adminPanel.add(new WTLabel(usersRS.getString(4), false, "sm", "b", 'c'));
                    WTButton userBtn = new WTButton("Details");
                    userBtn.setActionCommand(usersRS.getString(1) + ":" + usersRS.getString(4)); // GET ID AS STRING ->
                                                                                                 // FN EXPECTS STRING
                    userBtn.addActionListener(this);
                    adminPanel.add(userBtn);
                }
            }

        } catch (Exception err) {
            System.out.println("ERROR IN ADMINVIEW GET USERS: " + err);
        }

        adminPanel.add(errorLabel);

        adminWindow.add(adminPanel);
        adminWindow.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userData = e.getActionCommand(); // GET AS STRING COS ITS STORED AS STRING

        if (!userData.isEmpty()) {
            new UserReport(userData);
        }
    }
}
