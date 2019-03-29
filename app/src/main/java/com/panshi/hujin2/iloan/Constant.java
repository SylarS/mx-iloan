package com.panshi.hujin2.iloan;

/**
 * Created by Administrator on 2018/6/11 0011.
 */

/**
 * 常量设置
 */
public class Constant {
    //线上环境 生产环境
     public static final String URL = "https://api.pandafintech.com.mx"; //公测地址
    //h5地址 生产地址
       public static final String WEB_URL = "https://app.pandafintech.com.mx";

//    //测试环境 wifi其作用
//      public static final String URL = "http://192.168.24.100:9030";
//    //测试环境 wifi其作用
//    public static final String WEB_URL = "http://192.168.24.100:3002";

    //新预生产
//    public static final String URL = "https://pre-api.pandafintech.com.mx";
    //h5线上测试环境 预生产
//    public static final String WEB_URL = "http://52.52.134.68:9020";deprected
//    public static final String WEB_URL = "https://pre-app.pandafintech.com.mx";


    //建康
//    public static final String URL = "http://10.0.52.103:8200";
    //黄冰
//    public static final String WEB_URL = "http://10.0.52.171:8100";
    //刁琪
//    public static final String WEB_URL = "http://10.0.52.247:8100";


    /**
     * shareParenfence 数据库名称
     */
    public static final String SHAREPAREN_XML_NAME = "sharesqlite"; //数据库名称

    public static final String USER_ID_KEY = "USER_ID";//用户id
    public static final String USER_TOKEN_KEY = "USER_TOKEN";//用户token
    //登录成功
    public static final int VERTIFY_REGIT = 1;//注册短信获取
    public static final int VERTIFY_RESET = 2;  //重置登录密码
    public static final int VERTIFY_PAY = 3; //重置支付密码
    public static final int SUCESS_CODE = 0;
    public static final int REGIT_CODE = 7;

    public static final int LOGIN_PASSWD = 0;
    public static final int PAY_PASSWD = 1;

    public static final String JS_SUCESS_CODE = "1";
    public static final String JS_FAILTURE_CODE = "0";
    public static final String JS_CODE = "code";
    public static final String JS_DATA = "data";

    public static final int REQUEST_COUNTRY_CODE = 0;
    public static final int RESONSE_COUNTRY_CODE = 1;

    public static final int FORGET_APY_PASS = 1;
    public static final int MIDIFY_APY_PASS = 0;
    public static final int PAGESIZE = 10;
    public static final int G_TYPE = 1;
    public static final int M_TYPE = 2;
    public static final int K_TYPE = 3;
    public static final int B_TYPE = 3;
    //0eLoan、1 iloan、2 mloan、3、MonneyR、4yLOan 5 uLoan、6 HiLoan
    //0eLoan、1 iloan、2 mloan、3、MonneyR、4yLOan 5 uLoan、6 HiLoan
    public static final String apkType = "1";

    public static class OrderStatus {
        public static final int STATUS_1 = 1; //待审核";
        public static final int STATUS_2 = 2; //审核通过";
        public static final int STATUS_3 = 3; //提现/打款中";
        public static final int STATUS_4 = 4; //待还款";
        public static final int STATUS_5 = 5; //逾期中"; //后台暂时去掉了
        public static final int STATUS_6 = 6; //已完成";
        public static final int STATUS_7 = 7; //部分还款";//后台暂时去掉了
        public static final int STATUS_8 = 8; //手动关闭/取消";
        public static final int STATUS_9 = 9; //审批拒绝";
        public static final int STATUS_10 = 10; //逾期已结清";
        public static final int STATUS_11 = 11; //系统关闭";
        public static final int STATUS_12 = 12; //机审拒绝";
        public static final int STATUS_13 = 13; //放款失败";

      /*  STATUS_3(3, "提现/打款中"),
        STATUS_4(4, "待还款"),
        STATUS_5(5, "逾期中"),
        STATUS_6(6, "已完成"),
        STATUS_7(7, "部分还款"),
        STATUS_8(8, "手动关闭/取消"),
        STATUS_9(9, "审批拒绝"),
        STATUS_10(10, "逾期已结清"),
        STATUS_11(11, "系统关闭"),
        STATUS_12(12, "机审拒绝"),*/
    }

    public static int REFRESH_ORDER = 0; //反馈

    //是否是退出账号
    public static int LOGIN_OUT = 0;/*[^\"\']*/
    //  public static final String REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z][^\\\"\\']{6,18}$";
    public static final String REGEX = "^(?![\\d]+$)(?![a-zA-Z]+$)(?![!#$%^&._*]+$)[\\da-zA-Z!#$%^&._*]{6,18}$";

    public static int REFRESH_NOREAD_NEWS = 0; //刷新未读消息
    public static int IF_REFRESH_NOREAD_NEWS = 0; //刷新未读消息

    public static final String HTML_PARAM = "param"; //html参数
    public static final String HTML_PARAM_Str = "string"; //html参数
    public static final String HTML_PARAM_Type = "type"; //html参数
    public static final String HTML_PARAM_Data = "data"; //html参数
    public static final String HTML_PARAM_PATH = "urlPath"; //html参数

    public static final String HTML_LOAN_PATH = "/#/loanPurpose"; //html贷款
    public static final String HTML_WTTHDRAWCASH_PATH = "/#/enchashment"; //html提现
    public static final String HTML_HELP_PATH = "/#/help-center"; //html帮助中心
    public static final String HTML_LOAN_HISTORY_PATH = "/#/invest-list"; //贷款记录
    public static final String HTML_SUGGEST_REPLAY_PATH = "/#/feed-back"; //反馈
    public static final String HTML_MY_REWARD = "/#/my-reward"; //我的优惠券
    public static final String HTML_ABOUT_MONNEY_PATH = "/#/about-money"; //关于还款
    public static final String HTML_ABOUT_US_PATH = "/#/about-us"; //关于我们
    public static final String HTML_REGIT_PROTTROL = "/#/reg-protocol"; //协议
    public static final String HTML_REGIT_PROTTROL_REGIT = "/#/reg-protocol-two"; //协议
    public static final String HTML_ORDER_DETAIL = "/#/invest-detail/"; //协议
    public static final String HTML_TCIKET_CENTER = "/#/voucher-center"; //领取中心
    public static final String HTML_ACITVE = "/escrowwap/firstProm"; //活动
    public static final String HTML_BACK = "/#/user-account"; //银行卡列表

}
