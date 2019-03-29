package com.panshi.hujin2.iloan.bean;

/**
 * Created by Administrator on 2018/7/25 0025.
 */

public class UserContactInputVOS {

    /**紧急联系人
     * contactGrade : 0
     * contactName : string 姓名
     * contactPhone : string 电话
     */

    private int contactGrade;
    private String contactName;
    private String contactPhone;

    public int getContactGrade() {
        return contactGrade;
    }

    public void setContactGrade(int contactGrade) {
        this.contactGrade = contactGrade;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    @Override
    public boolean equals(Object obj) {
        //DebugLog.i("wang","==name=="+obj.getClass().getSimpleName());
        if (obj!=null&&obj.getClass().getSimpleName().equals("userContactInputVOS")) {
            UserContactInputVOS userContactInputVOS = (UserContactInputVOS) obj;
            if (userContactInputVOS.getContactName().equals(contactName) && userContactInputVOS.getContactPhone().equals(contactPhone)) {
                return true;
            }
        }
        return false;
    }
}
