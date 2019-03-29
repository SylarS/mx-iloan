package com.panshi.hujin2.iloan.model;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/25 0025.
 */

public abstract class CheckRegitModel implements BaseModel {
    public void checkRegit(String phoneNum, String area, final CheckRegitModelInterface checkRegitModelInterface) {
        RetrofitFactory.createFactoty().create(UserApi.class).
                checkRegit(area, phoneNum).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                if (response.getCode() == Constant.REGIT_CODE) {
                    checkRegitModelInterface.regited();
                    return false;
                } else {
                    failture(response.getCode(), response.getMessage());
                }
                return false;
            }
        }).subscribe(new CusumeObserver<Response>(CheckRegitModel.this) {
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
                checkRegitModelInterface.noRegit();

            }
        });

    }

    public interface CheckRegitModelInterface {
        //注册过
        void regited();

        //没有注册
        void noRegit();
    }
}
