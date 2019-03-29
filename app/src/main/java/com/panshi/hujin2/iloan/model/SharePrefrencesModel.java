package com.panshi.hujin2.iloan.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.util.DebugLog;

import java.util.HashMap;

/**
 * SharePrefrences 数据库
 * Created by Administrator on 2018/6/11 0011.
 */

public class SharePrefrencesModel {
    SharedPreferences sharedPreferences;

    public SharePrefrencesModel(Context context) {
        sharedPreferences = context.getSharedPreferences(Constant.SHAREPAREN_XML_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 存数据
     *
     * @param params
     */
    public void save(HashMap<String, Object> params) {

        HashMap<String, Object> map = (HashMap) params;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (HashMap.Entry<String, Object> m : map.entrySet()) {
            Object value = m.getValue();
            if (value instanceof String) {
                editor.putString(m.getKey(), (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(m.getKey(), (Integer) value);
            } else if (value instanceof Float) {
                editor.putFloat(m.getKey(), (Float) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(m.getKey(), (Boolean) value);
            } else if (value instanceof Long) {
                editor.putLong(m.getKey(), (Long) value);
            }
        }
        boolean b = editor.commit();
        DebugLog.i("wang", "====" + b);
    }

    /**
     * 查数据
     *
     * @param key
     * @return
     */
    public Object query(String key) {
        HashMap<String, ?> map = (HashMap<String, ?>) sharedPreferences.getAll();
        return map.get(key);
    }

    /**
     * 删除数据
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * 删除所有数据
     *
     * @return
     */
    public boolean deleteAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }
}
