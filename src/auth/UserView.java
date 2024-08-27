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

    WTWindow userWindow = new WTWindow("Work Time", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true, true);
    WTPanel mainPanel = new WTPanel("box");
    WTPanel contentPanel = new WTPanel("");

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

        // DISABLE OR ENABLE BUTTONS
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

                    disableButtons("toilet,other,work");
                } else if (state.getBreakType().equals("toilet")) {
                    contentPanel.add(startLunchBtn);
                    contentPanel.add(endToiletBtn);
                    contentPanel.add(startOtherBreakBtn);

                    disableButtons("lunch,other,work");
                } else if (state.getBreakType().equals("other")) {
                    contentPanel.add(startLunchBtn);
                    contentPanel.add(startToiletBtn);
                    contentPanel.add(endOtherBreakBtn);

                    disableButtons("toilet,lunch,work");
                }
            } else {
                contentPanel.add(startLunchBtn);
                contentPanel.add(startToiletBtn);
                contentPanel.add(startOtherBreakBtn);
            }
        } else {
            contentPanel.add(startWorkBtn);

            contentPanel.add(startLunchBtn);
            contentPanel.add(startToiletBtn);
            contentPanel.add(startOtherBreakBtn);

            disableButtons("lunch,toilet,other");
            logoutBtn.setEnabled(true);

        }

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
                PreparedStatement logoutPSTMT = con.prepareStatement("DELETE FROM sessions WHERE user_id = ?");
                logoutPSTMT.setInt(1, state.getUserId());
                logoutPSTMT.executeUpdate();

                SessionManager.invalidateSession();

                AppState.resetInstance();
                userWindow.dispose();
                new LoginPage();
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
                startBreak();
            } else if (e.getSource() == endLunchBtn) {
                endBreak();
                resetBreak();

            } else if (e.getSource() == startToiletBtn) {
                state.setIsOnBreak(true);
                state.setBreakType("toilet");
                startBreak();
            } else if (e.getSource() == endToiletBtn) {
                endBreak();
                resetBreak();

            } else if (e.getSource() == startOtherBreakBtn) {
                state.setIsOnBreak(true);
                state.setBreakType("other");
                startBreak();
            } else if (e.getSource() == endOtherBreakBtn) {
                endBreak();
                resetBreak();
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

                        disableButtons("toilet,other,work");
                    } else if (state.getBreakType().equals("toilet")) {
                        contentPanel.add(startLunchBtn);
                        contentPanel.add(endToiletBtn);
                        contentPanel.add(startOtherBreakBtn);

                        disableButtons("lunch,other,work");
                    } else if (state.getBreakType().equals("other")) {
                        contentPanel.add(startLunchBtn);
                        contentPanel.add(startToiletBtn);
                        contentPanel.add(endOtherBreakBtn);

                        disableButtons("toilet,lunch,work");
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

                disableButtons("lunch,toilet,other");
                logoutBtn.setEnabled(true);

            }

            contentPanel.repaint();
            contentPanel.revalidate();
        });
    }

    private void resetBreak() {
        state.setIsOnBreak(false);
        state.setBreakType("");
    }

    private void disableButtons(String csv) {
        String[] btnTypes = csv.split(",");

        for (String btnType : btnTypes) {
            if (btnType.equals("lunch")) {
                startLunchBtn.setEnabled(false);
            } else if (btnType.equals("toilet")) {
                startToiletBtn.setEnabled(false);
            } else if (btnType.equals("other")) {
                startOtherBreakBtn.setEnabled(false);
            } else if (btnType.equals("work")) {
                endWorkBtn.setEnabled(false);
            }
        }

    }

    private void startBreak() {
        try {
            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);
            PreparedStatement startBreakPSTMT = con
                    .prepareStatement("INSERT INTO breaks(user_id, start, type) VALUES(?, ?, ?)");
            startBreakPSTMT.setInt(1, state.getUserId());
            startBreakPSTMT.setLong(2, new Date().getTime());
            startBreakPSTMT.setString(3, state.getBreakType());
            startBreakPSTMT.execute();

            con.close();
        } catch (Exception err) {
            System.out.println("ERROR IN USER VIEW START BREAK: " + err);
        }
    }

    private void endBreak() {
        try {
            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);
            PreparedStatement endBreakPSTMT = con
                    .prepareStatement("UPDATE breaks SET end = ? WHERE end IS NULL AND user_id = ?");
            endBreakPSTMT.setLong(1, new Date().getTime());
            endBreakPSTMT.setInt(2, state.getUserId());

            endBreakPSTMT.executeUpdate();

            System.out.println("ENDED BREAK");

            con.close();
        } catch (Exception err) {
            System.out.println("ERROR IN USER VIEW END BREAK: " + err);
        }
    }
}
