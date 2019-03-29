package com.panshi.hujin2.iloan.presenter;

import android.os.Bundle;

import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.bean.CallRecordReq;
import com.panshi.hujin2.iloan.bean.UserContactInputVOS;
import com.panshi.hujin2.iloan.model.ContactModel;
import com.panshi.hujin2.iloan.model.UplaodRealModel;
import com.panshi.hujin2.iloan.model.UploadFamalyModel;
import com.panshi.hujin2.iloan.model.UploadIncomeModel;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.WebViewInterface;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/25 0025.
 */

public class WebBannerPresenter extends BasePresenter {

    public void uploadContact(List<UserContactInputVOS> list) {
        new ContactModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((BaseViewInterface) getAttach()).showFailture(code, message);
                }
            }
        }.uploadContact(list, new ContactModel.ContactModelCallBack() {
            @Override
            public void uploadSucess() {
                if (isViewAttach()) {
                    ((WebViewInterface) getAttach()).onUploadSucess();
                }
            }

            @Override
            public void uploadCallRecordSucess() {

            }

            @Override
            public void uploadSmsSucess() {

            }
        });
    }

    /**
     * 上传实名
     *
     * @param fileMap
     * @param jsonStr
     */
    public void uploadFile(HashMap<String, File> fileMap, HashMap<String, String> pathSize, HashMap<String, String> fileSize, HashMap<String, String> fileScale, String jsonStr) {
        new UplaodRealModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((WebViewInterface) getAttach()).onUploadRealFailture(code, message);
                }

            }
        }.uploadRealModel(fileMap, pathSize, fileSize, fileScale, jsonStr, new UplaodRealModel.UploadRealModelInterface() {

            @Override
            public void uploadRealSucess() {
                if (isViewAttach()) {
                    if (isViewAttach()) {
                        ((WebViewInterface) getAttach()).onUploadRealSucess();
                    }
                }
            }
        });
    }

    /**
     * 上传收入证明
     *
     * @param fileMap
     * @param jsonStr
     */
    public void uploadIncomeFile(HashMap<String, File> fileMap, HashMap<String, String> pasthMap, HashMap<String, String> fileSize, HashMap<String, String> fileScale, String jsonStr) {
        new UploadIncomeModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((WebViewInterface) getAttach()).onUploadRealFailture(code, message);
                }

            }
        }.uploadIcomeModel(fileMap, pasthMap, fileSize, fileScale, jsonStr, new UploadIncomeModel.UploadRealModelInterface() {

            @Override
            public void uploadRealSucess() {
                if (isViewAttach()) {
                    if (isViewAttach()) {
                        ((WebViewInterface) getAttach()).onUploadRealSucess();
                    }
                }
            }
        });

    }

    /**
     * 上传家庭信息证明
     *
     * @param fileMap
     * @param jsonStr
     */
    public void uploFamailyFile(HashMap<String, File> fileMap, HashMap<String, String> pasthMap, HashMap<String, String> fileSize, HashMap<String, String> fileScale, String jsonStr) {
        new UploadFamalyModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((WebViewInterface) getAttach()).onUploadRealFailture(code, message);
                }

            }
        }.uploadFamilyModel(fileMap, pasthMap, fileSize, fileScale, jsonStr, new UploadFamalyModel.UploadRealModelInterface() {

            @Override
            public void uploadRealSucess() {
                if (isViewAttach()) {
                    if (isViewAttach()) {
                        ((WebViewInterface) getAttach()).onUploadRealSucess();
                    }
                }
            }
        });

    }

    /**
     * 上传通话记录
     */
    public void uploadCallRecord(CallRecordReq callRecordReq) {
        new ContactModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((BaseViewInterface) getAttach()).showFailture(code, message);
                }
            }
        }.upLoadCallRecord(callRecordReq, new ContactModel.ContactModelCallBack() {
            @Override
            public void uploadSucess() {

            }

            @Override
            public void uploadCallRecordSucess() {
                DebugLog.i("wang", "==通话记录上传成功===");
            }

            @Override
            public void uploadSmsSucess() {

            }
        });
    }

    public void uploadSmsNum(int num) {
        new ContactModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((BaseViewInterface) getAttach()).showFailture(code, message);
                }
            }
        }.uploadSms(num, new ContactModel.ContactModelCallBack() {
            @Override
            public void uploadSucess() {

            }

            @Override
            public void uploadCallRecordSucess() {

            }

            @Override
            public void uploadSmsSucess() {
                DebugLog.i("wang", "===sms==sucess==");
            }
        });
    }

    public void uploadFaceFile(Bundle bundle) {
        new ContactModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((WebViewInterface) getAttach()).onUploadFaceFail(R.string.meglive_reupload);
                }
            }
        }.
                upLoadFaceFile(bundle, new ContactModel.UpLoadFaceFileInterface() {
                    @Override
                    public void uploadSucced() {
                        ((WebViewInterface) getAttach()).onUploadFaceSuccessed();
                    }

                    @Override
                    public void uploadFail(int s) {
                        ((WebViewInterface) getAttach()).onUploadFaceFail(s);
                    }
                });
    }
}
