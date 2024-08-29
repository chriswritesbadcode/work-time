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

public class AgentWorkReport {

        WTWindow workReportWindow = new WTWindow("", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true, false);
        WTPanel workReportPanel = new WTPanel("box");
        WTPanel contentPanel = new WTPanel("");
        WTScrollPane scrollPane = new WTScrollPane(workReportPanel);

        WTLabel workReportHeading = new WTLabel("", true, "lg", "b", 'c');

        WTLabel errorLabel = new WTLabel("", false, "md", "b", 'c');

        public AgentWorkReport(String userData) {

                String[] parts = userData.split(":");

                int userId = Integer.parseInt(parts[0]);
                String fullName = parts[1];

                workReportWindow.setTitle("Agent report - " + fullName);
                workReportHeading.setText(fullName + "'s work times");

                workReportPanel.add(workReportHeading);

                try {

                        Connection con = DriverManager.getConnection(Constants.DB_HOST, Constants.DB_USER,
                                        Constants.DB_PASSWORD);

                        Statement getUserWorkTimesSTMT = con.createStatement();
                        ResultSet userWorkTimesRS = getUserWorkTimesSTMT
                                        .executeQuery("SELECT start, end FROM work_times WHERE user_id = " + userId
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

                        }
                        con.close();
                } catch (Exception err) {
                        System.out.println("ERROR IN USER REPORT ACTION PERFORMED: " + err);
                }

                workReportPanel.add(errorLabel);
                workReportPanel.add(contentPanel);

                workReportWindow.add(scrollPane);

                workReportWindow.setVisible(true);
        }
}
