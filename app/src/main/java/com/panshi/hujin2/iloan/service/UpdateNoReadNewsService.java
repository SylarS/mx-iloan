package com.panshi.hujin2.iloan.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.view.activity.MainActivity;

import java.util.Iterator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/** 更新未读消息服务
 * Created by Administrator on 2018/7/20 0020.
 */

public class UpdateNoReadNewsService extends IntentService {
    public static final String Tag="UpdateNoReadNewsService";

    @Override
    public void onCreate() {
        super.onCreate();
        DebugLog.i("wang","===service==oncrearte");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *

     */

   public UpdateNoReadNewsService(){
       super(Tag);
   }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DebugLog.i("wang","===onHandleInten==");
        RetrofitFactory.createFactoty().create(UserApi.class).updateNoReadNews().subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Response response) {
                if(response.getCode()== Constant.SUCESS_CODE){
                    Constant.REFRESH_NOREAD_NEWS=1;
                    DebugLog.i("wang","===更新未读消息成功");
                    if(AppStaus.activitielists!=null){
                        Iterator it= AppStaus.activitielists.iterator();
                        if(it.hasNext()){
                           Activity activity= (Activity) it.next();
                            if(activity instanceof MainActivity){
                                ((MainActivity)activity).refreshNews();
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}
