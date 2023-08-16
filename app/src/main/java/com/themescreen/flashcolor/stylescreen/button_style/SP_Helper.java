package com.themescreen.flashcolor.stylescreen.button_style;

import android.content.Context;
import android.content.SharedPreferences;

public class SP_Helper {
    public static String CONSOLE = "iconstyle";
    public static String IMAGER = "imageurl";
    public static final String PREFERENCE = "ColorCallScreen2021";

    public static void putValueInSharedpreference(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getValueFromSharedprefrence(Context context, String str, String str2) {
        return context.getSharedPreferences(PREFERENCE, 0).getString(str, str2);
    }
}