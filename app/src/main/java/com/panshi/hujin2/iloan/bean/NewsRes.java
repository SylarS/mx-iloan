package com.panshi.hujin2.iloan.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class NewsRes {


    /**
     * code : 0
     * data : {"appPushHistoryOutputBOList":[{"businessTypeId":0,"createTime":"2018-07-11T07:55:41.698Z","id":0,"modifyTime":"2018-07-11T07:55:41.698Z","sendTime":"string","status":true,"text":"string","title":"string","userId":0}],"nextId":0}
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
         * appPushHistoryOutputBOList : [{"businessTypeId":0,"createTime":"2018-07-11T07:55:41.698Z","id":0,"modifyTime":"2018-07-11T07:55:41.698Z","sendTime":"string","status":true,"text":"string","title":"string","userId":0}]
         * nextId : 0
         */

        private int nextId;
        private List<AppPushHistoryOutputBOListBean> appPushHistoryOutputBOList;

        public int getNextId() {
            return nextId;
        }

        public void setNextId(int nextId) {
            this.nextId = nextId;
        }

        public List<AppPushHistoryOutputBOListBean> getAppPushHistoryOutputBOList() {
            return appPushHistoryOutputBOList;
        }

        public void setAppPushHistoryOutputBOList(List<AppPushHistoryOutputBOListBean> appPushHistoryOutputBOList) {
            this.appPushHistoryOutputBOList = appPushHistoryOutputBOList;
        }

        public static class AppPushHistoryOutputBOListBean {
            /**
             * businessTypeId : 0
             * createTime : 2018-07-11T07:55:41.698Z
             * id : 0
             * modifyTime : 2018-07-11T07:55:41.698Z
             * sendTime : string
             * status : true
             * text : string
             * title : string
             * userId : 0
             */

            private int businessTypeId;
            private String createTime;
            private int id;
            private String modifyTime;
            private String sendTime;
            private boolean status;
            private String text;
            private String title;
            private int userId;

            public int getBusinessTypeId() {
                return businessTypeId;
            }

            public void setBusinessTypeId(int businessTypeId) {
                this.businessTypeId = businessTypeId;
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

            public String getModifyTime() {
                return modifyTime;
            }

            public void setModifyTime(String modifyTime) {
                this.modifyTime = modifyTime;
            }

            public String getSendTime() {
                return sendTime;
            }

            public void setSendTime(String sendTime) {
                this.sendTime = sendTime;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
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
