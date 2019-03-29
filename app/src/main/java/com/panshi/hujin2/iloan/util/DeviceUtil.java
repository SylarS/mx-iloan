package com.panshi.hujin2.iloan.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.panshi.hujin2.iloan.application.AppStaus;

import java.util.List;

import static com.panshi.hujin2.iloan.application.AppStaus.mContext;
import static com.panshi.hujin2.iloan.application.AppStaus.regitActivity;

public class DeviceUtil {

    public static String getDeviceName() {
//		String devName = android.os.Build.MANUFACTURER +"-"+ android.os.Build.PRODUCT;
        String devName = Build.MODEL;
        devName = devName.trim();
        return devName;
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context sContext) {
        TelephonyManager telephonyManager = (TelephonyManager) sContext.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId(); //IMEI码
        return IMEI;
    }

    /**
     * 获取手机IMSI号
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();

        return imsi;
    }

    public static String getICCID(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iccid = mTelephonyMgr.getSimSerialNumber();
        return iccid;
    }

    public static String getUniqueID(Context sContext) {
        TelephonyManager telephonyManager = (TelephonyManager) sContext.getSystemService(Context.TELEPHONY_SERVICE);
        String m_IMEI = telephonyManager.getDeviceId(); //IMEI码


        //这个同一个厂商同样设备同样的rom下会重复
        String m_DEVID = "35" + //we make this look like a valid IMEI
                Build.BOARD + Build.BRAND
                + Build.CPU_ABI + Build.DEVICE
                + Build.DISPLAY + Build.HOST
                + Build.ID + Build.MANUFACTURER
                + Build.MODEL.length() + Build.PRODUCT.length()
                + Build.TAGS.length() + Build.TYPE.length()
                + Build.USER.length(); // 13 digits
        DebugLog.i("wang", "==dev name==" + Build.MODEL);
        DebugLog.i("wang", "==Build.BOARD==" + Build.BOARD);
        DebugLog.i("wang", "==Build.BRAND==" + Build.BRAND);
        DebugLog.i("wang", "==Build.CPU_ABI==" + Build.CPU_ABI);
        DebugLog.i("wang", "==Build.DEVICE==" + Build.DEVICE);
        DebugLog.i("wang", "==Build.DISPLAY==" + Build.DISPLAY);
        DebugLog.i("wang", "==Build.HOST==" + Build.HOST);
        DebugLog.i("wang", "==Build.ID==" + Build.ID);
        DebugLog.i("wang", "==Build.MANUFACTURER==" + Build.MANUFACTURER);
        DebugLog.i("wang", "==Build.MODEL==" + Build.MODEL);
        DebugLog.i("wang", "==Build.PRODUCT==" + Build.PRODUCT);
        DebugLog.i("wang", "==Build.TAGS==" + Build.TAGS);
        DebugLog.i("wang", "==Build.TYPE==" + Build.TYPE);
        DebugLog.i("wang", "==Build.USER==" + Build.USER);
        WifiManager wm = (WifiManager) sContext.getSystemService(Context.WIFI_SERVICE);
        String m_WLANMAC = wm.getConnectionInfo().getMacAddress();
        DebugLog.i("wang", "==IMEI==" + m_IMEI + "===m_DEVID===" + m_DEVID + "===m_WLANMAC===" + m_WLANMAC);
        DebugLog.i("wang", "==uniqueID===" + m_IMEI + m_DEVID + m_WLANMAC);
        String uniqueID = MD5Util.md5(m_IMEI + m_DEVID + m_WLANMAC);
        DebugLog.i("wang", "==uniqueID==MD5==" + uniqueID);
        return uniqueID;
    }

    public static String getWIFIMAC() {
        WifiManager wm = (WifiManager) AppStaus.mContext.getSystemService(Context.WIFI_SERVICE);
        String m_WLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_WLANMAC;
    }

    /**
     * 获取电话号码
     *
     * @param context
     * @return
     */
    public static String getTelPhone(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNum = mTelephonyMgr.getLine1Number();
        DebugLog.i("wang", "==phoneNum==" + phoneNum);
        return phoneNum;
    }

    public static boolean isScreenChange(Context ctx) {
        Configuration mConfiguration = ctx.getResources().getConfiguration(); // 获取设置的配置信息
        int ori = mConfiguration.orientation; // 获取屏幕方向
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            return true;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            return false;
        }
        return false;
    }


    public static boolean screenLocked(Context ctx) {
        KeyguardManager mKeyguardManager = (KeyguardManager) ctx.getSystemService(Context.KEYGUARD_SERVICE);
        boolean status = mKeyguardManager.inKeyguardRestrictedInputMode();
        return status;
    }

    public List<TelephonyManager> getTelephonyManager() {
        String simId_1, simId_2;
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> c = null;
       /* try {
            c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            simId_1 = (Integer) fields1.get(null);
            Field fields2 = c.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            simId_2 = (Integer) fields2.get(null);

            Method mx = TelephonyManager.class.getMethod("getDefault",
                    int.class);
            TelephonyManager tm1 = (TelephonyManager) mx.invoke(tm, simId_1);
            TelephonyManager tm2 = (TelephonyManager) mx.invoke(tm, simId_2);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
*/

        return null;
    }


}
