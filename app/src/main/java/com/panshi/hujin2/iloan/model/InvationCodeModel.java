package com.panshi.hujin2.iloan.model;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public abstract class InvationCodeModel implements BaseModel {
    /**
     * 注册邀请码
     *
     * @param inviteCode
     */
    public void postInvitionCode(String inviteCode, final InvitationSuccess invitationSuccess) {
        RetrofitFactory.createFactoty().create(UserApi.class).postInviteCode(inviteCode).subscribeOn(Schedulers.io()).filter(new Predicate<Response>() {
            @Override
            public boolean test(Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                failture(response.getCode(), response.getMessage());
                return false;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new CusumeObserver<Response>() {
            @Override
            public void onNext(Response response) {
                super.onNext(response);
                invitationSuccess.invitationSuccess();
                DebugLog.i("====邀请上传成功====");
            }
        });
    }

    public interface InvitationSuccess {
        void invitationSuccess();
    }
}
