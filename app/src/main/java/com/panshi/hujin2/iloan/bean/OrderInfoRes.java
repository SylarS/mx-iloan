package com.panshi.hujin2.iloan.bean;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class OrderInfoRes {

    /**
     * code : 0
     * data : {"applyMoney":0,"applyMonth":0,"createTime":"2018-07-30T09:15:33.701Z","id":0,"lendRate":0,"loanDays":0,"modifyTime":"2018-07-30T09:15:33.701Z","orderNo":"string","status":0,"userId":0,"userIdNo":"string","userName":"string","userPhone":"string"}
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
         * applyMoney : 0
         * applyMonth : 0
         * createTime : 2018-07-30T09:15:33.701Z
         * id : 0
         * lendRate : 0
         * loanDays : 0
         * modifyTime : 2018-07-30T09:15:33.701Z
         * orderNo : string
         * status : 0
         * userId : 0
         * userIdNo : string
         * userName : string
         * userPhone : string
         */

        private double applyMoney;
        private int applyMonth;
        private String createTime;
        private int id;
        private double lendRate;
        private int loanDays;
        private String modifyTime;
        private String orderNo;
        private int status;
        private int userId;
        private String userIdNo;
        private String userName;
        private String userPhone;
        private double auditMoney; //可提现额度
        private boolean hasDue;//是否逾期
        private double cat; //年度总费用
        private double yearRate;
        private int expireRejectDays;//剩余被拒天数

        public int getExpireRejectDays() {
            return expireRejectDays;
        }

        public void setExpireRejectDays(int expireRejectDays) {
            this.expireRejectDays = expireRejectDays;
        }

        public double getCat() {
            return cat;
        }

        public double getYearRate() {
            return yearRate;
        }

        public void setYearRate(double yearRate) {
            this.yearRate = yearRate;
        }


        public void setCat(double cat) {
            this.cat = cat;
        }

        public boolean isHasDue() {
            return hasDue;
        }

        public void setHasDue(boolean hasDue) {
            this.hasDue = hasDue;
        }

        public double getAuditMoney() {
            return auditMoney;
        }

        public void setAuditMoney(double auditMoney) {
            this.auditMoney = auditMoney;
        }

        public double getApplyMoney() {
            return applyMoney;
        }

        public void setApplyMoney(double applyMoney) {
            this.applyMoney = applyMoney;
        }

        public int getApplyMonth() {
            return applyMonth;
        }

        public void setApplyMonth(int applyMonth) {
            this.applyMonth = applyMonth;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getLendRate() {
            return lendRate;
        }

        public void setLendRate(double lendRate) {
            this.lendRate = lendRate;
        }

        public int getLoanDays() {
            return loanDays;
        }

        public void setLoanDays(int loanDays) {
            this.loanDays = loanDays;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserIdNo() {
            return userIdNo;
        }

        public void setUserIdNo(String userIdNo) {
            this.userIdNo = userIdNo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
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
