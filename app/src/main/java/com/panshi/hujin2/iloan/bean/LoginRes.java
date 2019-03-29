package com.panshi.hujin2.iloan.bean;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public class LoginRes {


    /**
     * code : 0
     * data : {"birthday":"2018-07-23T09:28:42.399Z","birthplace":"string","createTime":"2018-07-23T09:28:42.399Z","educationBackground":0,"email":"string","gender":0,"id":0,"idNumber":"string","loanPurpose":0,"maritalStatus":0,"modifyTime":"2018-07-23T09:28:42.400Z","phoneNumber":"string","token":"string","username":"string"}
     * message : string
     * page : {"currentPage":0,"dbIndex":0,"dbNumber":0,"pageSize":0,"totalNumber":0,"totalPage":0}
     * success : true
     */

    private int code;
    private DataBean data;
    private String message;
    private PageBean page;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * birthday : 2018-07-23T09:28:42.399Z
         * birthplace : string
         * createTime : 2018-07-23T09:28:42.399Z
         * educationBackground : 0
         * email : string
         * gender : 0
         * id : 0
         * idNumber : string
         * loanPurpose : 0
         * maritalStatus : 0
         * modifyTime : 2018-07-23T09:28:42.400Z
         * phoneNumber : string
         * token : string
         * username : string
         */

        private String birthday;
        private String birthplace;
        private String createTime;
        private int educationBackground;
        private String email;
        private int gender;
        private int id;
        private String idNumber;
        private int loanPurpose;
        private int maritalStatus;
        private String modifyTime;
        private String phoneNumber;
        private String token;
        private String username;
        private String areaCode;
        public  String longitude="";
        public  String latitude="";
        public  String mac="";
        public String vest="";

        public String getVest() {
            return vest;
        }

        public void setVest(String vest) {
            this.vest = vest;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public  String getLongitude() {
            return longitude;
        }

        public  void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public  String getLatitude() {
            return latitude;
        }

        public  void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBirthplace() {
            return birthplace;
        }

        public void setBirthplace(String birthplace) {
            this.birthplace = birthplace;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getEducationBackground() {
            return educationBackground;
        }

        public void setEducationBackground(int educationBackground) {
            this.educationBackground = educationBackground;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public int getLoanPurpose() {
            return loanPurpose;
        }

        public void setLoanPurpose(int loanPurpose) {
            this.loanPurpose = loanPurpose;
        }

        public int getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(int maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }

    public static class PageBean {
        /**
         * currentPage : 0
         * dbIndex : 0
         * dbNumber : 0
         * pageSize : 0
         * totalNumber : 0
         * totalPage : 0
         */

        private int currentPage;
        private int dbIndex;
        private int dbNumber;
        private int pageSize;
        private int totalNumber;
        private int totalPage;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getDbIndex() {
            return dbIndex;
        }

        public void setDbIndex(int dbIndex) {
            this.dbIndex = dbIndex;
        }

        public int getDbNumber() {
            return dbNumber;
        }

        public void setDbNumber(int dbNumber) {
            this.dbNumber = dbNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalNumber() {
            return totalNumber;
        }

        public void setTotalNumber(int totalNumber) {
            this.totalNumber = totalNumber;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }
    }
}
