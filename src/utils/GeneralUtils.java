package utils;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;

import components.WTButton;

public class GeneralUtils {
    GeneralUtils() {

    }

    public static String formatDate(String pattern, long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return formatter.format(time);
    }

    public static void addListenerForEnter(WTButton button) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyChar() == '\n') {
                    button.doClick();
                }
                return false;
            }
        });
    }

    public static String capitalizeString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
