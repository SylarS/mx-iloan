package com.panshi.hujin2.iloan.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.igexin.sdk.PushManager;
import com.mob.MobSDK;
import com.panshi.hujin2.iloan.bean.LoginRes;
import com.panshi.hujin2.iloan.factory.ImageLoaderFactory;
import com.panshi.hujin2.iloan.service.PushIntentService;
import com.panshi.hujin2.iloan.service.PushService;
import com.panshi.hujin2.iloan.util.ChannelUtil;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.PackageManagerUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * asmall loan application
 * Created by Administrator on 2018/6/13 0013.
 */

public class AppStaus extends Application {
    public static Context mContext;
    public static int APP_VERCODE;
    public static String APP_VERSIONNAME;
    public static String MAC;
    public static String language;
    public static String channel;
    public static String token;
    public static String processName;
    public static String packageName;
    public static String country, province, city;
    public static LoginRes.DataBean userInfo;
    public static String longitude = "";
    public static String latitude = "";
    public static int msmsNum = 0;
    public static List<Activity> activitielists = new ArrayList<>();
    public static int notificationArrived = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取当前进程名
        processName = getProcessName(android.os.Process.myPid());
        // 获取当前包名
        packageName = this.getPackageName();
        APP_VERCODE = PackageManagerUtil.getVersionCode(this);
        APP_VERSIONNAME = PackageManagerUtil.getVersionName(this);
        mContext = this;
        channel = ChannelUtil.getChannel(this);
        // CrashReport.initCrashReport(getApplicationContext(), "0b466efc1c", false);

        DebugLog.d("wang", "====mac||" + MAC);

        //   ImageLoaderFactory.createImageLoader().initialize(getApplicationContext());
        Locale locale = null;
      /*  if (Build.VERSION.SDK_INT >= 17) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {*/
        locale = getResources().getConfiguration().locale;
        // }
        //   Locale.
        PushManager.getInstance().initialize(this, PushService.class);
        PushManager.getInstance().registerPushIntentService(this, PushIntentService.class);
        language = locale.getLanguage() + "-" + locale.getCountry();
      /*  FacebookSdk.setApplicationId("1964719267159304");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FacebookSdk.setAutoLogAppEventsEnabled(true);
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);*/
        DebugLog.d("wang", "language||" + language);
        AppsFlyerLib.getInstance().init("mjBybJPN4C2dM3s9FUVS4g", new AppStusAppsFlyerConversionListener(), getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
        MobSDK.init(AppStaus.mContext);
        ImageLoaderFactory.createImageLoader().initialize(getApplicationContext());
        // DebugLog.i("wang");

    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


    public static String getLocationInfo() {
        return AppStaus.country + "_" + AppStaus.province + "_" + AppStaus.city;
    }

    public static int getUserID() {
     /*    if(userInfo==null){
             SharePrefrencesModel sharePrefrencesModel=new SharePrefrencesModel(AppStaus.mContext);
            if(sharePrefrencesModel.query(Constant.USER_ID_KEY)!=null){
                return (int) sharePrefrencesModel.query(Constant.USER_ID_KEY);
            }
         }else{
             return  userInfo.getId();
         }*/
        return 0;
    }

    public static void regitActivity(Activity activity) {
        if (activitielists != null) {
            if (activitielists.contains(activity)) {
                activitielists.remove(activity);
            }
            activitielists.add(activity);
        }
    }

    public static void unRegitActivity(Activity activity) {
        if (activitielists != null) {
            activitielists.remove(activity);
        }
    }

    public static void unRegitAllActivity() {
        if (activitielists != null) {
            for (int i = 0; i < activitielists.size(); i++) {
                // Activity activity = activitielists.get(i);
                // activitielists.remove(activity);
                // activity.finish();
            }
        }
    }

    class AppStusAppsFlyerConversionListener implements AppsFlyerConversionListener {

        @Override
        public void onInstallConversionDataLoaded(Map<String, String> conversionData) {

            for (String attrName : conversionData.keySet()) {
                Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
                if (conversionData.containsKey("media_source")) {
                    //  ToastUtil.showToast(conversionData.get("media_source"));
                }
            }

        }

        @Override
        public void onInstallConversionFailure(String errorMessage) {
            Log.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " + errorMessage);
        }

        @Override
        public void onAppOpenAttribution(Map<String, String> map) {

        }

        @Override
        public void onAttributionFailure(String errorMessage) {
            Log.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
        }
    }
}
