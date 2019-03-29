package com.panshi.hujin2.iloan.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.panshi.hujin2.iloan.application.AppStaus;


public class ToastUtil {

	private static Toast mToast;

//	public static void showToast(String title) {
//		showToast(title, Toast.LENGTH_SHORT);
//	}

	/**
	 * 吐丝替换，旧吐丝不再多次显示
	 * @param string 吐丝内容
     */
	public static void showToast(String string) {
		if (TextUtils.isEmpty(string)) {
			return;
		}
		if (mToast == null){
			mToast = Toast.makeText(AppStaus.mContext,"",Toast.LENGTH_SHORT);
		}
		mToast.setText(string);
		mToast.show();
	}

//	private ToastUtil() {
//
//	}
//
//	public static void showToast(int resId) {
//		showToast(AppStatus.mContext.getResources().getString(resId));
//	}
//
//	public static void showToast(int resId, Object... formatArgs) {
//		showToast(AppStatus.mContext.getResources().getString(resId,
//				formatArgs));
//	}
//
//	public static void cancel() {
//		if (mToast != null) {
//			mToast = null;
//		}
//	}
}
