package com.panshi.hujin2.iloan.model;

import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.SetView;

/**
 * Created by Administrator on 2018/7/26 0026.
 */

public class SettingPresenter extends BasePresenter {
    public void checkBindCard(final int type) {
        new CheckBindCardModel() {
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

}
