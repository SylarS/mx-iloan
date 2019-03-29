package com.panshi.hujin2.iloan.presenter;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.model.CheckBindCardModel;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.SetView;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class SetPresenter extends BasePresenter {
    /**
     * 检查是否设置支付密码
     * @param type
     */
    public void checkPayPasswd(final int type){
         new CheckBindCardModel() {
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
         }.checkBindCard(new CheckBindCardModel.CheckBindCardModelInterface() {
             @Override
             public void bindCardSucess() {
                 if(isViewAttach()){
                     ((SetView)getAttach()).checkBindCardResult(type);
                 }
             }

             @Override
             public void bindCardFailture() {
                 if(isViewAttach()){
                     ((SetView)getAttach()).noBindCardResult();
                 }
             }
         });
    }

    public void logout() {
        RetrofitFactory.createFactoty().create(UserApi.class).LoginOut().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<Response>() {
            @Override
            public boolean test(@NonNull Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                if (isViewAttach()) {
                    ((SetView) getAttach()).LogoutSucess();
                }
                return false;
            }
        }).subscribe(new CusumeObserver<Response>(this) {
            @Override
            public void onNext(@NonNull Response response) {
                super.onNext(response);
                if (isViewAttach()) {
                    ((SetView) getAttach()).LogoutSucess();
                }
            }
        });
    }
}
