package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import components.WTButton;
import state.AppState;

public class UserViewUtils {

    public UserViewUtils() {

    }

    public static void endBreak() {
        AppState state = AppState.getInstance();
        try {
            Connection con = DatabaseUtils.getConnection();
            PreparedStatement endBreakPSTMT = con
                    .prepareStatement("UPDATE breaks SET end = ? WHERE end IS NULL AND user_id = ?");
            endBreakPSTMT.setLong(1, new Date().getTime());
            endBreakPSTMT.setInt(2, state.getUserId());

            endBreakPSTMT.executeUpdate();

            con.close();
        } catch (Exception err) {
            System.out.println("ERROR IN USER VIEW END BREAK: " + err);
        }
    }

    public static void startBreak() {
        AppState state = AppState.getInstance();
        try {
            Connection con = DatabaseUtils.getConnection();
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

    public static void resetBreak() {
        AppState state = AppState.getInstance();

        state.setIsOnBreak(false);
        state.setBreakType("");
    }

    public static void disableButtons(String csv, WTButton startLunchBtn, WTButton startToiletBtn,
            WTButton startOtherBreakBtn, WTButton endWorkBtn) {
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

}
