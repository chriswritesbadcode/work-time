package sections;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import components.WTButton;
import components.WTLabel;
import components.WTOptionPane;
import components.WTPanel;
import components.WTScrollPane;
import components.WTWindow;
import consts.Constants;
import utils.DatabaseUtils;
import utils.GeneralUtils;

public class AgentReport implements ActionListener {

        String reportType;
        String userData;
        int userId;
        String fullName;

        WTWindow reportWindow = new WTWindow("", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true, false);
        WTPanel reportPanel = new WTPanel("box");
        WTPanel contentPanel = new WTPanel("");
        WTScrollPane scrollPane = new WTScrollPane(reportPanel);

        WTLabel reportHeading = new WTLabel("", true, "lg", "b", 'c');

        WTLabel rangeLabel = new WTLabel("Show last: ", false, "sm", "b", 'c');
        JFormattedTextField rangeTextField = new JFormattedTextField(GeneralUtils.getNumberFormatter());
        JComboBox<String> dropDownBox = new JComboBox<String>(Constants.orderResultsByChoices);
        WTButton submitSearchBtn = new WTButton("Search");
        WTLabel totalDurationLabel = new WTLabel("", false, "sm", "b", 'c');

        WTLabel errorLabel = new WTLabel("", false, "md", "b", 'c');

        public AgentReport(String userData, String reportType) {
                // userdata = id:fullName:reportTypeChar
                // reportType = breaks or work times
                String[] parts = userData.split(":");
                this.userData = userData;
                this.reportType = reportType;
                this.userId = Integer.parseInt(parts[0]);
                this.fullName = parts[1];

                reportWindow.setTitle("Agent " + reportType + " report - " + fullName);
                reportHeading.setText(fullName + "'s " + reportType);

                reportPanel.add(reportHeading);

                reportPanel.add(rangeLabel);
                reportPanel.add(rangeTextField);

                rangeTextField.setValue(12);
                rangeTextField.setMaximumSize(new Dimension(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT));
                submitSearchBtn.addActionListener(this);

                dropDownBox.setVisible(true);
                dropDownBox.setMaximumSize(new Dimension(Constants.DEF_INPUT_WIDTH, Constants.DEF_INPUT_HEIGHT));
                reportPanel.add(dropDownBox);
                reportPanel.add(submitSearchBtn);
                reportPanel.add(totalDurationLabel);

                showData();

                reportPanel.add(errorLabel);
                reportPanel.add(contentPanel);

                reportWindow.add(scrollPane);

                reportWindow.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                if (e.getSource() == submitSearchBtn) {
                        showData();
                }
        }

        private ResultSet getData(Connection con) throws SQLException {

                String endOfQueryString = "WHERE user_id = "
                                + userId
                                + " ORDER BY start "
                                + (dropDownBox.getSelectedItem()
                                                .toString()
                                                .equals("Newest first")
                                                                ? "DESC"
                                                                : "ASC")
                                + " LIMIT "
                                + Integer.parseInt(
                                                rangeTextField
                                                                .getValue().toString());

                Statement getReportDataSTMT = con.createStatement();
                ResultSet reportRS = getReportDataSTMT
                                .executeQuery(
                                                reportType.equals("work times")
                                                                ? "SELECT start, end FROM work_times "
                                                                                + endOfQueryString
                                                                : "SELECT start, end, type FROM breaks "
                                                                                + endOfQueryString);
                return reportRS;
        }

        private void showData() {
                contentPanel.removeAll();
                try {
                        Connection con = DatabaseUtils.getConnection();

                        ResultSet reportRS = getData(con);

                        if (!reportRS.isBeforeFirst()) {
                                errorLabel.setText("No records!");
                        } else {
                                DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(Constants.HM_PATTERN);
                                long totalDuration = 0;
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
                                        totalDuration += duration.toMillis();

                                        String durationStr = (days != 0 ? days + "d, " : "") + hours + "h, "
                                                        + minutes + "m";

                                        WTPanel reportChunkPanel = new WTPanel("box");
                                        reportChunkPanel.add(new WTLabel(
                                                        GeneralUtils.formatDate(
                                                                        Constants.DMY_PATTERN, reportRS.getLong(1)),
                                                        true, "sm", "b", 'c'));

                                        reportChunkPanel.add(new WTLabel(
                                                        (reportType.equals("work times") ? ""
                                                                        : GeneralUtils.capitalizeString(
                                                                                        reportRS.getString(3)) + ": ")
                                                                        + startTime
                                                                        + " to " + endTime,
                                                        false,
                                                        "md", "b",
                                                        'c'));

                                        reportChunkPanel.add(`
                                                        new WTLabel(durationStr, false, "sm", "b", 'c'));
                                        contentPanel.add(reportChunkPanel);
                                }

                                long hourDur = totalDuration / 1000 / 3600;
                                long minDur = (totalDuration / 1000 % 3600) / 60;
                                totalDurationLabel.setText("Total: " + hourDur + "h, " + minDur + "m");

                        }
                        con.close();
                } catch (SQLException err) {
                        WTOptionPane.showMessageBox("Error in get agent report - custom data fn: " + err);
                }
                contentPanel.revalidate();
                contentPanel.repaint();
        }
}
