package common.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;




public class E6bappsPreferences {
    private static final long DAY = 21600000;
    protected static final String PREF = "MyPrefs";

    public static void adClic(Context context, long hideTime) {
        context.getSharedPreferences(PREF, 0).edit().putLong(Constants.PREF_ADS_LAST_CLIC, System.currentTimeMillis() + hideTime).commit();
    }

    public static long getAdLastHideTime(Context context) {
        return Math.max(getSimpleAdLastClicTime(context), getAdFullScreenLastClicTime(context));
    }

    public static long getSimpleAdLastClicTime(Context context) {
        return context.getSharedPreferences(PREF, 0).getLong(Constants.PREF_ADS_LAST_CLIC, 0);
    }

    public static void adReminderShowed(Context context) {
        context.getSharedPreferences(PREF, 0).edit().putLong(Constants.PREF_ADS_LAST_MSG, System.currentTimeMillis()).commit();
    }

    public static long getLastMsgTime(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF, 0);
        long firstTime = settings.getLong(Constants.PREF_ADS_FIRST_TIME, 0);
        if (firstTime == 0) {
            firstTime = System.currentTimeMillis();
            settings.edit().putLong(Constants.PREF_ADS_FIRST_TIME, firstTime).commit();
        }
        return settings.getLong(Constants.PREF_ADS_LAST_MSG, firstTime);
    }

    public static boolean showWhatNew(Context context, int version) {
        SharedPreferences settings = context.getSharedPreferences(PREF, 0);
        int savedVersion = settings.getInt(Constants.VERSION_CODE, 0);
        boolean showWhatNew = (savedVersion == version || savedVersion == 0) ? false : true;
        if (showWhatNew || savedVersion == 0) {
            settings.edit().putInt(Constants.VERSION_CODE, version).commit();
        }
        return showWhatNew;
    }

    public static void adFullScreenClic(Activity context, long hideTime) {
        context.getSharedPreferences(PREF, 0).edit().putLong(Constants.PREF_ADS_FULL_SCREEN_LAST_CLIC, System.currentTimeMillis() + hideTime).commit();
    }

    public static long getAdFullScreenLastClicTime(Context context) {
        return context.getSharedPreferences(PREF, 0).getLong(Constants.PREF_ADS_FULL_SCREEN_LAST_CLIC, 0);
    }

    public static void adFullScreenClosed(Activity context) {
        SharedPreferences settings = context.getSharedPreferences(PREF, 0);
        settings.edit().putInt(Constants.PREF_ADS_FULL_SCREEN_CLOSE_TIMES, settings.getInt(Constants.PREF_ADS_FULL_SCREEN_CLOSE_TIMES, 0) + 1).commit();
    }

    /*public static boolean showTagMessage(Context context, TagMessage tagMessage) {
        SharedPreferences settings = context.getSharedPreferences(PREF, 0);
        if (tagMessage == null) {
            return false;
        }
        int code = tagMessage.hashCode();
        if (code == settings.getInt(Constants.TAG_MESSAGE_CODE, 0)) {
            return false;
        }
        settings.edit().putInt(Constants.TAG_MESSAGE_CODE, code).commit();
        return true;
    }*/

    public static int getSimpleAdCountPerDay(Context context) {
        return getCountPerDay(context, Constants.PREF_ADS_SIMPLE_COUNT, Constants.PREF_ADS_SIMPLE_COUNT_DAY);
    }

    public static int getFullScreenAdCountPerDay(Context context) {
        return getCountPerDay(context, Constants.PREF_ADS_FULL_SCREEN_COUNT, Constants.PREF_ADS_FULL_SCREEN_COUNT_DAY);
    }

    public static void simpleAdCountPerDay(Context context) {
        adCountPerDay(context, Constants.PREF_ADS_SIMPLE_COUNT, Constants.PREF_ADS_SIMPLE_COUNT_DAY);
    }

    public static void fullScreenAdCountPerDay(Context context) {
        adCountPerDay(context, Constants.PREF_ADS_FULL_SCREEN_COUNT, Constants.PREF_ADS_FULL_SCREEN_COUNT_DAY);
    }

    private static void adCountPerDay(Context context, String adCountId, String adDayId) {
        SharedPreferences settings = context.getSharedPreferences(PREF, 0);
        Editor editor = settings.edit();
        if (System.currentTimeMillis() - settings.getLong(adDayId, 0) > DAY) {
            editor.putLong(adDayId, System.currentTimeMillis());
            editor.putInt(adCountId, 1);
        } else {
            editor.putInt(adCountId, settings.getInt(adCountId, 0) + 1);
        }
        editor.commit();
    }

    private static int getCountPerDay(Context context, String adCountId, String adDayId) {
        SharedPreferences settings = context.getSharedPreferences(PREF, 0);
        if (System.currentTimeMillis() - settings.getLong(adDayId, 0) <= DAY) {
            return settings.getInt(adCountId, 0);
        }
        return 0;
    }

    public static int getOnboardingCount(Context context) {
        return context.getSharedPreferences(PREF, 0).getInt(Constants.PREF_ONBOARDING_COUNT, 0);
    }

    public static void addOnboardingCount(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF, 0);
        int count = settings.getInt(Constants.PREF_ONBOARDING_COUNT, 0) + 1;
        if (count < Integer.MAX_VALUE) {
            settings.edit().putInt(Constants.PREF_ONBOARDING_COUNT, count).commit();
        }
    }

    public static void resetOnboardingCount(Context context) {
        context.getSharedPreferences(PREF, 0).edit().putInt(Constants.PREF_ONBOARDING_COUNT, 0).commit();
    }

    public static void adFullScreenShowed(Activity context) {
        context.getSharedPreferences(PREF, 0).edit().putLong(Constants.PREF_ADS_FULL_SCREEN_SHOWED, System.currentTimeMillis()).commit();
    }

    public static long getAdFullScreenShowed(Activity context) {
        return context.getSharedPreferences(PREF, 0).getLong(Constants.PREF_ADS_FULL_SCREEN_SHOWED, 0);
    }

    public static void setLaunch(Context context, boolean launch) {
        context.getSharedPreferences(PREF, 0).edit().putBoolean(Constants.PREF_APP_LAUNCH, launch).commit();
    }

    public static boolean isLaunch(Context context) {
        return context.getSharedPreferences(PREF, 0).getBoolean(Constants.PREF_APP_LAUNCH, true);
    }
}
