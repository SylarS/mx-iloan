package com.panshi.hujin2.iloan.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/10 0010.
 */

public class CallRecordReq {

    private List<UserCallRecordInputVOSBean> userCallRecordInputVOS;

    public List<UserCallRecordInputVOSBean> getUserCallRecordInputVOS() {
        return userCallRecordInputVOS;
    }

    public void setUserCallRecordInputVOS(List<UserCallRecordInputVOSBean> userCallRecordInputVOS) {
        this.userCallRecordInputVOS = userCallRecordInputVOS;
    }

    public static class UserCallRecordInputVOSBean {
        /**
         * contactName : string
         * contactPhone : string
         * recordType : 0
         * startTime : 2018-08-10T05:59:50.246Z
         * time : 0
         * userId : 0
         */

        private String contactName;
        private String contactPhone;
        private int recordType;
        private String startTime;
        private int time;
        private int userId;

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

        public int getRecordType() {
            return recordType;
        }

        public void setRecordType(int recordType) {
            this.recordType = recordType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
