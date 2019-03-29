package com.panshi.hujin2.iloan.api;

import com.panshi.hujin2.iloan.bean.ActiveRes;
import com.panshi.hujin2.iloan.bean.LoginRes;
import com.panshi.hujin2.iloan.bean.MainDiscountCouponBean;
import com.panshi.hujin2.iloan.bean.MainDiscountCouponOuterBean;
import com.panshi.hujin2.iloan.bean.NewsRes;
import com.panshi.hujin2.iloan.bean.NoReadNewsRes;
import com.panshi.hujin2.iloan.bean.OrderDetailRes;
import com.panshi.hujin2.iloan.bean.OrderInfoRes;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.bean.UpdateInfoRes;
import com.panshi.hujin2.iloan.bean.YearRateRes;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public interface UserApi {
    @POST("/user/login")
    Observable<LoginRes> login(@Body RequestBody body);

    @POST("/user/register")
    Observable<LoginRes> regit(@Body RequestBody body);

    @GET("/user/register/check")
    Observable<Response> checkRegit(@Query("areaCode") String areaCode, @Query("phoneNumber") String phoneNum);

    @GET("/system/apkVersion")
    Observable<UpdateInfoRes> getUpdateInfo();

    @GET("")
    Observable<ResponseBody> getApk();

    @PATCH("/account/payment/password/forget")
    Observable<Response> forgetPayPasswd(@Body RequestBody body);

    @PATCH("/user/login/password/forget")
    Observable<LoginRes> forgetLogin(@Body RequestBody body);

    @DELETE("/user/logout")
    Observable<Response> LoginOut();

    @GET("/verification/code")
    Observable<Response> getVertifyCode(@Query("areaCode") String areaCode, @Query("phoneNumber") String phoneNum, @Query("verificationType") int type);

    @GET("/verification/code/check")
    Observable<Response> checkVertifyCode(@Query("areaCode") String areaCode, @Query("phoneNumber") String phoneNum, @Query("verificationCode") String vertifyCode, @Query("verificationType") int type);

    @POST("/user/contact")
    Observable<Response> uploadContact(@Body RequestBody body);

    @GET("/account/payment/password/exist")
    Observable<Response> checkBindCard();

    @PATCH("/user/login/password/update")
    Observable<Response> updateLoginPasswd(@Body RequestBody body);

    @PATCH("/account/payment/password/update")
    Observable<Response> updatePayPasswd(@Body RequestBody body);

    @POST("/user/authentication")
    Observable<Response> uploadRealInfo(@Body RequestBody body);

    @POST("/user/work")
    Observable<Response> uploadInComeInfo(@Body RequestBody body);

    @POST("/user/family")
    Observable<Response> uploadFamailyInfo(@Body RequestBody body);

    @GET("/message/history")
    Observable<NewsRes> getNews(@Query("nextId") Integer nextId, @Query("limit") Integer limit);

    @PATCH("/message/read")
        //更新未读消息
    Observable<Response> updateNoReadNews();

    @GET("/order/user")
//查询最新一条订单
    Observable<OrderInfoRes> queryOderInfo();

    @GET("/order/detail/{orderId}")
    Observable<OrderDetailRes> queryOderDetailInfo(@Path("orderId") int orderId);

    @GET("/message/unread")
        //获取未读消息
    Observable<NoReadNewsRes> getNoRead();

    @GET("/homePage")
    Observable<YearRateRes> queryYearRate(@Query("borrowDays") int borrowDays);

    @POST("/user/callrecord")
    Observable<Response> uploadCallRecord(@Body RequestBody body);

    @POST("/user/callrecord/message/count")
    Observable<Response> uploadSmsNum(@Query("messageCount") int num);

    @GET("/activity/frame")
//获取活动信息
    Observable<ActiveRes> getActiveInfo();

    @GET("/activity/channel/{activityId}")
//分享成功
    Observable<Response> uploadShareSucess(@Path("activityId") int activityId, @Query("channelsType") int channelsType);

    @PUT("/user/register/invitationCode")
        //注册邀请码
    Observable<Response> postInviteCode(@Query("invitationCode") String invitationCode);

    @POST("/user/face/saveImg")
    Observable<Response> uploadFaceFile(@Body RequestBody body);

    @GET("/coupon/frame")
        //获取活动信息
    Observable<MainDiscountCouponOuterBean> getDiscountCoupon();

    @POST("/user/device/detail")
    Observable<Response> getDeviceDetail(@Body RequestBody body);
}
