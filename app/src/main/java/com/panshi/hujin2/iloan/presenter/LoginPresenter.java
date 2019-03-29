package com.panshi.hujin2.iloan.presenter;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.LoginReq;
import com.panshi.hujin2.iloan.bean.LoginRes;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.model.CheckRegitModel;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.MD5Util;
import com.panshi.hujin2.iloan.util.MediaTypeUtil;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.LoginView;
import com.panshi.hujin2.iloan.view.interfaces.RegitView;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public class LoginPresenter extends BasePresenter {
    public void login(String name, String passwd, String areaCode) {
        final LoginReq loginReq = new LoginReq();
        loginReq.setPassword(MD5Util.md5(passwd));
        loginReq.setPhoneNumber(name);
        loginReq.setArea("");
        loginReq.setAreaCode(areaCode);
        RetrofitFactory.createFactoty().create(UserApi.class).
                login(MediaTypeUtil.createJsonMediaType(GonsonUtil.buildGosn().toJson(loginReq))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<LoginRes>() {
            @Override
            public boolean test(@NonNull LoginRes loginRes) throws Exception {
                if (loginRes.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                if(isViewAttach()){
                    ((BaseViewInterface)getAttach()).showFailture(loginRes.getCode(),loginRes.getMessage());
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
                AppStaus.userInfo = dataBean;
                super.onNext(dataBean);
                if(isViewAttach()){
                    ((LoginView)getAttach()).loginSucess();
                }
            }
        });
    }
    public void checkRegit(String areaCode, String phoneNum) {
        new CheckRegitModel() {
            @Override
            public void failture(int code, String message) {
                if(isViewAttach()){
                    ((BaseViewInterface)getAttach()).showFailture(code,message);
                }
            }
        }.checkRegit(phoneNum, areaCode, new CheckRegitModel.CheckRegitModelInterface() {
            @Override
            public void regited() {
                if(isViewAttach()){
                    ((LoginView)getAttach()).regited();
                }
            }

            @Override
            public void noRegit() {
                if(isViewAttach()){
                    ((LoginView)getAttach()).noRegit();
                }
            }
        });
    }

}
