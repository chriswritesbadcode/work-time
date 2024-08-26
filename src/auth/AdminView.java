package auth;

import components.WTLabel;
import components.WTPanel;
import components.WTWindow;
import consts.Constants;
import state.AppState;

public class AdminView {
    AppState state = AppState.getInstance();

    WTWindow adminWindow = new WTWindow("Work Time - Admin", Constants.DEF_WINDOW_W, Constants.DEF_WINDOW_H, true);

    WTPanel adminPanel = new WTPanel();

    WTLabel adminHeading = new WTLabel("Admin Dashboard", true, "lg", "b", 'c');

    public AdminView() {

        adminPanel.add(adminHeading);

        adminWindow.add(adminPanel);
        adminWindow.setVisible(true);
    }
}
