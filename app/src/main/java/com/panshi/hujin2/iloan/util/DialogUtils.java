package com.panshi.hujin2.iloan.util;

import android.app.Dialog;
import android.content.Context;

import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.view.activity.WebViewBannerActivity;
import com.panshi.hujin2.iloan.view.widget.LoadingDialog;

public class DialogUtils {

    public static void showDialog(Context context, Dialog loadingDialog, int string) {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        loadingDialog = LoadingDialog.createLoadingDialog(context, context.getResources().getString(string));
        loadingDialog.show();
    }

    public static void cancelDialog(Dialog loadingDialog) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }
}
