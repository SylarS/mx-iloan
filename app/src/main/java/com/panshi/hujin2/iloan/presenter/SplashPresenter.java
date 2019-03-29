package com.panshi.hujin2.iloan.presenter;

import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.view.interfaces.SplashView;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public class SplashPresenter extends BasePresenter {
    /**
     * 倒计时
     */
    public void doCountDwon(final long time) {
        Observable.interval(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).
                map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        DebugLog.i("wang", "==time==" + time + "||" + aLong);
                        return time - aLong;
                    }
                }).
                take(time + 1).
                subscribe(new CusumeObserver<Long>() {
                    @Override
                    public void onNext(@NonNull Long aLong) {
                        super.onNext(aLong);
                        DebugLog.e("wang", "===倒计时==" + aLong);
                        if (isViewAttach()) {
                            ((SplashView) getAttach()).splash(aLong);
                        }
                    }
                });

    }
}
