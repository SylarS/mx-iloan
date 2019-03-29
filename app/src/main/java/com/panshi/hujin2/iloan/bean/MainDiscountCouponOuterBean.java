package com.panshi.hujin2.iloan.bean;

import java.util.ArrayList;

public class MainDiscountCouponOuterBean {
    int code;
    boolean success;

    ArrayList<MainDiscountCouponBean> data = new ArrayList<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<MainDiscountCouponBean> getArrayList() {
        return data;
    }

    public void setArrayList(ArrayList<MainDiscountCouponBean> arrayList) {
        this.data = arrayList;
    }


    @Override
    public String toString() {
        return "MainDiscountCouponOuterBean{" +
                "code=" + code +
                ", success=" + success +
                ", arrayList=" + data +
                '}';
    }
}
