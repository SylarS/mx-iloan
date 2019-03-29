package com.panshi.hujin2.iloan.bean;

import java.util.List;

public class ActiveRes {


    /**
     * code : 0
     * data : [{"activityId":0,"forwardUrl":"string","imageUrl":"string","isShow":0,"isSort":0,"productId":0}]
     * message : string
     * page : {"currentPage":0,"dbIndex":0,"dbNumber":0,"isPaging":true,"pageSize":0,"totalNumber":0,"totalPage":0}
     * success : true
     */

    private int code;
    private String message;
    private PageBean page;
    private boolean success;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PageBean {
        /**
         * currentPage : 0
         * dbIndex : 0
         * dbNumber : 0
         * isPaging : true
         * pageSize : 0
         * totalNumber : 0
         * totalPage : 0
         */

        private int currentPage;
        private int dbIndex;
        private int dbNumber;
        private boolean isPaging;
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

        public boolean isIsPaging() {
            return isPaging;
        }

        public void setIsPaging(boolean isPaging) {
            this.isPaging = isPaging;
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

    public static class DataBean {
        /**
         * activityId : 0
         * forwardUrl : string
         * imageUrl : string
         * isShow : 0
         * isSort : 0
         * productId : 0
         */

        private int activityId;
        private String forwardUrl;
        private String imageUrl;
        private int isShow;
        private int isSort;
        private int productId;

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public String getForwardUrl() {
            return forwardUrl;
        }

        public void setForwardUrl(String forwardUrl) {
            this.forwardUrl = forwardUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public int getIsSort() {
            return isSort;
        }

        public void setIsSort(int isSort) {
            this.isSort = isSort;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }
    }
}
