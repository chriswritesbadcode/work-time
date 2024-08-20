
public class UserView {

    UserView() {
        WTWindow userWindow = new WTWindow("Work Time", 600, 300, true);
        WTPanel panel = new WTPanel();
        WTLabel text = new WTLabel("Work Time");
        panel.add(text);

        WTButton endOtherBreakBtn = new WTButton("Start Break");
        WTButton startOtherBreakBtn = new WTButton("End Break");
        WTButton endToiletBtn = new WTButton("End Toilet Break");
        WTButton startToiletBtn = new WTButton("Start Toilet Break");
        WTButton endLunchBtn = new WTButton("End Lunch");
        WTButton startLunchBtn = new WTButton("Start Lunch");
        WTButton endWorkBtn = new WTButton("End Work");
        WTButton startWorkBtn = new WTButton("Start Work");

        panel.add(startWorkBtn);
        panel.add(endWorkBtn);
        panel.add(startLunchBtn);
        panel.add(endLunchBtn);
        panel.add(startToiletBtn);
        panel.add(endToiletBtn);
        panel.add(startOtherBreakBtn);
        panel.add(endOtherBreakBtn);

        userWindow.add(panel);
        userWindow.setVisible(true);
    }
}
