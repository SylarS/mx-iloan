package com.panshi.hujin2.iloan.view.interfaces;

/**
 * Created by Administrator on 2018/7/25 0025.
 */

public interface WebViewInterface {
    void onUploadSucess();

    /**
     * 上传实名成功
     */
    void onUploadRealSucess();
    void onUploadRealFailture(int code,String message);

    //face上传成功失败
    void onUploadFaceSuccessed();

    void onUploadFaceFail(int failString);
}
