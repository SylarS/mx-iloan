package com.panshi.hujin2.iloan.bean;

/**
 * Created by Administrator on 2018/8/7 0007.
 */

public class YearRateRes {
    private int code;
    private YearRateRes.DataBean data;
    private String message;
    private YearRateRes.PageBean page;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public YearRateRes.DataBean getData() {
        return data;
    }

    public void setData(YearRateRes.DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public YearRateRes.PageBean getPage() {
        return page;
    }

    public void setPage(YearRateRes.PageBean page) {
        this.page = page;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {


        private double cat; //年利率
        private double yearRate;//平均年度总成本

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
