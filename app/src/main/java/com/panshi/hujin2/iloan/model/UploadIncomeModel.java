package com.panshi.hujin2.iloan.model;

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
public abstract class UploadIncomeModel implements BaseModel {
    public void uploadIcomeModel(HashMap<String, File> fMap,HashMap<String,String>pasthMap,HashMap<String,String> sizeMap, HashMap<String,String> scaleMap,String realInfoJson, final UploadRealModelInterface uploadRealModelInterface) {
        String incomeSource = "";
        String company = "";
        String companyStreet = "";
        String companyAddress = "";
        String position = "";

        String companyBlock = "";// 公司地址区
        String companyMpio = "";  // 公司地址
        String companyCity = "";  // 公司地址城市
        String companyState = "";  // 公司地址洲

        String companyPostCode = "";  // 公司地址洲

        boolean isFirst = true;
        DebugLog.i("wang", "===" + realInfoJson);
        try {
            JSONObject obj = new JSONObject(realInfoJson);
            isFirst = obj.getBoolean("isFirst");
            incomeSource = obj.getString("incomeSource");
            if (obj.has("company")) {
                if (!obj.isNull("company")) {
                    company = obj.getString("company");
                }
            }
            if (obj.has("companyStreet")) {
                if (!obj.isNull("companyStreet")) {
                    companyStreet = obj.getString("companyStreet");
                }
            }
            if (obj.has("companyAddress")) {
                if (!obj.isNull("companyAddress")) {
                    companyAddress = obj.getString("companyAddress");
                }
            }

            if (obj.has("position")) {
                if (!obj.isNull("position")) {
                    position = obj.getString("position");
                }
            }
            if (obj.has("companyBlock")) {
                if (!obj.isNull("companyBlock")) {
                    companyBlock = obj.getString("companyBlock");
                }
            }
            if (obj.has("companyMpio")) {
                if (!obj.isNull("companyMpio")) {
                    companyMpio = obj.getString("companyMpio");
                }
            }

            if (obj.has("companyCity")) {
                if (!obj.isNull("companyCity")) {
                    companyCity = obj.getString("companyCity");
                }
            }
            if (obj.has("companyState")) {
                if (!obj.isNull("companyState")) {
                    companyState = obj.getString("companyState");
                }
            }
            if (obj.has("companyPostCode")) {
                if (!obj.isNull("companyPostCode")) {
                    companyPostCode = obj.getString("companyPostCode");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.i("wang", "=====上传==");
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
        DebugLog.i("wang", "====" + incomeSource + "||" + company + "||" + isFirst);
     /*   builder.addPart(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idNumber)));
        builder.addPart(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(username)));*/
        builder.addFormDataPart("isFirst", String.valueOf(isFirst));
        builder.addFormDataPart("incomeSource", String.valueOf(incomeSource));
        builder.addFormDataPart("company", company);
        builder.addFormDataPart("companyStreet", companyStreet);
        builder.addFormDataPart("position", position);
        builder.addFormDataPart("companyBlock", companyBlock);
        builder.addFormDataPart("companyMpio", companyMpio);
        builder.addFormDataPart("companyCity", companyCity);
        builder.addFormDataPart("companyState", companyState);
        builder.addFormDataPart("companyPostCode", companyPostCode);
        builder.addFormDataPart("companyAddress", companyAddress);
        if(sizeMap != null) {
            for (Map.Entry<String, String> f : sizeMap.entrySet()) {
                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===size==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue());

            }

        }

        if(pasthMap != null) {
            for (Map.Entry<String, String> f : pasthMap.entrySet()) {
                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===size==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue());

            }

        }

        if(scaleMap != null) {
            for (Map.Entry<String, String> f : scaleMap.entrySet()) {
                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===scale==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue());

            }

        }

        RetrofitFileFactory.createFactoty().create(UserApi.class).uploadInComeInfo(builder.build()).
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
                // if(throwable.isShow){
                failture(throwable.code, throwable.message);
                //         }
            }
        });

    }

    public interface UploadRealModelInterface {
        void uploadRealSucess();
    }
}
