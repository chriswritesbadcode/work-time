package auth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import components.WTButton;
import components.WTLabel;
import components.WTPanel;
import components.WTWindow;
import consts.Constants;
import state.AppState;

public class UserView implements ActionListener {

    AppState state = AppState.getInstance();

    WTWindow userWindow = new WTWindow("Work Time", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true);
    WTPanel panel = new WTPanel();
    WTLabel userViewHeading = new WTLabel("Work Time", true, "lg", "b", 'c');

    WTLabel fullNameLabel = new WTLabel(state.getFullName(), false, "sm", "b", 'r');
    WTLabel userNameLabel = new WTLabel(state.getUserName(), false, "sm", "b", 'r');
    WTLabel userIdLabel = new WTLabel(state.getUserId(), false, "sm", "b", 'r');

    WTButton endOtherBreakBtn = new WTButton("Start Break");
    WTButton startOtherBreakBtn = new WTButton("End Break");
    WTButton endToiletBtn = new WTButton("End Toilet Break");
    WTButton startToiletBtn = new WTButton("Start Toilet Break");
    WTButton endLunchBtn = new WTButton("End Lunch");
    WTButton startLunchBtn = new WTButton("Start Lunch");
    WTButton endWorkBtn = new WTButton("End Work");
    WTButton startWorkBtn = new WTButton("Start Work");

    WTButton logoutBtn = new WTButton("Logout");

    public UserView() {

        panel.add(userViewHeading);

        panel.add(fullNameLabel);
        panel.add(userNameLabel);
        panel.add(userIdLabel);

        panel.add(startWorkBtn);
        panel.add(endWorkBtn);
        panel.add(startLunchBtn);
        panel.add(endLunchBtn);
        panel.add(startToiletBtn);
        panel.add(endToiletBtn);
        panel.add(startOtherBreakBtn);
        panel.add(endOtherBreakBtn);

        panel.add(logoutBtn);

        startWorkBtn.addActionListener(this);
        endWorkBtn.addActionListener(this);
        startLunchBtn.addActionListener(this);
        endLunchBtn.addActionListener(this);
        startToiletBtn.addActionListener(this);
        endToiletBtn.addActionListener(this);
        startOtherBreakBtn.addActionListener(this);
        endOtherBreakBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        userWindow.add(panel);
        userWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutBtn) {

            try {
                Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                        Constants.DB_PASSWORD);
                PreparedStatement logoutPSTMT = con.prepareStatement("DELETE FROM sessions WHERE user_id = ?");
                logoutPSTMT.setString(1, state.getUserId());
                logoutPSTMT.execute();

                SessionManager.invalidateSession();

            } catch (Exception err) {
                System.out.println("ERROR IN USER VIEW: " + err);
            }
            AppState.resetInstance();
            userWindow.dispose();
            new LoginPage();
        }
    }
}
