package validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static final String FNAME_REGEX = "^[a-zA-Z ]{2,50}$";
    private static final String UNAME_REGEX = "^[a-z]{4,20}$";
    private static final String PW_REGEX = "^[0-9]{4}$";

    private static final Pattern pwPattern = Pattern.compile(PW_REGEX);
    private static final Pattern fnPattern = Pattern.compile(FNAME_REGEX);
    private static final Pattern unPattern = Pattern.compile(UNAME_REGEX);

    public static boolean validatePasword(String password) {
        Matcher pwMatcher = pwPattern.matcher(password);

        return pwMatcher.matches();
    }

    public static boolean validateFullName(String fullName) {
        Matcher fnMatcher = fnPattern.matcher(fullName);

        return fnMatcher.matches();
    }

    public static boolean validateUserName(String userName) {
        Matcher unMatcher = unPattern.matcher(userName);

        return unMatcher.matches();
    }

}
