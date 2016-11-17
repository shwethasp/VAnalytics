package common.util;

import android.content.Context;
import android.util.TypedValue;

public class CommonUtil {
    private static final int SALT = 1;

    public static int getThemeResourceId(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId;
    }

    public static final String incrementEachChar(String key) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < key.length(); i += SALT) {
            result.append((char) (key.charAt(i) + SALT));
        }
        return result.toString();
    }

    public static final String decrementEachChar(String key) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < key.length(); i += SALT) {
            result.append((char) (key.charAt(i) - 1));
        }
        return result.toString();
    }
}
