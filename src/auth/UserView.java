package auth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import components.WTButton;
import components.WTLabel;
import components.WTPanel;
import components.WTWindow;
import consts.Constants;
import state.AppState;
import utils.GeneralUtils;
import utils.UserViewUtils;

public class UserView implements ActionListener {

    AppState state = AppState.getInstance();

    WTWindow userWindow = new WTWindow("", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true, true);
    WTPanel mainPanel = new WTPanel("box");
    WTPanel contentPanel = new WTPanel("");
    WTPanel statusPanel = new WTPanel("box");

    WTLabel userViewHeading = new WTLabel("Work Time", true, "lg", "b", 'c');
    WTLabel fullNameLabel = new WTLabel(state.getFullName(), false, "sm", "b", 'c');

    WTButton endWorkBtn = new WTButton("End Work");
    WTButton startWorkBtn = new WTButton("Start Work");
    WTButton endLunchBtn = new WTButton("End Lunch");
    WTButton startLunchBtn = new WTButton("Start Lunch");
    WTButton endToiletBtn = new WTButton("End Toilet Break");
    WTButton startToiletBtn = new WTButton("Start Toilet Break");
    WTButton endOtherBreakBtn = new WTButton("End Break");
    WTButton startOtherBreakBtn = new WTButton("Start Break");

    WTLabel workStatusLabel = new WTLabel("Not working", false, "sm", "bb", 'c');
    WTLabel breakStatusLabel = new WTLabel("Not on break", false, "sm", "bb", 'c');

    WTButton logoutBtn = new WTButton("Logout");

    public UserView() {
        userWindow.setTitle("Work Time - " + state.getFullName());
        try {
            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);
            PreparedStatement isWorkingPSTMT = con
                    .prepareStatement("SELECT * FROM work_times WHERE end IS NULL AND user_id = ?");
            isWorkingPSTMT.setInt(1, state.getUserId());
            ResultSet workingData = isWorkingPSTMT.executeQuery();
            if (workingData.isBeforeFirst()) {
                state.setIsWorking(true);
                workingData.next();

            } else {
                state.setIsWorking(false);
            }

            PreparedStatement isOnBreakPSTMT = con
                    .prepareStatement("SELECT * FROM breaks WHERE end IS NULL AND user_id = ?");
            isOnBreakPSTMT.setInt(1, state.getUserId());
            ResultSet breakData = isOnBreakPSTMT.executeQuery();
            if (breakData.isBeforeFirst()) {
                state.setIsOnBreak(true);
                breakData.next();
                state.setBreakType(breakData.getString(5));
            } else {
                state.setIsOnBreak(false);
                state.setBreakType("");
            }

            con.close();
        } catch (Exception err) {
            System.out.println("ERROR IN USERVIEW BEGINNING: " + err);
        }

        mainPanel.add(userViewHeading);
        mainPanel.add(fullNameLabel);

        updateUI();

        mainPanel.add(contentPanel);
        mainPanel.add(logoutBtn);

