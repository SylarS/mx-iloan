package com.panshi.hujin2.iloan.util.ViewTools;

import com.panshi.hujin2.iloan.application.AppStaus;

public class ScreenTools {
    public static int sp2px(float spValue) {
        final float fontScale = AppStaus.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public static float dp2px(float dp) {
        float density = AppStaus.mContext.getResources().getDisplayMetrics().density;
        return dp * density;
    }
}
