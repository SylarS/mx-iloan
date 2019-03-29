package com.panshi.hujin2.iloan.presenter;

import android.text.TextUtils;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.LoginRes;
import com.panshi.hujin2.iloan.bean.RegitReq;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.MD5Util;
import com.panshi.hujin2.iloan.util.MediaTypeUtil;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.RegitView;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class SetRegitPasswdPresenter extends BasePresenter {
    public void regit(String area, String areaCode, String phoneNumber, String passwd, String verifyCode,String inviteCode) {
        RegitReq regitReq = new RegitReq();
        regitReq.setAreaCode(areaCode);
        regitReq.setArea(area);
        regitReq.setPassword(MD5Util.md5(passwd));
        regitReq.setPhoneNumber(phoneNumber);
        regitReq.setVerificationCode(verifyCode);
        if(!TextUtils.isEmpty(inviteCode)){
           regitReq.setInvitationCode(inviteCode);
        }
        DebugLog.i("wang","==areaCode=="+areaCode+"=area="+area+"=="+passwd+"=="+"==phoneNumber=="+phoneNumber+"==verifyCode=="+verifyCode+"==inviteCode="+inviteCode);
        DebugLog.i("wang","==gason=="+GonsonUtil.buildGosn().toJson(regitReq));
        RetrofitFactory.createFactoty().create(UserApi.class).regit(MediaTypeUtil.createJsonMediaType(GonsonUtil.buildGosn().toJson(regitReq))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<LoginRes>() {
            @Override
            public boolean test(@NonNull LoginRes loginRes) throws Exception {
                if (loginRes.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                if (isViewAttach()) {
                    ((BaseViewInterface) getAttach()).showFailture(loginRes.getCode(), loginRes.getMessage());
                }
                return false;
            }
        }).map(new Function<LoginRes, LoginRes.DataBean>() {
            @Override
            public LoginRes.DataBean apply(@NonNull LoginRes loginRes) throws Exception {

                return loginRes.getData();
            }
        }).subscribe(new CusumeObserver<LoginRes.DataBean>(getAttach()) {
            @Override
            public void onNext(@NonNull LoginRes.DataBean dataBean) {
                super.onNext(dataBean);
                AppStaus.userInfo = dataBean;
                if (isViewAttach()) {
                    ((RegitView) getAttach()).regitSucess();
                }

            }
        });
    }
}
