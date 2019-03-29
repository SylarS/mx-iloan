package com.panshi.hujin2.iloan.presenter;

import android.text.TextUtils;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.LoginRes;
import com.panshi.hujin2.iloan.bean.RegitReq;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.model.CheckRegitModel;
import com.panshi.hujin2.iloan.model.CheckRegitWihtProDialogModel;
import com.panshi.hujin2.iloan.model.ContactModel;
import com.panshi.hujin2.iloan.model.VertifyCodeModel;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.MD5Util;
import com.panshi.hujin2.iloan.util.MediaTypeUtil;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.PhoneView;
import com.panshi.hujin2.iloan.view.interfaces.RegitView;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.panshi.hujin2.iloan.Constant.VERTIFY_REGIT;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class RegitPresenter extends BasePresenter {
    public void getVerifyCode(String areaCode,String phoneNum) {
       new VertifyCodeModel() {
           @Override
           public void showDialoging() {

           }

           @Override
           public void hiddenDialoging() {

           }

           @Override
           public void failture(int code, String message) {
               if (isViewAttach()) {
                   ((BaseViewInterface) getAttach()).showFailture(code, message);
               }
           }
       }.getVertifyCode(new VertifyCodeModel.VertifyCode() {
           @Override
           public void getVertifySucess() {
             // doCountDwon(60);
           }
       }, phoneNum, areaCode, VERTIFY_REGIT);

    }

    public void checkVertifyCode(String areaCode, String phoneNum, String vertifycode){
        new VertifyCodeModel() {
            @Override
            public void showDialoging() {
                if(isViewAttach()){
                    ((BaseViewInterface)getAttach()).showProgessDialog();
                }
            }

            @Override
            public void hiddenDialoging() {
                 if(isViewAttach()){
                     ((BaseViewInterface)getAttach()).hiddenProgessDialog();
                 }
            }

            @Override
            public void failture(int code, String message) {
                if(isViewAttach()){
                    ((BaseViewInterface)getAttach()).showFailture(code,message);
                }
            }
        }.checkVertifyCode(new VertifyCodeModel.VertifyCodeCheck() {

            @Override
            public void checkVertifySucess() {
               if(isViewAttach()){
                   ((RegitView)getAttach()).checkVertifyCodeSucess();
               }
            }
        },areaCode,phoneNum,vertifycode,VERTIFY_REGIT);
    }




    /**
     * 倒计时
     */
    public void doCountDwon(final long time) {
        Observable.interval(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).map(new Function<Long, Long>() {
            @Override
            public Long apply(@NonNull Long aLong) throws Exception {

                DebugLog.i("wang", "==time==" + time + "||" + aLong);
                return time - aLong;
            }
        }).take(time + 1).subscribe(new CusumeObserver<Long>() {
            @Override
            public void onNext(@NonNull Long aLong) {
                super.onNext(aLong);
                DebugLog.e("wang", "===倒计时==" + aLong);
                if (isViewAttach()) {
                    ((RegitView) getAttach()).yanma(aLong);
                }
            }
        });

    }


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
                    ((RegitView)getAttach()).regited();
                }
            }

            @Override
            public void noRegit() {
                if(isViewAttach()){
                    ((RegitView)getAttach()).noRegit();
                }
            }
        });
    }
    public void checkRegitWithDialog(String areaCode, String phoneNum) {
        new CheckRegitWihtProDialogModel() {
            @Override
            public void showDialoging() {
                 if(isViewAttach()){
                     ((BaseViewInterface)getAttach()).showProgessDialog();
                 }
            }

            @Override
            public void hiddenDialoging() {
                ((BaseViewInterface)getAttach()).hiddenProgessDialog();
            }

            @Override
            public void failture(int code, String message) {
                if(isViewAttach()){
                    ((BaseViewInterface)getAttach()).showFailture(code,message);
                }
            }
        }.checkRegit(phoneNum, areaCode, new CheckRegitWihtProDialogModel.CheckRegitModelInterface() {
            @Override
            public void regited() {
                if(isViewAttach()){
                    ((RegitView)getAttach()).regited();
                }
            }

            @Override
            public void noRegit() {
                if(isViewAttach()){
                    ((RegitView)getAttach()).noRegitFouse();
                }
            }
        });
    }
    /**
     * 上传短息数量
      * @param num
     */
    public void uploadSmsNum(int num){
        new ContactModel() {
            @Override
            public void failture(int code, String message) {
                if(isViewAttach()){
                    ((BaseViewInterface)getAttach()).showFailture(code,message);
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
                DebugLog.i("wang","===sms==sucess==");
            }
        });
    }
}
