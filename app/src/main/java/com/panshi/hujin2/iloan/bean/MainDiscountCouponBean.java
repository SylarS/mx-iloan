package com.panshi.hujin2.iloan.bean;

public class MainDiscountCouponBean {
    private String beginDate;
    private String endDate;
    private String couponContent;
    private Double couponValue;
    private boolean isJump;
    private String expDescription;
    private int couponType;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCouponContent() {
        return couponContent;
    }

    public void setCouponContent(String couponContent) {
        this.couponContent = couponContent;
    }

    public Double getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(Double couponValue) {
        this.couponValue = couponValue;
    }

    public boolean isJump() {
        return isJump;
    }

    public void setJump(boolean jump) {
        isJump = jump;
    }


    public String getExpDescription() {
        return expDescription;
    }

    public void setExpDescription(String expDescription) {
        this.expDescription = expDescription;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    @Override
    public String toString() {
        return "MainDiscountCouponBean{" +
                "beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", couponContent='" + couponContent + '\'' +
                ", couponValue='" + couponValue + '\'' +
                ", isJump=" + isJump +
                ", expDescription='" + expDescription + '\'' +
                '}';
    }
}
