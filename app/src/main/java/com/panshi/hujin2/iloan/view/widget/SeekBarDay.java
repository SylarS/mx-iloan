package com.panshi.hujin2.iloan.view.widget;
import android.content.Context;
import android.util.AttributeSet;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.zhouyou.view.seekbar.SignSeekBar;
/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class SeekBarDay extends SignSeekBar {
    public SeekBarDay(Context context) {
        super(context);
    }

    public SeekBarDay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarDay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getProgress() {
        int progress = super.getProgress();
        DebugLog.i("wang", "progess==" + progress);
        int a = progress / 7;
        DebugLog.i("wang", "===" + a);
      /*  if (progress / 7 == 3) {
            a = 4;
        }*/
      if(progress/7f>1.5&&progress/7f<2){
          a=2;
      }
        return a * 7;
    }

    @Override
    public String getMaxText() {
        String value=super.getMaxText();
      /*  if(mcontext!=null){
            value=mcontext.getResources().getString(R.string.empty_str2,value);
            DebugLog.i("wang","==max=="+value+"||"+value.length());
        }*/

        return  "&"+value+"&";

    }

    @Override
    public String getMinText() {
        String value=super.getMinText();
     /*   if(mcontext!=null){
            value=mcontext.getResources().getString(R.string.empty_str3,value);
            DebugLog.i("wang","==min=="+value+"||"+value.length());
        }*/
        return "&&"+value+"&";
    }
}
