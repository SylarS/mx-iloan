package com.panshi.hujin2.iloan.model;

import android.text.TextUtils;

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
 * Created by Administrator on 2018/7/26 0026.
 */

public abstract class CheckBindCardModel extends ProgessBaseModel{
    public void checkBindCard(final CheckBindCardModelInterface checkBindCardModelInterface){

        RetrofitFactory.createFactoty().create(UserApi.class).checkBindCard().
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                if(response.getCode()== Constant.SUCESS_CODE){
                    return true;
                }
                failture(response.getCode(),response.getMessage());
                return false;
            }
        }).subscribe(new CusumeObserver<Response>(this){
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
                if(!TextUtils.isEmpty(response.getData())){
                    if(response.getData().equals("true")){
                        checkBindCardModelInterface.bindCardSucess();
                    }else {
                        checkBindCardModelInterface.bindCardFailture();
                    }
                }
            }
        });
    }

    public  interface CheckBindCardModelInterface{
        void bindCardSucess();
        void bindCardFailture();
    }
}
