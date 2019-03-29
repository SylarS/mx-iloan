package com.panshi.hujin2.iloan.model;

import android.text.TextUtils;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.ResponeThrowable;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.factory.RetrofitFileFactory;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.ExceptionHandle;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public abstract class UplaodRealModel implements BaseModel {
    public void uploadRealModel(HashMap<String, File> fMap, HashMap<String, String> pasthMap, HashMap<String, String> fsize, HashMap<String, String> fscale, String realInfoJson, final UploadRealModelInterface uploadRealModelInterface) {
        String idNumber = "";
        String username = "";
        String rfc = "";
        String fatherFirstName = "";
        String motherFirstName = "";
        boolean isFirst = true;
        DebugLog.i("wang", "===" + realInfoJson);
        try {
            JSONObject obj = new JSONObject(realInfoJson);
            if (obj.has("idNumber")) {
                idNumber = obj.getString("idNumber");
            }
            username = obj.getString("username");
            isFirst = obj.getBoolean("isFirst");
            if (obj.has("rfc")) {
                if (!TextUtils.isEmpty("rfc")) {
                    rfc = obj.getString("rfc");
                }
            }
            if (obj.has("fatherFirstName")) {
                if (!obj.isNull("fatherFirstName")) {
                    fatherFirstName = obj.getString("fatherFirstName");
                }
            }
            if (obj.has("motherFirstName")) {
                if (!obj.isNull("motherFirstName")) {
                    motherFirstName = obj.getString("motherFirstName");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.i("wang", "=====上传==");
        if (TextUtils.isEmpty(idNumber) || TextUtils.isEmpty(username)) {
            DebugLog.i("wang", "===id= userName==不能为空");
            //   failture(-1,);
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();


        if (fMap != null) {
            for (Map.Entry<String, File> f : fMap.entrySet()) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), f.getValue());
                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===f==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue().getName(), requestBody);

            }

        }
        builder.setType(MultipartBody.FORM);
        DebugLog.i("wang", "====" + idNumber + "||" + username + "||" + isFirst);
     /*   builder.addPart(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idNumber)));
        builder.addPart(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(username)));*/
        builder.addFormDataPart("isFirst", String.valueOf(isFirst));
        builder.addFormDataPart("username", String.valueOf(username));
        builder.addFormDataPart("idNumber", String.valueOf(idNumber));

        if (fsize != null) {
            for (Map.Entry<String, String> f : fsize.entrySet()) {

                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===f==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue());

            }

        }

        if (pasthMap != null) {
            for (Map.Entry<String, String> f : pasthMap.entrySet()) {

                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===f==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue());

            }

        }

        if (fscale != null) {
            for (Map.Entry<String, String> f : fscale.entrySet()) {

                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===f==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue());

            }

        }
        builder.addFormDataPart("rfc", String.valueOf(rfc));
        if (!TextUtils.isEmpty(fatherFirstName)) {
            builder.addFormDataPart("fatherFirstName", fatherFirstName);
        }
        if (!TextUtils.isEmpty(motherFirstName)) {
            builder.addFormDataPart("motherFirstName", motherFirstName);
        }

        RetrofitFileFactory.createFactoty().create(UserApi.class).uploadRealInfo(builder.build()).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CusumeObserver<Response>() {
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
                if (response.getCode() == Constant.SUCESS_CODE) {
                    uploadRealModelInterface.uploadRealSucess();
                    return;
                } else {
                    failture(response.getCode(), response.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
                ResponeThrowable throwable = ExceptionHandle.handleException(e);
                //    if(throwable.isShow){
                failture(throwable.code, throwable.message);
                //      }
            }
        });

    }

    public interface UploadRealModelInterface {
        void uploadRealSucess();
    }
}
