package com.panshi.hujin2.iloan.presenter;

import com.panshi.hujin2.iloan.bean.OrderDetailRes;
import com.panshi.hujin2.iloan.bean.OrderInfoRes;
import com.panshi.hujin2.iloan.bean.YearRateRes;
import com.panshi.hujin2.iloan.model.QueOderModel;
import com.panshi.hujin2.iloan.model.QueryYearRateModel;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.interfaces.IndexView;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class IndexPresenter extends BasePresenter {
    public void queryYearRate(int days){
        new QueryYearRateModel() {
            @Override
            public void failture(int code, String message) {
                if(isViewAttach()){
                    ((BaseViewInterface)getAttach()).showFailture(code,message);
                }
            }
        }.queryYearRate(days, new QueryYearRateModel.QueryYearRateInterface() {
            @Override
            public void yearRate(YearRateRes.DataBean dataBean) {
                    if(isViewAttach()){
                        ((IndexView)getAttach()).callYearRate(dataBean);
                    }
            }
        });
    }
    public void queryOderInfo() {
        new QueOderModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((BaseViewInterface) getAttach()).showFailture(code, message);
                }
            }
        }.queryLastLyOrder(new QueOderModel.QueOderModelInterface() {
            @Override
            public void callOderInfo(OrderInfoRes.DataBean orderInfo) {
                if (isViewAttach()) {
                    if (isViewAttach()) {
                        ((IndexView) getAttach()).callOrderInfo(orderInfo);
                    }
                }
            }

            @Override
            public void callOderDetail(OrderDetailRes.DataBean orderDetail) {

            }
        });
    }

    public void QueryOrderDetailInfo(int orderId) {
        new QueOderModel() {
            @Override
            public void failture(int code, String message) {
                if (isViewAttach()) {
                    ((BaseViewInterface) getAttach()).showFailture(code, message);
                }
            }
        }.queryOderDetail(orderId, new QueOderModel.QueOderModelInterface() {

            @Override
            public void callOderInfo(OrderInfoRes.DataBean orderInfo) {

            }

            @Override
            public void callOderDetail(OrderDetailRes.DataBean orderDetail) {
                if (isViewAttach()) {
                    ((IndexView) getAttach()).callOrderDetail(orderDetail);
                }
            }
        });
    }


}
