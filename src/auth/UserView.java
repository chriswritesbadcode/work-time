package auth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.swing.SwingUtilities;

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

    WTLabel fullNameLabel = new WTLabel(state.getFullName(), false, "sm", "b", 'c');
    WTLabel userNameLabel = new WTLabel(state.getUserName(), false, "sm", "b", 'c');
    WTLabel userIdLabel = new WTLabel(Integer.toString(state.getUserId()), false, "sm", "b", 'c');

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

        try {
            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);
            PreparedStatement isWorkingPSTMT = con
                    .prepareStatement("SELECT * FROM work_times WHERE end IS NULL AND user_id = ?");
            isWorkingPSTMT.setInt(1, state.getUserId());
            ResultSet workingData = isWorkingPSTMT.executeQuery();
            if (workingData.isBeforeFirst()) {
                state.setIsWorking(true);
            } else {
                state.setIsWorking(false);
            }

        } catch (Exception err) {
            System.out.println("ERROR IN USERVIEW BEGINNING: " + err);
        }

        panel.add(userViewHeading);

        panel.add(fullNameLabel);
        panel.add(userNameLabel);
        panel.add(userIdLabel);

        if (state.getIsWorking()) {
            panel.add(endWorkBtn);
        } else {
            panel.add(startWorkBtn);
        }
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
        try {

            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);

            if (e.getSource() == logoutBtn) {
                PreparedStatement logoutPSTMT = con.prepareStatement("DELETE FROM sessions WHERE user_id = ?");
                logoutPSTMT.setInt(1, state.getUserId());
                logoutPSTMT.execute();

                SessionManager.invalidateSession();

                AppState.resetInstance();
                userWindow.dispose();
                new LoginPage();
            } else if (e.getSource() == startWorkBtn) {
                PreparedStatement startWorkPSTMT = con
                        .prepareStatement("INSERT INTO work_times(user_id, start) VALUES(?, ?)");
                startWorkPSTMT.setInt(1, state.getUserId());
                startWorkPSTMT.setLong(2, new Date().getTime());
                startWorkPSTMT.execute();

                state.setIsWorking(true);

            } else if (e.getSource() == endWorkBtn) {
                PreparedStatement endWorkPSTMT = con
                        .prepareStatement("UPDATE work_times SET end = ? WHERE end IS NULL AND user_id = ?");
                endWorkPSTMT.setLong(1, new Date().getTime());
                endWorkPSTMT.setInt(2, state.getUserId());
                endWorkPSTMT.execute();

                state.setIsWorking(false);

            } else if (e.getSource() == startLunchBtn) {
                System.out.println("UNIMPLEMENTED");
            } else if (e.getSource() == endLunchBtn) {
                System.out.println("UNIMPLEMENTED");
            } else if (e.getSource() == startToiletBtn) {
                System.out.println("UNIMPLEMENTED");
            } else if (e.getSource() == endToiletBtn) {
                System.out.println("UNIMPLEMENTED");
            } else if (e.getSource() == startOtherBreakBtn) {
                System.out.println("UNIMPLEMENTED");
            } else if (e.getSource() == endOtherBreakBtn) {
                System.out.println("UNIMPLEMENTED");
            }

            updateUI();

            con.close();
        } catch (Exception err) {
            System.out.println("ERROR IN USER VIEW ACTION PERFORMED: " + err);
        }
    }

    private void updateUI() {
        SwingUtilities.invokeLater(() -> {
            panel.removeAll();
            panel.add(userViewHeading);

            panel.add(fullNameLabel);
            panel.add(userNameLabel);
            panel.add(userIdLabel);

            if (state.getIsWorking()) {
                System.out.println("IS WORKING ");
                panel.add(endWorkBtn);
            } else {
                System.out.println("NOT  WORKING ");
                panel.add(startWorkBtn);
            }
            panel.add(startLunchBtn);
            panel.add(endLunchBtn);
            panel.add(startToiletBtn);
            panel.add(endToiletBtn);
            panel.add(startOtherBreakBtn);
            panel.add(endOtherBreakBtn);

            panel.add(logoutBtn);

            panel.revalidate();
            panel.repaint();
        });
    }
}
