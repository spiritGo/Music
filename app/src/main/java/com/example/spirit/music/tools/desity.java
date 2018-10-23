package com.example.spirit.music.tools;

import android.content.Context;

public class desity {

    public static float px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (px / density + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
