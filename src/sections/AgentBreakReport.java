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

public class AgentBreakReport {
    WTWindow breaksReportWindow = new WTWindow("", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true, false);
    WTPanel breaksReportPanel = new WTPanel("box");
    WTPanel contentPanel = new WTPanel("");
    WTScrollPane scrollPane = new WTScrollPane(breaksReportPanel);

    WTLabel breaksReportHeading = new WTLabel("", true, "lg", "b", 'c');

    WTLabel errorLabel = new WTLabel("", false, "md", "b", 'c');

    public AgentBreakReport(String userData) {

        String[] parts = userData.split(":");

        int userId = Integer.parseInt(parts[0]);
        String fullName = parts[1];

        breaksReportWindow.setTitle("User breaks - " + fullName);
        breaksReportHeading.setText(fullName + "'s breaks");

        breaksReportPanel.add(breaksReportHeading);

        try {

            Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                    Constants.DB_PASSWORD);

            Statement getUserBreaksSTMT = con.createStatement();
            ResultSet userBreaksRS = getUserBreaksSTMT
                    .executeQuery("SELECT start, end, type FROM breaks WHERE user_id = " + userId
                            + " ORDER BY start DESC");

            String hmPattern = "HH:mm";
            String dmyPattern = "dd/MM/yyyy";
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(hmPattern);
            if (!userBreaksRS.isBeforeFirst()) {
                errorLabel.setText("No records!");
            } else {
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

                    breakGroupPanel.add(new WTLabel(
                            userBreaksRS.getString(3).substring(0, 1).toUpperCase()
                                    + userBreaksRS.getString(3).substring(1)
                                    + " break: " + startTime
                                    + " to "
                                    + endTime,
                            false,
                            "md", "b", 'c'));

                    breakGroupPanel.add(new WTLabel(durationStr, false, "sm", "b", 'c'));

                    contentPanel.add(breakGroupPanel);
                }
            }
            con.close();
        } catch (Exception err) {
            System.out.println("ERROR IN USER REPORT ACTION PERFORMED: " + err);
        }

        breaksReportPanel.add(errorLabel);
        breaksReportPanel.add(contentPanel);

        breaksReportWindow.add(scrollPane);

        breaksReportWindow.setVisible(true);
    }
}
