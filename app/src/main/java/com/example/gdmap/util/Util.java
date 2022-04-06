package com.example.gdmap.util;

import android.content.Context;

public class Util {
    public static double LATITUDE = -10000;
    public static double LONGITUDE = -10000;

    public static int pxToDip(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    public static int dipToPx(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static float floatTo2(float value) {
        return Math.round(value * 10) / 10.0f;
    }
}
