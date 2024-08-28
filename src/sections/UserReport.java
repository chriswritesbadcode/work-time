package sections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import components.WTLabel;
import components.WTPanel;
import components.WTScrollPane;
import components.WTWindow;
import consts.Constants;

public class UserReport {

        WTWindow userReportWindow = new WTWindow("", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true, false);
        WTPanel userReportPanel = new WTPanel("box");
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
                        ResultSet userWorkTimesRS = getUserWorkTimesSTMT
                                        .executeQuery(
                                                        "SELECT a.start, a.end FROM work_times as a JOIN users as b ON a.user_id = b.id WHERE user_id = "
                                                                        + userId + " ORDER BY a.start DESC");

                        if (!userWorkTimesRS.isBeforeFirst()) {
                                errorLabel.setText("No records!"); // GET FULL NAME
                        } else {
                                while (userWorkTimesRS.next()) {
                                        SimpleDateFormat sFormatter = new SimpleDateFormat("EEEE dd/MM/yyyy HH:mm:ss");
                                        DateTimeFormatter dtFormatter = DateTimeFormatter
                                                        .ofPattern("EEEE dd/MM/yyyy HH:mm:ss");

                                        String startTime = sFormatter.format(userWorkTimesRS.getLong(1));
                                        String endTime = sFormatter.format(userWorkTimesRS.getLong(2));

                                        CharSequence chst = startTime;
                                        CharSequence chet = endTime;

                                        Duration duration = Duration.between(LocalDateTime.parse(chst, dtFormatter),
                                                        LocalDateTime.parse(chet, dtFormatter));

                                        long days = duration.toDays();
                                        long hours = duration.toHours() % 24;
                                        long minutes = duration.toMinutes() % 60;
                                        long seconds = duration.toSeconds() % 60;

                                        String durationStr = (days != 0 ? days + " days, " : "") + hours + " hours, "
                                                        + minutes + " minutes, "
                                                        + seconds + " seconds.";

                                        userReportPanel.add(new WTLabel(
                                                        "Start: " + startTime, false, "md", "b", 'c'));
                                        userReportPanel.add(
                                                        new WTLabel("End: " + endTime, false, "md", "b", 'c'));

                                        userReportPanel.add(
                                                        new WTLabel(durationStr, false, "sm", "b", 'c'));
                                        userReportPanel.add(
                                                        new WTLabel(" ", false, "sm", "b", 'c'));

                                }
                        }
                        con.close();
                } catch (Exception err) {
                        System.out.println("ERROR IN USER REPORT ACTION PERFORMED: " + err);
                }

                userReportPanel.add(errorLabel);

                userReportWindow.add(scrollPane);

                userReportWindow.setVisible(true);
        }
}
