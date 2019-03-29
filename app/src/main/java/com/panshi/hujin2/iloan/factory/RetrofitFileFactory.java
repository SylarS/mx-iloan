package com.panshi.hujin2.iloan.factory;



import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.view.widget.TaokenHeadweInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class RetrofitFileFactory {
    static OkHttpClient httpClient = new OkHttpClient.Builder().addNetworkInterceptor(new TaokenHeadweInterceptor()).connectTimeout(5, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(50,TimeUnit.SECONDS).build();
    static Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.URL).client(httpClient).
            addConverterFactory(GsonConverterFactory.create(GonsonUtil.buildGosn())).
            addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

    /**
     * 创建retrofit
     *
     * @return
     */
    public static Retrofit createFactoty() {
        return retrofit;
    }

}
