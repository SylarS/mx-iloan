package com.panshi.hujin2.iloan.view.interfaces;

import com.panshi.hujin2.iloan.bean.OrderDetailRes;
import com.panshi.hujin2.iloan.bean.OrderInfoRes;
import com.panshi.hujin2.iloan.bean.YearRateRes;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public interface IndexView {
    void callOrderInfo(OrderInfoRes.DataBean dataBeans);
    void callOrderDetail(OrderDetailRes.DataBean dataBean);
    void callYearRate(YearRateRes.DataBean dataBean);

}
