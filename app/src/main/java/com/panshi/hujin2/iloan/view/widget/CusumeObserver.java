package com.panshi.hujin2.iloan.view.widget;


import com.panshi.hujin2.iloan.bean.ResponeThrowable;
import com.panshi.hujin2.iloan.model.ProgessBaseModel;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.ExceptionHandle;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.BaseViewInterface;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;

/**
 * Created by Administrator on 2018/7/21 0021.
 */

public class CusumeObserver<T> extends DefaultObserver<T> {
    // ProgressDialog progressDialog;
    private BaseViewInterface baseViewInterface;
    private ProgessBaseModel progessBaseModel;

    public CusumeObserver() {

    }

    public CusumeObserver(Object window) {
        if (window != null) {
            if(window instanceof BaseViewInterface){
                baseViewInterface = (BaseViewInterface) window;
            }
            if(window instanceof ProgessBaseModel){
                progessBaseModel= (ProgessBaseModel) window;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (baseViewInterface != null) {
            baseViewInterface.showProgessDialog();
        }
       if(progessBaseModel!=null){
           progessBaseModel.showDialoging();
       }
      /*  progressDialog.show();*/
    }

    @Override
    public void onNext(@NonNull T t) {
        /*if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }*/
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        String message = "";
        if (baseViewInterface != null) {
            baseViewInterface.hiddenProgessDialog();
        }
        if(progessBaseModel!=null){
            progessBaseModel.hiddenDialoging();
        }
       /* if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }*/
      /*  if(AppStaus.mContext!=null){
            ToastUtil.showToast(AppStaus.mContext.getResources().getString(R.string.net_error));
        }*/
        ResponeThrowable throwable = ExceptionHandle.handleException(e);
        if (throwable.isShow) {
            ToastUtil.showToast(throwable.message);
        }
        DebugLog.i("wang", "=====" + e);
    }

    @Override
    public void onComplete() {
     /*   if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
*/
        if (baseViewInterface != null) {
            baseViewInterface.hiddenProgessDialog();
        }
        if(progessBaseModel!=null){
            progessBaseModel.hiddenDialoging();
        }
    }
}
