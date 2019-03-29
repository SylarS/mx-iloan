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

public abstract class UploadFamalyModel implements BaseModel {
    public void uploadFamilyModel(HashMap<String, File> fMap, HashMap<String, String> pasthMap, HashMap<String, String> sizeMap, HashMap<String, String> scaleMap, String famailyInfoJson, final UploadRealModelInterface uploadRealModelInterface) {
        String fatherName = "";//父亲全名
        String motherName = "";// 母亲全名
        String homeTelephone = "";//家庭电话
        String homeStreet = "";// 家庭详细地址
        String homeBlock = "";//家庭地址区
        String homeMpio = "";   //家庭地址mpio
        String homeCity = "";   //家庭地址城市
        String homeState = "";  // 家庭地址洲
        String homeAddress = "";
        String familyMembersCount = "";
        String addressType = "";
        String addressPostCode = "";

        boolean isFirst = true;
        DebugLog.i("wang", "===" + famailyInfoJson);
        try {
            JSONObject obj = new JSONObject(famailyInfoJson);
            isFirst = obj.getBoolean("isFirst");
            fatherName = obj.getString("fatherName");
            motherName = obj.getString("motherName");
            homeTelephone = obj.getString("homeTelephone");
            // homeStreet = obj.getString("homeStreet");
            homeBlock = obj.getString("homeBlock");
            homeMpio = obj.getString("homeMpio");
            homeCity = obj.getString("homeCity");
            homeState = obj.getString("homeState");
            homeAddress = obj.getString("homeAddress");
            familyMembersCount = obj.getString("familyMembersCount");
            addressType = obj.getString("addressType");
            addressPostCode = obj.getString("addressPostCode");

            //familyMembersCount//家庭人数  addressType addressPostCode
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
        DebugLog.i("wang", "====" + homeTelephone + "||" + homeStreet + "||" + isFirst);
     /*   builder.addPart(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idNumber)));
        builder.addPart(RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(username)));*/
        builder.addFormDataPart("isFirst", String.valueOf(isFirst));
        builder.addFormDataPart("fatherName", fatherName);
        builder.addFormDataPart("motherName", motherName);
        builder.addFormDataPart("homeTelephone", homeTelephone);
//        builder.addFormDataPart("homeStreet", homeStreet);
        builder.addFormDataPart("homeBlock", homeBlock);
        builder.addFormDataPart("homeMpio", homeMpio);
        builder.addFormDataPart("homeCity", homeCity);
        builder.addFormDataPart("homeState", homeState);
        builder.addFormDataPart("homeAddress", homeAddress);
        //familyMembersCount//家庭人数  addressType addressPostCode
        builder.addFormDataPart("familyMembersCount", familyMembersCount);
        builder.addFormDataPart("addressType", addressType);
        builder.addFormDataPart("addressPostCode", addressPostCode);
        if (pasthMap != null) {
            for (Map.Entry<String, String> f : pasthMap.entrySet()) {
                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===f==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue());

            }

        }
        if (sizeMap != null) {
            for (Map.Entry<String, String> f : sizeMap.entrySet()) {
                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===f==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue());

            }

        }

        if (scaleMap != null) {
            for (Map.Entry<String, String> f : scaleMap.entrySet()) {
                //    builder.addFormDataPart("multipartFile", f.getName(), requestBody);
                DebugLog.i("wang", "===f==" + f.getKey());
                builder.addFormDataPart(f.getKey(), f.getValue());

            }

        }

        RetrofitFileFactory.createFactoty().create(UserApi.class).uploadFamailyInfo(builder.build()).
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
