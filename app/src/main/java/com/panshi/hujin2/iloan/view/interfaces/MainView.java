package com.panshi.hujin2.iloan.view.interfaces;

import com.panshi.hujin2.iloan.bean.ActiveRes;
import com.panshi.hujin2.iloan.bean.MainDiscountCouponBean;
import com.panshi.hujin2.iloan.bean.MainDiscountCouponOuterBean;

/**
 * Created by Administrator on 2018/7/31 0031.
 */

public interface MainView {
    void callNoReadNews(int count);

    void resultActive(ActiveRes.DataBean dataBean);

    void setDiscountCoupon(MainDiscountCouponOuterBean mainDiscountCouponBean);

    void dismissInvitationDialog();

}
