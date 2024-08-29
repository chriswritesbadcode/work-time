package sections;

import java.sql.Connection;
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
import utils.DatabaseUtils;
import utils.GeneralUtils;

public class AgentReport {

    WTWindow reportWindow = new WTWindow("", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true, false);
    WTPanel reportPanel = new WTPanel("box");
    WTPanel contentPanel = new WTPanel("");
    WTScrollPane scrollPane = new WTScrollPane(reportPanel);

    WTLabel reportHeading = new WTLabel("", true, "lg", "b", 'c');

    WTLabel errorLabel = new WTLabel("", false, "md", "b", 'c');

    public AgentReport(String userData, String reportType) {
        // userdata = id:fullName:reportTypeChar
        // reportType = breaks or work times

        String[] parts = userData.split(":");

        int userId = Integer.parseInt(parts[0]);
        String fullName = parts[1];

        reportWindow.setTitle("Agent " + reportType + " report - " + fullName);
        reportHeading.setText(fullName + "'s " + reportType);

        reportPanel.add(reportHeading);

        try {

            Connection con = DatabaseUtils.getConnection();

            Statement getReportDataSTMT = con.createStatement();
            ResultSet reportRS = getReportDataSTMT
                    .executeQuery(
                            reportType.equals("work times")
                                    ? "SELECT start, end FROM work_times WHERE user_id = " + userId
                                            + " ORDER BY start DESC"
                                    : "SELECT start, end, type FROM breaks WHERE user_id = " + userId
                                            + " ORDER BY start DESC");

            if (!reportRS.isBeforeFirst()) {
                errorLabel.setText("No records!");
            } else {
                DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(Constants.HM_PATTERN);

                while (reportRS.next()) {

                    String startTime = GeneralUtils.formatDate(Constants.HM_PATTERN,
                            reportRS.getLong(1));
                    String endTime = GeneralUtils.formatDate(Constants.HM_PATTERN,
                            reportRS.getLong(2));

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

                    WTPanel reportChunkPanel = new WTPanel("box");
                    reportChunkPanel.add(new WTLabel(
                            GeneralUtils.formatDate(
                                    Constants.DMY_PATTERN, reportRS.getLong(1)),
                            true, "sm", "b", 'c'));

                    reportChunkPanel.add(new WTLabel(
                            (reportType.equals("work times") ? "Work: "
                                    : GeneralUtils.capitalizeString(reportRS.getString(3)) + ": ") + startTime
                                    + " to " + endTime,
                            false,
                            "md", "b",
                            'c'));

                    reportChunkPanel.add(
                            new WTLabel(durationStr, false, "sm", "b", 'c'));
                    contentPanel.add(reportChunkPanel);
                }

            }
            con.close();
        } catch (Exception err) {
            System.out.println("ERROR IN USER REPORT ACTION PERFORMED: " + err);
        }

        reportPanel.add(errorLabel);
        reportPanel.add(contentPanel);

        reportWindow.add(scrollPane);

        reportWindow.setVisible(true);
    }
}