        startWorkBtn.addActionListener(this);
        endWorkBtn.addActionListener(this);
        startLunchBtn.addActionListener(this);
        endLunchBtn.addActionListener(this);
        startToiletBtn.addActionListener(this);
        endToiletBtn.addActionListener(this);
        startOtherBreakBtn.addActionListener(this);
        endOtherBreakBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        userWindow.add(mainPanel);
        userWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);

            if (e.getSource() == logoutBtn) {
                int dialogResponse = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?",
                        "Confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (dialogResponse == JOptionPane.OK_OPTION) {
                    PreparedStatement logoutPSTMT = con.prepareStatement("DELETE FROM sessions WHERE user_id = ?");
                    logoutPSTMT.setInt(1, state.getUserId());
                    logoutPSTMT.executeUpdate();

                    SessionManager.invalidateSession();

                    AppState.resetInstance();
                    userWindow.dispose();
                    new LoginPage();
                }
            } else if (e.getSource() == startWorkBtn) {
                PreparedStatement startWorkPSTMT = con
                        .prepareStatement("INSERT INTO work_times(user_id, start) VALUES(?, ?)");
                startWorkPSTMT.setInt(1, state.getUserId());
                startWorkPSTMT.setLong(2, new Date().getTime());
                startWorkPSTMT.executeUpdate();

                state.setIsWorking(true);

            } else if (e.getSource() == endWorkBtn) {
                PreparedStatement endWorkPSTMT = con
                        .prepareStatement("UPDATE work_times SET end = ? WHERE end IS NULL AND user_id = ?");
                endWorkPSTMT.setLong(1, new Date().getTime());
                endWorkPSTMT.setInt(2, state.getUserId());
                endWorkPSTMT.executeUpdate();

                state.setIsWorking(false);

            } else if (e.getSource() == startLunchBtn) {
                state.setIsOnBreak(true);
                state.setBreakType("lunch");
                UserViewUtils.startBreak();
            } else if (e.getSource() == endLunchBtn) {
                UserViewUtils.endBreak();
                UserViewUtils.resetBreak();

            } else if (e.getSource() == startToiletBtn) {
                state.setIsOnBreak(true);
                state.setBreakType("toilet");
                UserViewUtils.startBreak();
            } else if (e.getSource() == endToiletBtn) {
                UserViewUtils.endBreak();
                UserViewUtils.resetBreak();

            } else if (e.getSource() == startOtherBreakBtn) {
                state.setIsOnBreak(true);
                state.setBreakType("other");
                UserViewUtils.startBreak();
            } else if (e.getSource() == endOtherBreakBtn) {
                UserViewUtils.endBreak();
                UserViewUtils.resetBreak();
            }

            con.close();
        } catch (Exception err) {
            System.out.println("ERROR IN USER VIEW ACTION PERFORMED: " + err);
        }
        updateUI();
    }

    private void updateUI() {
        SwingUtilities.invokeLater(() -> {
            contentPanel.removeAll();

            try {

                Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                        Constants.DB_PASSWORD);

                Statement isWorkingSTMT = con.createStatement();
                Statement isOnBreakSTMT = con.createStatement();

                ResultSet isWorkingData = isWorkingSTMT
                        .executeQuery("SELECT * FROM work_times WHERE end IS NULL AND user_id = " + state.getUserId());

                ResultSet isOnBreakData = isOnBreakSTMT
                        .executeQuery("SELECT * FROM breaks WHERE end IS NULL AND user_id = " + state.getUserId());

                if (isWorkingData.isBeforeFirst()) {
                    isWorkingData.next();

                    workStatusLabel
                            .setText("Working since " + GeneralUtils.formatDate("HH:mm", isWorkingData.getLong(3)));
                } else {
                    workStatusLabel.setText("Not working");
                }
                if (isOnBreakData.isBeforeFirst()) {
                    isOnBreakData.next();

                    breakStatusLabel.setText("On " + isOnBreakData.getString(5) + " break since "
                            + GeneralUtils.formatDate("HH:mm", isOnBreakData.getLong(3)));
                } else {
                    breakStatusLabel.setText("Not on break");
                }

                con.close();
            } catch (Exception err) {
                System.out.println("ERROR IN USER VIEW UPDATE UI: " + err);
            }

            if (state.getIsWorking()) {
                contentPanel.add(endWorkBtn);

                startLunchBtn.setEnabled(true);
                startToiletBtn.setEnabled(true);
                startOtherBreakBtn.setEnabled(true);
                logoutBtn.setEnabled(false);

                if (state.getIsOnBreak()) {
                    if (state.getBreakType().equals("lunch")) {
                        contentPanel.add(endLunchBtn);

                        contentPanel.add(startToiletBtn);
                        contentPanel.add(startOtherBreakBtn);

                        UserViewUtils.disableButtons("toilet,other,work", startLunchBtn, startToiletBtn,
                                startOtherBreakBtn, endWorkBtn);
                    } else if (state.getBreakType().equals("toilet")) {
                        contentPanel.add(startLunchBtn);
                        contentPanel.add(endToiletBtn);
                        contentPanel.add(startOtherBreakBtn);

                        UserViewUtils.disableButtons("lunch,other,work", startLunchBtn, startToiletBtn,
                                startOtherBreakBtn,
                                endWorkBtn);
                    } else if (state.getBreakType().equals("other")) {
                        contentPanel.add(startLunchBtn);
                        contentPanel.add(startToiletBtn);
                        contentPanel.add(endOtherBreakBtn);

                        UserViewUtils.disableButtons("lunch,toilet,other", startLunchBtn, startToiletBtn,
                                startOtherBreakBtn, endWorkBtn);

                        UserViewUtils.disableButtons("toilet,lunch,work", startLunchBtn, startToiletBtn,
                                startOtherBreakBtn, endWorkBtn);
                    }
                } else {
                    contentPanel.add(startLunchBtn);
                    contentPanel.add(startToiletBtn);
                    contentPanel.add(startOtherBreakBtn);

                    endWorkBtn.setEnabled(true);

                }
            } else {
                contentPanel.add(startWorkBtn);

                contentPanel.add(startLunchBtn);
                contentPanel.add(startToiletBtn);
                contentPanel.add(startOtherBreakBtn);

                UserViewUtils.disableButtons("lunch,toilet,other", startLunchBtn, startToiletBtn, startOtherBreakBtn,
                        endWorkBtn);
                logoutBtn.setEnabled(true);
            }

            statusPanel.add(workStatusLabel);
            statusPanel.add(breakStatusLabel);

            contentPanel.add(statusPanel);
            contentPanel.repaint();
            contentPanel.revalidate();
        });
    }

}
