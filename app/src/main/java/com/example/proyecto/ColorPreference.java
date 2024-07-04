package com.example.proyecto;

import android.content.SharedPreferences;
import android.content.Context;

public class ColorPreference {
    private static final String PREF_NAME = "color_preference";
    private static final String KEY_COLOR = "selected_color";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setColor(Context context, int colorResId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(KEY_COLOR, colorResId);
        editor.apply();
    }

    public static int getColor(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getInt(KEY_COLOR, R.color.Amarillo); // Valor por defecto Amarillo
    }
}
