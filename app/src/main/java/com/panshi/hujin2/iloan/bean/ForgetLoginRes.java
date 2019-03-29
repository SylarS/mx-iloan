package com.panshi.hujin2.iloan.bean;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class ForgetLoginRes {
    //"区号"
    private String areaCode;
    //"手机号码"
    private String phoneNumber;
    //"验证码"
    private String verificationCode;
    //"新的密码"
    private String password;
    //"地点"
    private String area;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
