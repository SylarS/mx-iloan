package com.panshi.hujin2.iloan.model;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.LoginRes;
import com.panshi.hujin2.iloan.bean.RegitReq;
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

public abstract class ForgetLoginPasswdModel extends ProgessBaseModel {
    public void forget(String passwd, String vertify, String phoneNum, String areaCode,final ForgetPasswdCall forgetPasswdCall) {
        RegitReq regitReq = new RegitReq();
        regitReq.setArea("");
        regitReq.setPassword(MD5Util.md5(passwd));
        regitReq.setVerificationCode(vertify);
        regitReq.setPhoneNumber(phoneNum);
        regitReq.setAreaCode(areaCode);
        RetrofitFactory.createFactoty().create(UserApi.class).
                forgetLogin(MediaTypeUtil.createJsonMediaType(GonsonUtil.buildGosn().toJson(regitReq))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<LoginRes>() {
            @Override
            public boolean test(@NonNull LoginRes response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                failture(response.getCode(),response.getMessage());

                return false;
            }
        }).subscribe(new CusumeObserver<LoginRes>(this) {
            @Override
            public void onNext(@NonNull LoginRes response) {
                super.onNext(response);
                AppStaus.userInfo=response.getData();
                forgetPasswdCall.setPasswdSucess();

            }
        });
    }
    public interface ForgetPasswdCall{
         void setPasswdSucess();
    }
}
