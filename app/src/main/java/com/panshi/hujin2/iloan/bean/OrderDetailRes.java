package com.panshi.hujin2.iloan.bean;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class OrderDetailRes {

    /**
     * code : 0
     * data : {"applyMoney":0,"bankCardNum":"string","bankName":"string","borrowTime":"2018-07-30T09:15:33.687Z","currentRepay":0,"orderNo":"string","repayCode":"string","repayTime":"2018-07-30T09:15:33.687Z","status":0}
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
         * bankCardNum : string
         * bankName : string
         * borrowTime : 2018-07-30T09:15:33.687Z
         * currentRepay : 0
         * orderNo : string
         * repayCode : string
         * repayTime : 2018-07-30T09:15:33.687Z
         * status : 0
         */

        private double applyMoney;
        private String bankCardNum;
        private String bankName;
        private String borrowTime;
        private double currentRepay;
        private String orderNo;
        private String repayCode;
        private String repayTime;
        private int status;
        private int distanceRepayDay; //待还款天数，或逾期天数
        private String receiveBankName;//还款名称
        private String receiveAccount;//还款账户
        private String receiveAccountName;//还款人姓名
        private String receiveClabe; //电子账户

        public String getReceiveClabe() {
            return receiveClabe;
        }

        public void setReceiveClabe(String receiveClabe) {
            this.receiveClabe = receiveClabe;
        }

        public String getReceiveAccountName() {
            return receiveAccountName;
        }

        public void setReceiveAccountName(String receiveAccountName) {
            this.receiveAccountName = receiveAccountName;
        }

        public String getReceiveAccount() {
            return receiveAccount;
        }

        public void setReceiveAccount(String receiveAccount) {
            this.receiveAccount = receiveAccount;
        }

        public String getReceiveBankName() {
            return receiveBankName;
        }

        public void setReceiveBankName(String receiveBankName) {
            this.receiveBankName = receiveBankName;
        }

        public int getDistanceRepayDay() {
            return distanceRepayDay;
        }

        public void setDistanceRepayDay(int distanceRepayDay) {
            this.distanceRepayDay = distanceRepayDay;
        }

        public double getApplyMoney() {
            return applyMoney;
        }

        public void setApplyMoney(double applyMoney) {
            this.applyMoney = applyMoney;
        }

        public String getBankCardNum() {
            return bankCardNum;
        }

        public void setBankCardNum(String bankCardNum) {
            this.bankCardNum = bankCardNum;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBorrowTime() {
            return borrowTime;
        }

        public void setBorrowTime(String borrowTime) {
            this.borrowTime = borrowTime;
        }

        public double getCurrentRepay() {
            return currentRepay;
        }

        public void setCurrentRepay(double currentRepay) {
            this.currentRepay = currentRepay;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getRepayCode() {
            return repayCode;
        }

        public void setRepayCode(String repayCode) {
            this.repayCode = repayCode;
        }

        public String getRepayTime() {
            return repayTime;
        }

        public void setRepayTime(String repayTime) {
            this.repayTime = repayTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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
