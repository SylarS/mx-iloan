package com.panshi.hujin2.iloan.view.interfaces;


import com.panshi.hujin2.iloan.bean.NewsRes;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public interface NewsInterface {
    void onFailture(int code, String message);
    void callNewsList(List<NewsRes.DataBean.AppPushHistoryOutputBOListBean> list);
    void callNextId(int nextId);

}
