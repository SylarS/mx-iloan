package com.panshi.hujin2.iloan.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/26 0026.
 */

public class UploadContact {

    private List<UserContactInputVOSBean> userContactInputVOS;

    public List<UserContactInputVOSBean> getUserContactInputVOS() {
        return userContactInputVOS;
    }

    public void setUserContactInputVOS(List<UserContactInputVOSBean> userContactInputVOS) {
        this.userContactInputVOS = userContactInputVOS;
    }

    public static class UserContactInputVOSBean {
        /**
         * contactGrade : 0
         * contactName : string
         * contactPhone : string
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
    }
}
