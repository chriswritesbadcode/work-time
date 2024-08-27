package auth;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import components.UserReport;
import components.WTButton;
import components.WTLabel;
import components.WTPanel;
import components.WTScrollPane;
import components.WTSpacer;
import components.WTWindow;
import consts.Constants;
import state.AppState;

public class AdminView implements ActionListener {
    AppState state = AppState.getInstance();

    WTWindow adminWindow = new WTWindow("Work Time - Admin", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true,
            true);
    WTPanel adminPanel = new WTPanel("box");
    WTScrollPane adminScrollPane = new WTScrollPane(adminPanel);
    WTPanel contentPanel = new WTPanel("");

    WTLabel adminHeading = new WTLabel("Admin Dashboard", true, "lg", "b", 'c');
    WTLabel errorLabel = new WTLabel("", false, "md", "b", 'c');

    WTButton logoutButton = new WTButton("Logout");

    public AdminView() {
        adminPanel.add(adminHeading);

        try {

            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);
            Statement getUserSTMT = con.createStatement();
            ResultSet usersRS = getUserSTMT.executeQuery("SELECT * FROM users WHERE role = 'agent'");
            if (!usersRS.isBeforeFirst()) {
                errorLabel.setText("No users");
            } else {
                while (usersRS.next()) {
                    WTPanel singleUserContainer = new WTPanel("box");

                    singleUserContainer.add(new WTLabel(usersRS.getString(4), false, "sm", "b", 'c'));
                    WTButton userBtn = new WTButton("Details");
                    userBtn.setActionCommand(usersRS.getString(1) + ":" + usersRS.getString(4)); // GET ID AS STRING ->
                                                                                                 // FN EXPECTS STRING
                    userBtn.addActionListener(this);
                    singleUserContainer.add(userBtn);

                    contentPanel.add(singleUserContainer);

                }
            }

        } catch (Exception err) {
            System.out.println("ERROR IN ADMINVIEW GET USERS: " + err);
        }

        adminPanel.add(contentPanel);
        adminPanel.add(errorLabel);
        // SPACERS
        for (int i = 0; i < 5; i++) {
            adminPanel
                    .add(new WTSpacer(new Dimension(Constants.LARGE_Y_SPACER_WIDTH, Constants.LARGE_Y_SPACER_HEIGHT)));
        }
        adminPanel.add(logoutButton);
        logoutButton.addActionListener(this);
        adminWindow.add(adminScrollPane);
        adminWindow.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String userData = e.getActionCommand(); // GET AS STRING COS ITS STORED AS STRING
            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);

            if (e.getSource() == logoutButton) {
                PreparedStatement logoutPSTMT = con.prepareStatement("DELETE FROM sessions WHERE user_id = ?");
                logoutPSTMT.setInt(1, state.getUserId());
                logoutPSTMT.executeUpdate();

                SessionManager.invalidateSession();

                AppState.resetInstance();
                adminWindow.dispose();
                new LoginPage();
            } else {
                if (!userData.isEmpty()) {
                    new UserReport(userData);
                }
            }
        } catch (Exception err) {
            System.out.println("ERROR IN ADMIN VIEW - ACTION PERFORMED OVERRIDE: " + err);
        }

    }
}
