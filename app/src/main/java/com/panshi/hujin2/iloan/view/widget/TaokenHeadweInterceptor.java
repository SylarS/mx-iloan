package com.panshi.hujin2.iloan.view.widget;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.igexin.sdk.PushManager;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.model.SharePrefrencesModel;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.DeviceUtil;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.MediaTypeUtil;
import com.panshi.hujin2.iloan.util.PackageManagerUtil;
import com.panshi.hujin2.iloan.util.StringUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/6/20 0020.
 */

public class TaokenHeadweInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

       /* HashMap<String, String> heads = new HashMap<>();
        heads.put("pm-ostype", "2");////系统类型，1: 其他，2.iso,3:安卓
        heads.put("pm-osversion",String.valueOf(Build.VERSION.SDK_INT));
        heads.put("pm-osdevice", AppStaus.MAC);//设备号
        heads.put("pm-appversion", String.valueOf(AppStaus.APP_VERSIONNAME));
        heads.put("pm-token",""); //token值
        heads.put("pm-uid", "");//用户id*/
        Response response = null;
        Request updateRequest=null;
        try {
            Request request = chain.request();
            String userToken = "";
            int userId = 0;
            DebugLog.i("wang", "============request================");
            if (AppStaus.userInfo == null) {
                SharePrefrencesModel sharePrefrencesModel = new SharePrefrencesModel(AppStaus.mContext);
                if (sharePrefrencesModel.query(Constant.USER_TOKEN_KEY) != null) {
                    userToken = (String) sharePrefrencesModel.query(Constant.USER_TOKEN_KEY);
                    userId = (int) sharePrefrencesModel.query(Constant.USER_ID_KEY);
                }
            } else {
                if (!TextUtils.isEmpty(AppStaus.userInfo.getToken())) {
                    userToken = AppStaus.userInfo.getToken();
                    userId = AppStaus.userInfo.getId();
                } else {
                    SharePrefrencesModel sharePrefrencesModel = new SharePrefrencesModel(AppStaus.mContext);
                    if (sharePrefrencesModel.query(Constant.USER_TOKEN_KEY) != null) {
                        userToken = (String) sharePrefrencesModel.query(Constant.USER_TOKEN_KEY);
                        userId = (int) sharePrefrencesModel.query(Constant.USER_ID_KEY);
                    }
                }
            }
            String clientId= PushManager.getInstance().getClientid(AppStaus.mContext);
             updateRequest = request.newBuilder().
                    //    header("pm_id","1").  //平台id 盘管家 1   无忧贷2
                            header("g2-osType", "3").
                            header("g2-systemVersion", String.valueOf(Build.VERSION.SDK_INT)).
                            header("g2-deviceId", AppStaus.MAC == null ? DeviceUtil.getUniqueID(AppStaus.mContext) : AppStaus.MAC).
                            header("g2-deviceType", DeviceUtil.getDeviceName()).
                            header("g2-appVersion", AppStaus.APP_VERSIONNAME == null ?
                                    PackageManagerUtil.getVersionName(AppStaus.mContext) : String.valueOf(AppStaus.APP_VERSIONNAME)).
                             header("g2-vest",Constant.apkType) //iLoan、1 iloan、2 mloan、3、MonneyR
                            .header("g2-token", userToken).

                            header("g2-uid", String.valueOf(userId)).addHeader("Connection","close").
                            header("Accept-Language", StringUtil.getLanguage(AppStaus.mContext)).
                            header("g2-longitude",AppStaus.longitude).
                            header("g2-latitude", AppStaus.latitude).

                            header("g2-clientId", clientId==null?"":clientId).
                            header("g2-appsflyId",AppsFlyerLib.getInstance().getAppsFlyerUID(AppStaus.mContext)).
                            method(request.method(), request.body()).build();
            Connection connection = chain.connection();
            String requestStartMessage = "--> "
                    + request.method()
                    + ' ' + request.url()
                    + (connection != null ? " " + connection.protocol() : "");

            DebugLog.i("wang", "====" + requestStartMessage);
            if(chain!=null){
                response = chain.proceed(updateRequest);
                if(response==null){
                    ToastUtil.showToast(AppStaus.mContext.getString(R.string.net_exception));
                }
            }else{
                ToastUtil.showToast(AppStaus.mContext.getString(R.string.net_exception));
            }
            ResponseBody responseBody = response.body();


            if (responseBody != null) {
                DebugLog.i("wang", "============response================");

                String code = response.header("code");
                DebugLog.e("wang", "===" + code + "||" + response.headers() + "||" + response.code());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    DebugLog.i("wang", "====" + headers.name(i) + "::" + headers.value(i));
                }
                MediaType mediaType = response.body().contentType();
                String content = responseBody.string();
                DebugLog.i("wang", "====Interceptor===" + content);
               /* if (response.code() == 401) {
                    SharePrefrencesModel sharePrefrencesModel = new SharePrefrencesModel(AppStaus.mContext);
                    sharePrefrencesModel.deleteAll();
                    Intent intent = new Intent();
                    intent.setAction("com.panshi.hujin2.loancash.login");
                    if (AppStaus.mContext != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AppStaus.mContext.startActivity(intent);
                    }

                }*/
                JSONObject object=new JSONObject(content);
                DebugLog.d("objectobject", object.toString());
                int result=object.optInt("code");
                if(result==401){
                    SharePrefrencesModel sharePrefrencesModel = new SharePrefrencesModel(AppStaus.mContext);
                    sharePrefrencesModel.deleteAll();
                    Intent intent = new Intent();
                    intent.setAction("com.panshi.hujin2.loancash.login");
                    if (AppStaus.mContext != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Constant.LOGIN_OUT = 1;
                        AppStaus.mContext.startActivity(intent);
                    }
                }
                return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
          /*  if(updateRequest!=null){
              return chain.proceed(updateRequest);
            }*/
            if(response==null){
                if(e instanceof SocketTimeoutException){
                    com.panshi.hujin2.iloan.bean.Response response1=new com.panshi.hujin2.iloan.bean.Response();
                    response1.setCode(318);
                    response1.setMessage("time out exeception");
                    return new Response.Builder().request(updateRequest).protocol(Protocol.HTTP_1_1).code(318).message("time out")
                            .body(MediaTypeUtil.createResponJsonMediaType(GonsonUtil.buildGosn().toJson(response1))).build();
                }
            //    ToastUtil.showToast(AppStaus.mContext.getString(R.string.net_exception));
            }
            DebugLog.e("wang", "==Interceptor=" + e.toString());
        }

        return response;
    }
}
