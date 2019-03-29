package com.panshi.hujin2.iloan.util;

import android.content.Context;

import com.panshi.hujin2.iloan.R;


/**
 * Created by Administrator on 2018/7/13 0013.
 */

public class BoolUtil {
    public static long mLastClickTime = 0;
    public static final int TIME_INTERVAL = 3000;
    public static boolean fastDouble(Context context){
        if (System.currentTimeMillis() - mLastClickTime >= TIME_INTERVAL) {
            //to do
            mLastClickTime = System.currentTimeMillis();
            return false;
        } else {
            ToastUtil.showToast(context.getString(R.string.double_fast));
            return true;
        }

    }
}
