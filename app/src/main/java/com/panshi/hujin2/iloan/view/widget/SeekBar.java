package com.panshi.hujin2.iloan.view.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.panshi.hujin2.iloan.util.DebugLog;
import com.zhouyou.view.seekbar.SignSeekBar;


/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class SeekBar extends SignSeekBar {
    public SeekBar(Context context) {
        super(context);
    }

    public SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getProgress() {
        int progress = super.getProgress();
        DebugLog.i("wang", "progess==" + progress);
        int a = progress / 100;
        DebugLog.i("wang", "===" + a);
        return a * 100;
    }
}
