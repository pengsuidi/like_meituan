package com.amaker.personalinfo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 共享参数操作公共类
 */
public class PreferenceUtil {

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(Config.FILE_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void putString(Context context, String key, String value) {
        getPreference(context).edit().putString(key, value).apply();
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        getPreference(context).edit().putBoolean(key, value).apply();
    }

    public static void putInt(Context context, String key, Integer value) {
        getPreference(context).edit().putInt(key, value).apply();
    }


    public static String getString(Context context, String key) {
        return getPreference(context).getString(key, "");
    }

    public static Boolean getBoolean(Context context, String key) {
        return getPreference(context).getBoolean(key, false);
    }

    public static Integer getInt(Context context, String key) {
        return getPreference(context).getInt(key, 0);
    }
}
