package com.panshi.hujin2.iloan.model;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.megvii.livenesslib.util.ConUtil;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.CallRecordReq;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.bean.UploadContact;
import com.panshi.hujin2.iloan.bean.UserContactInputVOS;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.factory.RetrofitFileFactory;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.MediaTypeUtil;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/7/25 0025.
 */

public abstract class ContactModel implements BaseModel {
    public void uploadContact(final List<UserContactInputVOS> list, final ContactModelCallBack contactModelCallBack) {
        UploadContact uploadContact = new UploadContact();
        List<UploadContact.UserContactInputVOSBean> listContact = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            UploadContact.UserContactInputVOSBean userContactInputVOSBean = new UploadContact.UserContactInputVOSBean();
            userContactInputVOSBean.setContactGrade(list.get(i).getContactGrade());
            userContactInputVOSBean.setContactName(list.get(i).getContactName());
            userContactInputVOSBean.setContactPhone(list.get(i).getContactPhone());
            listContact.add(userContactInputVOSBean);
        }
        uploadContact.setUserContactInputVOS(listContact);
        RetrofitFactory.createFactoty().create(UserApi.class).
                uploadContact(MediaTypeUtil.createJsonMediaType(GonsonUtil.buildGosn().toJson(uploadContact))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                failture(response.getCode(), response.getMessage());
                return false;
            }
        }).subscribe(new CusumeObserver<Response>() {
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
                contactModelCallBack.uploadSucess();
            }
        });
    }

    /**
     * 上传 通讯记录
     *
     * @param callRecordReq
     * @param callback
     */
    public void upLoadCallRecord(CallRecordReq callRecordReq, final ContactModelCallBack callback) {
        RetrofitFactory.createFactoty().create(UserApi.class).uploadCallRecord(MediaTypeUtil.createJsonMediaType(GonsonUtil.buildGosn().toJson(callRecordReq)))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                failture(response.getCode(), response.getMessage());
                return false;
            }
        }).subscribe(new CusumeObserver<Response>() {
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
                callback.uploadCallRecordSucess();
                DebugLog.i("wang", "===upload_sucess_callrecord===");
            }
        });
    }

    /**
     * 上传短信
     */
    public void uploadSms(int num, final ContactModelCallBack callBack) {
        RetrofitFactory.createFactoty().create(UserApi.class).uploadSmsNum(num).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                failture(response.getCode(), response.getMessage());
                return false;
            }
        }).subscribe(new CusumeObserver<Response>() {
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
                callBack.uploadSmsSucess();
            }
        });
    }

    public interface ContactModelCallBack {
        void uploadSucess();

        void uploadCallRecordSucess();

        void uploadSmsSucess();
    }

    public void upLoadFaceFile(Bundle bundle, final UpLoadFaceFileInterface uploadRealModelInterface) {
        String result = "";
        int resultcode = 0;
        String delta = "";
        String bestPath = "";
        String envPath = "";
        String action1Path = "";
        String action2Path = "";
        String action3Path = "";
        try {
            String resultOBJ = bundle.getString("result");
            JSONObject resultJsonObject = new JSONObject(resultOBJ);
            result = resultJsonObject.getString("result");
            resultcode = resultJsonObject.getInt("resultcode");
            boolean isSuccess = resultJsonObject.getString("result").equals(
                    AppStaus.mContext.getResources().getString(R.string.verify_success));
            if (isSuccess) {
                delta = bundle.getString("delta");
                Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
                byte[] image_best = images.get("image_best");
                byte[] image_env = images.get("image_env");
                byte[] image_action1 = images.get("image_action1");
                byte[] image_action2 = images.get("image_action2");
                byte[] image_action3 = images.get("image_action3");
                //保存图片
                bestPath = ConUtil.saveJPGFile(AppStaus.mContext, image_best, "image_best");
                envPath = ConUtil.saveJPGFile(AppStaus.mContext, image_env, "image_env");
                action1Path = ConUtil.saveJPGFile(AppStaus.mContext, image_action1, "image_action1");
                action2Path = ConUtil.saveJPGFile(AppStaus.mContext, image_action2, "image_action2");
                action3Path = ConUtil.saveJPGFile(AppStaus.mContext, image_action3, "image_action3");
            } else {
                uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
            return;
        }
        if (TextUtils.isEmpty(result)) {
            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
            return;
        }
        if (resultcode == 0) {
            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
            return;
        }
        if (TextUtils.isEmpty(delta)) {
            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
            return;
        }
        if (TextUtils.isEmpty(bestPath)) {
            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
            return;
        }
        if (TextUtils.isEmpty(envPath)) {
            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
            return;
        }
        if (TextUtils.isEmpty(action1Path)) {
            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
            return;
        }
        if (TextUtils.isEmpty(action2Path)) {
            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
            return;
        }
        if (TextUtils.isEmpty(action3Path)) {
            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
            return;
        }

        MultipartBody.Builder builder = new MultipartBody.Builder();

        File bestPathFile = new File(bestPath);
        RequestBody requestBodyImageBest = RequestBody.create(MediaType.parse("multipart/form-data"), bestPathFile);
        builder.addFormDataPart("imageBest", bestPathFile.getName(), requestBodyImageBest);

        File imageEnvFile = new File(envPath);
        RequestBody requestBodyImageEnv = RequestBody.create(MediaType.parse("multipart/form-data"), imageEnvFile);
        builder.addFormDataPart("imageEnv", imageEnvFile.getName(), requestBodyImageEnv);

        File action1File = new File(action1Path);
        RequestBody requestBodyImageAction1 = RequestBody.create(MediaType.parse("multipart/form-data"), action1File);
        builder.addFormDataPart("imageAction1", action1File.getName(), requestBodyImageAction1);

        File imageAction2File = new File(action2Path);
        RequestBody requestBodyImageAction2 = RequestBody.create(MediaType.parse("multipart/form-data"), imageAction2File);
        builder.addFormDataPart("imageAction2", imageAction2File.getName(), requestBodyImageAction2);

        File imageAction3File = new File(action3Path);
        RequestBody requestBodyImageAction3 = RequestBody.create(MediaType.parse("multipart/form-data"), imageAction3File);
        builder.addFormDataPart("imageAction3", imageAction3File.getName(), requestBodyImageAction3);

        builder.setType(MultipartBody.FORM);//干什么的
//        builder.addFormDataPart("imageAction1Size", action1File.length() + "");
//        builder.addFormDataPart("imageAction2Size", "");
//        builder.addFormDataPart("imageAction3Size", "");
//        builder.addFormDataPart("imageBestSize", "");
//        builder.addFormDataPart("imageEnvSize", "");

        builder.addFormDataPart("resultCode", resultcode + "");
        builder.addFormDataPart("result", result);
        builder.addFormDataPart("delta", delta);
        Log.d("resultOBJresultOBJ", resultcode + "||" + result + "|||" + delta);
        RetrofitFileFactory.
                createFactoty().
                create(UserApi.class).
                uploadFaceFile(builder.build()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new CusumeObserver<Response>() {
                    @Override
                    public void onNext(@NonNull Response response) {
                        super.onNext(response);
                        if (response.getCode() == Constant.SUCESS_CODE) {
                            uploadRealModelInterface.uploadSucced();
                        } else {
                            uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        uploadRealModelInterface.uploadFail(R.string.meglive_reupload);
                    }
                });

    }

    public interface UpLoadFaceFileInterface {
        void uploadSucced();

        void uploadFail(int  s);
    }
}
