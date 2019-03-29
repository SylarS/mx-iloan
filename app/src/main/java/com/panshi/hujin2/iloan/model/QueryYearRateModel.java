package com.panshi.hujin2.iloan.model;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.YearRateRes;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/8/7 0007.
 */

public abstract class QueryYearRateModel implements BaseModel {
    public void queryYearRate(int days, final QueryYearRateInterface queryYearInterface) {
        RetrofitFactory.createFactoty().create(UserApi.class).queryYearRate(days).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<YearRateRes>() {
            @Override
            public boolean test(@NonNull YearRateRes yearRateRes) throws Exception {
                if (yearRateRes.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                return false;
            }
        }).map(new Function<YearRateRes, YearRateRes.DataBean>() {
            @Override
            public YearRateRes.DataBean apply(@NonNull YearRateRes yearRateRes) throws Exception {
                return yearRateRes.getData();
            }
        }).subscribe(new CusumeObserver<YearRateRes.DataBean>() {
            @Override
            public void onNext(@NonNull YearRateRes.DataBean dataBean) {
                super.onNext(dataBean);
                queryYearInterface.yearRate(dataBean);
            }
        });
    }
    public interface QueryYearRateInterface{
          void yearRate(YearRateRes.DataBean dataBean);
    }
}
