package com.themescreen.flashcolor.stylescreen.custom_dailor.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SP_Helper {
    public static String ICONSTYLE = "iconstyle";
    public static String IMAGEURL = "imageurl";
    public static final String MYPREFERENCE = "ColorCallScreen2021";

    public static void put_value_in_sharedpreference(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MYPREFERENCE, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String get__value_from_sharedprefrence(Context context, String str, String str2) {
        return context.getSharedPreferences(MYPREFERENCE, 0).getString(str, str2);
    }
}
