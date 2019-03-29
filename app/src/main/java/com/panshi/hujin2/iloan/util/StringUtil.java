package com.panshi.hujin2.iloan.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.bean.CountryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2018/7/3 0003.
 */

public class StringUtil {

    public static String checkUrl(String url) {
        if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
            return "http://" + url;
        }
        return url;
    }

    public static String dealPhone(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum)) {
            if (phoneNum.length() >= 8) {
                return phoneNum.substring(0, 3) + "***" + phoneNum.substring(phoneNum.length() - 4, phoneNum.length());
            }
        }
        return "";
    }


    public static String getLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    public static String[] getCountryZipCode(Context context) {//   HashMap<Integer, String> countryCodeMap = new HashMap<>();
        String CountryID = "";
        String[] CountryZipCode = new String[2];
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        if (TextUtils.isEmpty(CountryID)) {
            Locale locale = context.getResources().getConfiguration().locale;
            CountryID = locale.getCountry();
        }
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode[0] = g[0];
                CountryZipCode[1] = g[2];

                break;
            }
        }
        DebugLog.i("wang", "code==" + CountryZipCode);
        return CountryZipCode;
    }

    public static List<CountryEntity> getCountry(Context context) {//   HashMap<Integer, String> countryCodeMap = new HashMap<>();
        String CountryZipCode = "";
        List<CountryEntity> countryEntityList = new ArrayList<>();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            CountryEntity countryEntity = new CountryEntity();
            String[] g = rl[i].split(",");
            countryEntity.setCountryCode(g[0]);
            countryEntity.setCountryName(g[2]);
            countryEntityList.add(countryEntity);
        }
        DebugLog.i("wang", "code==" + CountryZipCode);
        return countryEntityList;
    }

    public static int getRamdomValue(int round, int min) {
        Random random = new Random();
        int value = random.nextInt(round) + min;
        return value;
    }


}
