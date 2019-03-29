package com.panshi.hujin2.iloan.model;


import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.MainDiscountCouponOuterBean;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DiscountCouponDialogModel implements BaseModel {
    @Override
    public void failture(int code, String message) {

    }

    public void showDialog(final SetDiscountCouponInterface setDiscountCouponInterface) {

        RetrofitFactory.
                createFactoty().
                create(UserApi.class).
                getDiscountCoupon().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe( new CusumeObserver<MainDiscountCouponOuterBean>(){
                    @Override
                    public void onNext(MainDiscountCouponOuterBean mainDiscountCouponOuterBean) {
                        super.onNext(mainDiscountCouponOuterBean);
                        setDiscountCouponInterface.setDiscountCouponModle(mainDiscountCouponOuterBean);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    public interface SetDiscountCouponInterface {
        public void setDiscountCouponModle(MainDiscountCouponOuterBean mainDiscountCouponBean);
    }
}
