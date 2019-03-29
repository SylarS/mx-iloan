package com.panshi.hujin2.iloan.bean;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class UpdatePassReq {

    /**
     * oldPassword : string
     * password : string
     */

    private String oldPassword;
    private String password;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
