package net.wasnot.android.databasetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by akihiroaida on 15/12/02.
 */
public class PreferenceUtil {

    private static final String REPEAT_TEST_DATA = "repeat_test_data";

    public static boolean isRepeatTestData(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(REPEAT_TEST_DATA, false);
    }

    public static void setRepeatTestData(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(REPEAT_TEST_DATA, value).apply();
    }

}
