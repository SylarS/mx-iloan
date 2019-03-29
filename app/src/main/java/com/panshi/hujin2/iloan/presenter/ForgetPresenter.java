package com.panshi.hujin2.iloan.presenter;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.model.ForgetLoginPasswdModel;
import com.panshi.hujin2.iloan.model.ForgetPayPasswdModel;
import com.panshi.hujin2.iloan.model.VertifyCodeModel;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.ForgetView;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class ForgetPresenter extends BasePresenter {

    public void getVertifyCode(String phoneNum,String areaCode,int vertifyType) {
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
                if (isViewAttach()) {
                //    ((ForgetView) getAttach()).setPasswdSucess();

                }
            }
        },phoneNum,areaCode,vertifyType);
    }

    public void forget(int type,String passwd, String vertify, String phoneNum,String areaCode) {
        if(type==Constant.PAY_PASSWD){
            new ForgetPayPasswdModel() {
                @Override
                public void showDialoging() {
                    if (isViewAttach()) {
                        ((BaseViewInterface) getAttach()).showProgessDialog();
                    }
                }

                @Override
                public void hiddenDialoging() {
                    if (isViewAttach()) {
                        ((BaseViewInterface) getAttach()).hiddenProgessDialog();
                    }
                }

                @Override
                public void failture(int code, String message) {
                    if (isViewAttach()) {
                        ((BaseViewInterface) getAttach()).showFailture(code, message);
                    }
                }
            }.forget(passwd, vertify, phoneNum, areaCode,new ForgetPayPasswdModel.ForgetPasswdCall() {
                @Override
                public void setPasswdSucess() {
                    if (isViewAttach()) {
                        ((ForgetView) getAttach()).setPasswdSucess();
                    }
                }
            });
        }else{
            new ForgetLoginPasswdModel() {
                @Override
                public void showDialoging() {
                    if (isViewAttach()) {
                        ((BaseViewInterface) getAttach()).showProgessDialog();
                    }
                }

                @Override
                public void hiddenDialoging() {
                    if (isViewAttach()) {
                        ((BaseViewInterface) getAttach()).hiddenProgessDialog();
                    }
                }

                @Override
                public void failture(int code, String message) {
                    if (isViewAttach()) {
                        ((BaseViewInterface) getAttach()).showFailture(code, message);
                    }
                }
            }.forget(passwd, vertify, phoneNum,areaCode, new ForgetLoginPasswdModel.ForgetPasswdCall() {
                @Override
                public void setPasswdSucess() {
                    if (isViewAttach()) {
                        ((ForgetView) getAttach()).setPasswdSucess();
                    }
                }
            });
        }

    }

}
