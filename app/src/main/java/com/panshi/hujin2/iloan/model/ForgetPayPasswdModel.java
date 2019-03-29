package com.panshi.hujin2.iloan.model;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.RegitReq;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.MD5Util;
import com.panshi.hujin2.iloan.util.MediaTypeUtil;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public abstract class ForgetPayPasswdModel extends ProgessBaseModel {

    public void forget(String passwd, String vertify, String phoneNum,String areaCode, final ForgetPasswdCall forgetPasswdCall) {
        RegitReq regitReq = new RegitReq();
        regitReq.setArea("");
        regitReq.setPassword(MD5Util.md5(passwd));
        regitReq.setAreaCode(areaCode);
        regitReq.setVerificationCode(vertify);
        regitReq.setPhoneNumber(phoneNum);
        RetrofitFactory.createFactoty().create(UserApi.class).
                forgetPayPasswd(MediaTypeUtil.createJsonMediaType(GonsonUtil.buildGosn().toJson(regitReq))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                failture(response.getCode(),response.getMessage());

                return false;
            }
        }).subscribe(new CusumeObserver<Response>(this) {
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
                forgetPasswdCall.setPasswdSucess();

            }
        });
    }
    public interface ForgetPasswdCall{
         void setPasswdSucess();
    }
}
