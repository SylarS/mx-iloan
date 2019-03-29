package com.panshi.hujin2.iloan.presenter;


import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.model.UpdateLoginPasswdModel;
import com.panshi.hujin2.iloan.model.UpdatePayPasswdModel;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.UpdatePasswdView;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class UpdatePasswdPresenter extends BasePresenter {
    public void updatePasswd(int type, String oldPasswd, String newPasswd) {
        if (type == Constant.PAY_PASSWD) {
            new UpdatePayPasswdModel() {
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
            }.updatePayPasswd(oldPasswd, newPasswd, new UpdatePayPasswdModel.UpdatePayPasswd() {
                @Override
                public void updateSucess() {
                    if(isViewAttach()){
                        ((UpdatePasswdView)getAttach()).updateSucess();
                    }
                }
            });

        }else{
            new UpdateLoginPasswdModel() {
                @Override
                public void failture(int code, String message) {
                    if (isViewAttach()) {
                        ((BaseViewInterface) getAttach()).showFailture(code, message);
                    }
                }
            }.updateLoginPasswd(oldPasswd, newPasswd, new UpdateLoginPasswdModel.UpdateLoginPasswd() {
                @Override
                public void updateSucess() {
                    if(isViewAttach()){
                        ((UpdatePasswdView)getAttach()).updateSucess();
                    }
                }
            });
        }
    }
}
