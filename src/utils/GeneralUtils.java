package utils;

import java.text.SimpleDateFormat;

public class GeneralUtils {
    GeneralUtils() {

    }

    public static String formatDate(String pattern, long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return formatter.format(time);
    }
}
