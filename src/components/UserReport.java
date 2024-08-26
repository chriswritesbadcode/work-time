package components;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;

import consts.Constants;

public class UserReport {

    WTWindow userReportWindow = new WTWindow("", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true, false);
    WTPanel userReportPanel = new WTPanel();

    WTLabel userReportHeading = new WTLabel("", true, "lg", "b", 'c');
    WTLabel errorLabel = new WTLabel("", false, "md", "b", 'c');

    public UserReport(String userData) {
        String[] parts = userData.split(":");

        int userId = Integer.parseInt(parts[0]);
        String fullName = parts[1];

        userReportWindow.setTitle("User report - " + fullName);
        userReportHeading.setText(fullName + "'s report");

        userReportPanel.add(userReportHeading);

        try {

            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);

            Statement getUserWorkTimesSTMT = con.createStatement();
            ResultSet userWorkTimesRS = getUserWorkTimesSTMT
                    .executeQuery(
                            "SELECT a.start, a.end FROM work_times as a JOIN users as b ON a.user_id = b.id WHERE user_id = "
                                    + userId);

            if (!userWorkTimesRS.isBeforeFirst()) {
                errorLabel.setText("No records for: " + fullName + "!"); // GET FULL NAME
            } else {
                while (userWorkTimesRS.next()) {
                    long startTime = userWorkTimesRS.getLong(1);
                    long endTime = userWorkTimesRS.getLong(2);

                    userReportPanel.add(new WTLabel("Start: " + new Date(startTime) + " " + new Time(startTime), false,
                            "sm", "b", 'c'));
                    userReportPanel.add(
                            new WTLabel("End: " + new Date(endTime) + " " + new Time(endTime), false, "sm", "b",
                                    'c'));

                }
            }

        } catch (Exception err) {
            System.out.println("ERROR IN USER REPORT ACTION PERFORMED: " + err);
        }

        userReportPanel.add(errorLabel);

        userReportWindow.add(userReportPanel);
        userReportWindow.setVisible(true);
    }
}
