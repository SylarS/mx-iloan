package com.panshi.hujin2.iloan.presenter;

import com.panshi.hujin2.iloan.model.CheckRegitModel;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.PhoneView;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class PhoneNumPresenter extends BasePresenter {
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
                  ((PhoneView)getAttach()).regited();
              }
            }

            @Override
            public void noRegit() {
                if(isViewAttach()){
                    ((PhoneView)getAttach()).noRegit();
                }
            }
        });
    }
}
