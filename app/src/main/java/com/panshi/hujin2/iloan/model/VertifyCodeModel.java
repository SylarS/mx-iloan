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
 * Created by Administrator on 2018/7/24 0024.
 */

public abstract class VertifyCodeModel extends ProgessBaseModel {

    public void getVertifyCode(final VertifyCode vertifyCode,String phoneNum,String areaCode,int type) {
        RetrofitFactory.createFactoty().create(UserApi.class).getVertifyCode(areaCode,phoneNum,type).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                failture(response.getCode(), response.getMessage());
                return false;
            }
        }).subscribe(new CusumeObserver<Response>(this) {
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
              //  failture(response.getCode(),response.getData());
                vertifyCode.getVertifySucess();
            }
        });
    }

    /**
     * 校验短信
     * @param vertifyCodeCheck
     * @param areaCode
     * @param phoneNum
     * @param vertifycode
     * @param type
     */
    public void checkVertifyCode(final VertifyCodeCheck vertifyCodeCheck, String areaCode, String phoneNum, String vertifycode, int type){
      RetrofitFactory.createFactoty().create(UserApi.class).checkVertifyCode(areaCode,phoneNum,vertifycode,type).
              subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
          @Override
          public boolean test(@NonNull Response response) throws Exception {
              if(response.getCode()==Constant.SUCESS_CODE){
               return true;
              }
              failture(response.getCode(),response.getMessage());
              return false;
          }
      }).subscribe(new CusumeObserver<Response>(this){
          @Override
          public void onNext(@NonNull Response response) {
              super.onNext(response);
              vertifyCodeCheck.checkVertifySucess();
          }
      });
    }

    public interface VertifyCode {
        void getVertifySucess();
    }
    public interface VertifyCodeCheck{
        void checkVertifySucess();
    }
}
