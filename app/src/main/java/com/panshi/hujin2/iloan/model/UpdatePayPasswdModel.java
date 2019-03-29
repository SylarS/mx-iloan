package com.panshi.hujin2.iloan.model;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.bean.UpdatePassReq;
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

public abstract class UpdatePayPasswdModel extends ProgessBaseModel {
    public void updatePayPasswd(String oldPw, String newPw, final UpdatePayPasswd updatePayPasswd) {
        UpdatePassReq updatePassReq = new UpdatePassReq();
        updatePassReq.setOldPassword(MD5Util.md5(oldPw));
       // updatePassReq.setOldPassword(/*MD5Util.md5(*/oldPw));
        updatePassReq.setPassword(MD5Util.md5(newPw));
        RetrofitFactory.createFactoty().create(UserApi.class).
                updatePayPasswd(MediaTypeUtil.createJsonMediaType(GonsonUtil.buildGosn().toJson(updatePassReq))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                failture(response.getCode(), response.getMessage());
                return false;
            }
        }).subscribe(new CusumeObserver<Response>(){
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
                updatePayPasswd.updateSucess();
            }
        });
    }
    public  interface UpdatePayPasswd{
        void updateSucess();
    }

}
