package consts;

import java.util.regex.Pattern;

public final class Constants {
    private Constants() {
    }

    public static final String APP_NAME = "Work Time";
    public static final String SESSION_FILE = "session.dat";
    public static final int SESSION_VALIDITY_DAYS = 2;
    public static final String DB_HOST = "jdbc:mysql://" + System.getenv("WTDB_HOST") + "/"
            + System.getenv("WTDB_NAME");
    public static final String DB_USER = System.getenv("WTDB_USER");
    public static final String DB_PASSWORD = System.getenv("WTDB_PASSWORD");

    public static final int DEF_WINDOW_W = 600;
    public static final int DEF_WINDOW_H = 300;

    public static final int DEF_INPUT_WIDTH = 200;
    public static final int DEF_INPUT_HEIGHT = 30;

    public static final int SMALL_Y_SPACER_HEIGHT = 10;
    public static final int SMALL_Y_SPACER_WIDTH = 0;
    public static final int MEDIUM_Y_SPACER_HEIGHT = 20;
    public static final int MEDIUM_Y_SPACER_WIDTH = 0;
    public static final int LARGE_Y_SPACER_HEIGHT = 30;
    public static final int LARGE_Y_SPACER_WIDTH = 0;

    public static final int SMALL_X_SPACER_HEIGHT = 0;
    public static final int SMALL_X_SPACER_WIDTH = 10;
    public static final int MEDIUM_X_SPACER_HEIGHT = 0;
    public static final int MEDIUM_X_SPACER_WIDTH = 20;
    public static final int LARGE_X_SPACER_HEIGHT = 0;
    public static final int LARGE_X_SPACER_WIDTH = 30;

    // REGEX -----------------------
    public static final String FNAME_REGEX = "^[a-zA-Z ]{2,50}$";
    public static final String UNAME_REGEX = "^[a-z]{4,20}$";
    public static final String PW_REGEX = "^[0-9]{4}$";

    public static final Pattern PW_PATTERN = Pattern.compile(Constants.PW_REGEX);
    public static final Pattern FN_PATTERN = Pattern.compile(Constants.FNAME_REGEX);
    public static final Pattern UN_PATTERN = Pattern.compile(Constants.UNAME_REGEX);
    // -----------------------------
}
