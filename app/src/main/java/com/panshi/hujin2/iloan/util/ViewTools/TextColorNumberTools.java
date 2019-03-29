package com.panshi.hujin2.iloan.util.ViewTools;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;

import static com.panshi.hujin2.iloan.util.DensityUtil.sp2px;


/**
 * Created by 123 on 2017/10/23.
 */

public class TextColorNumberTools {

    public static SpannableString setTextColorfulNumber(String all, String number, int size, int color) {
        SpannableString spannableString = new SpannableString(all);
        spannableString.setSpan(new ForegroundColorSpan(AppStaus.mContext.getResources().getColor(color)),
                all.indexOf(number),
                all.indexOf(number) + number.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(sp2px(AppStaus.mContext, size)), all.indexOf(number),
                all.indexOf(number) + number.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
