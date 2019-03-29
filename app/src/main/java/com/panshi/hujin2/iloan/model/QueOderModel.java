package com.panshi.hujin2.iloan.model;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.OrderDetailRes;
import com.panshi.hujin2.iloan.bean.OrderInfoRes;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 查询最新一条订单
 * Created by Administrator on 2018/7/30 0030.
 */

public abstract class QueOderModel implements BaseModel {
    /**
     * 查询最新一条订单
     */
    public void queryLastLyOrder(final QueOderModelInterface queryLastLyOrder) {
        RetrofitFactory.createFactoty().create(UserApi.class).queryOderInfo().
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<OrderInfoRes>() {
            @Override
            public boolean test(@NonNull OrderInfoRes orderInfoRes) throws Exception {
                if (orderInfoRes.getCode() == Constant.SUCESS_CODE) {
                    if(orderInfoRes.getData()!=null){
                        return true;
                    }else{
                        queryLastLyOrder.callOderInfo(null);
                        return false;
                    }

                }
                failture(orderInfoRes.getCode(), orderInfoRes.getMessage());
                return false;
            }
        }).map(new Function<OrderInfoRes, OrderInfoRes.DataBean>() {
            @Override
            public OrderInfoRes.DataBean apply(@NonNull OrderInfoRes orderInfoRes) throws Exception {
                return orderInfoRes.getData();
            }
        }).subscribe(new CusumeObserver<OrderInfoRes.DataBean>() {
            @Override
            public void onNext(@NonNull OrderInfoRes.DataBean orderInfoRes) {
                super.onNext(orderInfoRes);
                queryLastLyOrder.callOderInfo(orderInfoRes);
            }
        });
    }

    public void queryOderDetail(int orderId ,final QueOderModelInterface queryLastLyOrder) {
        RetrofitFactory.createFactoty().create(UserApi.class).queryOderDetailInfo(orderId).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<OrderDetailRes>() {
            @Override
            public boolean test(@NonNull OrderDetailRes orderDetailRes) throws Exception {
                if(orderDetailRes.getCode()==Constant.SUCESS_CODE){
                    return true;
                }
                failture(orderDetailRes.getCode(),orderDetailRes.getMessage());
                return false;
            }
        }).map(new Function<OrderDetailRes, OrderDetailRes.DataBean>() {
            @Override
            public OrderDetailRes.DataBean apply(@NonNull OrderDetailRes orderDetailRes) throws Exception {
                return orderDetailRes.getData();
            }
        }).subscribe(new CusumeObserver<OrderDetailRes.DataBean>(){
            @Override
            public void onNext(@NonNull OrderDetailRes.DataBean dataBean) {
                super.onNext(dataBean);
                queryLastLyOrder.callOderDetail(dataBean);
            }
        });
    }

    public interface QueOderModelInterface {

        void callOderInfo(OrderInfoRes.DataBean orderInfo);
        void callOderDetail(OrderDetailRes.DataBean orderDetail);

    }
}
