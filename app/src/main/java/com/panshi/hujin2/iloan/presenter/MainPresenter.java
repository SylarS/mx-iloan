package com.panshi.hujin2.iloan.presenter;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.ActiveRes;
import com.panshi.hujin2.iloan.bean.MainDiscountCouponOuterBean;
import com.panshi.hujin2.iloan.bean.NoReadNewsRes;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.model.ActiveInfoModel;
import com.panshi.hujin2.iloan.model.DevicesModel;
import com.panshi.hujin2.iloan.model.DiscountCouponDialogModel;
import com.panshi.hujin2.iloan.model.InvationCodeModel;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.MainView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class MainPresenter extends BasePresenter {

    /**
     * 获取未读消息
     */
    public void getNoReadzNews() {

        int userId;
        if (AppStaus.userInfo != null) {
            userId = AppStaus.userInfo.getId();
        } else {
            /*     userId= (int) sharePrefrencesModel.query(Constant.USER_ID_KEY);*/
            return;
        }
        RetrofitFactory.createFactoty().create(UserApi.class).getNoRead().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<NoReadNewsRes>() {
            @Override
            public boolean test(@NonNull NoReadNewsRes noReadNewsRes) throws Exception {
                if (noReadNewsRes.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                if (isViewAttach()) {
                    ((BaseViewInterface) getAttach()).showFailture(noReadNewsRes.getCode(), noReadNewsRes.getMessage());
                }
                return false;
            }
        }).subscribe(new Observer<NoReadNewsRes>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull NoReadNewsRes noReadNewsRes) {
                if (isViewAttach()) {
                    ((MainView) getAttach()).callNoReadNews(noReadNewsRes.getData());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 获取活动信息
     */
    public void getActiveInfo() {
        new ActiveInfoModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((BaseViewInterface) getAttach()).showFailture(code, message);
                }
            }
        }.getActivieInfo(new ActiveInfoModel.ResultActive() {
            @Override
            public void resultActive(ActiveRes.DataBean dataBean) {
                if (isViewAttach()) {
                    ((MainView) getAttach()).resultActive(dataBean);
                }
            }
        });
    }

    /**
     * 上传验证码
     *
     * @param invateCode
     */
    public void postInviteCode(String invateCode) {
        new InvationCodeModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((BaseViewInterface) getAttach()).showFailture(code, message); //输入错误一直让他输入
                }
            }
        }.postInvitionCode(invateCode, new InvationCodeModel.InvitationSuccess() {
            @Override
            public void invitationSuccess() {
                ((MainView) getAttach()).dismissInvitationDialog();
            }
        });
    }

    public void getDiscountCouponStatus() {
        new DiscountCouponDialogModel().
                showDialog(new DiscountCouponDialogModel.SetDiscountCouponInterface() {
                    @Override
                    public void setDiscountCouponModle(MainDiscountCouponOuterBean mainDiscountCouponBean) {
                        ((MainView) getAttach()).setDiscountCoupon(mainDiscountCouponBean);
                    }
                });
    }

    public void sendDevice() {
        new DevicesModel().
                sendDevice(new DevicesModel.SendDeviceInfoInterface() {
                    @Override
                    public void sendDeviceInfo(Response response) {

                    }
                });
    }
}
