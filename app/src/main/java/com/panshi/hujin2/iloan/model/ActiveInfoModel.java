package com.panshi.hujin2.iloan.model;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.ActiveRes;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public abstract class ActiveInfoModel implements BaseModel {

    public void getActivieInfo(final ResultActive resultActive){
        RetrofitFactory.createFactoty().create(UserApi.class).getActiveInfo().subscribeOn(Schedulers.io()).filter(new Predicate<ActiveRes>() {
            @Override
            public boolean test(ActiveRes activeRes) throws Exception {
                if(activeRes.getCode()==Constant.SUCESS_CODE){
                    return true;
                }
                return false;
            }
        }).map(new Function<ActiveRes,ActiveRes.DataBean>() {
            @Override
            public ActiveRes.DataBean apply(ActiveRes actiiveRes) throws Exception {
                if(actiiveRes!=null&&actiiveRes.getData()!=null&&actiiveRes.getData().size()>0){
                    return actiiveRes.getData().get(0);
                }
                failture(0,"actiiveRes is null");
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new CusumeObserver<ActiveRes.DataBean>(){
            @Override
            public void onNext(ActiveRes.DataBean dataBean) {
                super.onNext(dataBean);
                if(dataBean!=null){
                    resultActive.resultActive(dataBean);
                }
            }
        });
    }

    public  interface  ResultActive{
        public void resultActive(ActiveRes.DataBean dataBean);
    }
}
