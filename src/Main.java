
import auth.LoginPage;
import auth.UserView;
import auth.SessionManager;

public class Main {

    public static void main(String args[]) {
        try {
            if (SessionManager.validateSession()) {
                new UserView();
            } else {
                new LoginPage();
            }
        } catch (Exception e) {
            new LoginPage();
        }
    }
}
