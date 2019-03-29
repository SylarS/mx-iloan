package com.panshi.hujin2.iloan.bean;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class RegitReq {

    /**
     * area : string
     * areaCode : string
     * password : string
     * phoneNumber : string
     * verificationCode : string
     */

    private String area;//地区
    private String areaCode;//区位号
    private String password;//密码
    private String phoneNumber;//手机号
    private String verificationCode;//验证码
    private String invitationCode; //邀请码

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
