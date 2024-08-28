package sections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import components.WTLabel;
import components.WTPanel;
import components.WTScrollPane;
import components.WTWindow;
import consts.Constants;
import utils.GeneralUtils;

public class UserReport {

        WTWindow userReportWindow = new WTWindow("", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true, false);
        WTPanel userReportPanel = new WTPanel("box");
        WTPanel contentPanel = new WTPanel("");
        WTScrollPane scrollPane = new WTScrollPane(userReportPanel);

        WTLabel userReportHeading = new WTLabel("", true, "lg", "b", 'c');
        WTLabel currMonthLabel = new WTLabel("", false, "sm", "bb", 'c');

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
                        Statement getUserBreaksSTMT = con.createStatement();
                        ResultSet userWorkTimesRS = getUserWorkTimesSTMT
                                        .executeQuery("SELECT start, end FROM work_times WHERE user_id = " + userId
                                                        + " ORDER BY start DESC");
                        ResultSet userBreaksRS = getUserBreaksSTMT
                                        .executeQuery("SELECT start, end, type FROM breaks WHERE user_id = " + userId
                                                        + " ORDER BY start DESC");

                        if (!userWorkTimesRS.isBeforeFirst()) {
                                errorLabel.setText("No records!");
                        } else {
                                String hmPattern = "HH:mm";
                                String dmyPattern = "dd/MM/yyyy";
                                DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(hmPattern);

                                while (userWorkTimesRS.next()) {

                                        String startTime = GeneralUtils.formatDate(hmPattern,
                                                        userWorkTimesRS.getLong(1));
                                        String endTime = GeneralUtils.formatDate(hmPattern,
                                                        userWorkTimesRS.getLong(2));

                                        CharSequence csStartTime = startTime;
                                        CharSequence csEndTime = endTime;

                                        Duration duration = Duration.between(
                                                        LocalTime.parse(csStartTime, dtFormatter),
                                                        LocalTime.parse(csEndTime, dtFormatter));

                                        long days = duration.toDays();
                                        long hours = duration.toHours() % 24;
                                        long minutes = duration.toMinutes() % 60;

                                        String durationStr = (days != 0 ? days + "d, " : "") + hours + "h, "
                                                        + minutes + "m";

                                        WTPanel workGroupPanel = new WTPanel("box");
                                        workGroupPanel.add(new WTLabel(
                                                        GeneralUtils.formatDate(dmyPattern, userWorkTimesRS.getLong(1)),
                                                        true, "sm", "b", 'c'));

                                        workGroupPanel.add(new WTLabel(
                                                        "Work: " + startTime + " to " + endTime, false, "md", "b",
                                                        'c'));

                                        workGroupPanel.add(
                                                        new WTLabel(durationStr, false, "sm", "b", 'c'));
                                        contentPanel.add(workGroupPanel);
                                }

                                contentPanel.add(new WTLabel("BREAKS", true, "lg", "b", 'c'));
                                contentPanel.add(new WTLabel("BREAKS", true, "lg", "b", 'c'));
                                contentPanel.add(new WTLabel("BREAKS", true, "lg", "b", 'c'));
                                contentPanel.add(new WTLabel("BREAKS", true, "lg", "b", 'c'));
                                contentPanel.add(new WTLabel("BREAKS", true, "lg", "b", 'c'));
                                contentPanel.add(new WTLabel("BREAKS", true, "lg", "b", 'c'));

                                while (userBreaksRS.next()) {
                                        String startTime = GeneralUtils.formatDate(hmPattern,
                                                        userBreaksRS.getLong(1));
                                        String endTime = GeneralUtils.formatDate(hmPattern,
                                                        userBreaksRS.getLong(2));

                                        CharSequence csStarTime = startTime;
                                        CharSequence csEndTime = endTime;

                                        Duration duration = Duration.between(LocalTime.parse(csStarTime, dtFormatter),
                                                        LocalTime.parse(csEndTime, dtFormatter));

                                        long days = duration.toDays();
                                        long hours = duration.toHours() % 24;
                                        long minutes = duration.toMinutes() % 60;

                                        String durationStr = (days != 0 ? days + "d, " : "") + hours + "h, " + minutes
                                                        + "m";

                                        WTPanel breakGroupPanel = new WTPanel("box");
                                        breakGroupPanel.add(new WTLabel(
                                                        GeneralUtils.formatDate(dmyPattern, userBreaksRS.getLong(1)),
                                                        true, "sm", "b", 'c'));

                                        breakGroupPanel.add(new WTLabel("Break: " + startTime + " to " + endTime, false,
                                                        "md", "b", 'c'));

                                        breakGroupPanel.add(new WTLabel(durationStr, false, "sm", "b", 'c'));

                                        contentPanel.add(breakGroupPanel);
                                }
                        }
                        con.close();
                } catch (Exception err) {
                        System.out.println("ERROR IN USER REPORT ACTION PERFORMED: " + err);
                }

                userReportPanel.add(errorLabel);
                userReportPanel.add(contentPanel);

                userReportWindow.add(scrollPane);

                userReportWindow.setVisible(true);
        }
}
