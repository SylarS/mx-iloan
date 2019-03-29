package com.panshi.hujin2.iloan.presenter;




import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.NewsRes;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.view.interfaces.NewsInterface;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class NewsPresenter extends BasePresenter {
     public void getListNews(Integer newxtId,Integer limit){
         RetrofitFactory.createFactoty().create(UserApi.class).getNews(newxtId,limit).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<NewsRes>() {
             @Override
             public boolean test(@NonNull NewsRes newsRes) throws Exception {
                 if (newsRes.getCode()== Constant.SUCESS_CODE){
                     return true;
                 }
                 if(isViewAttach()){
                     ((NewsInterface)getAttach()).onFailture(newsRes.getCode(),newsRes.getMessage());
                 }
                 return false;
             }
         }).map(new Function<NewsRes, List<NewsRes.DataBean.AppPushHistoryOutputBOListBean>>() {
             @Override
             public List<NewsRes.DataBean.AppPushHistoryOutputBOListBean> apply(@NonNull NewsRes newsRes) throws Exception {
                  if(isViewAttach()){
                      ((NewsInterface)getAttach()).callNextId(newsRes.getData().getNextId());
                  }
                  if(newsRes.getData().getAppPushHistoryOutputBOList()==null){
                      if(isViewAttach()){
                          ((NewsInterface)getAttach()).callNewsList(null);
                      }
                  }
                 return newsRes.getData().getAppPushHistoryOutputBOList();
             }
         }).subscribe(
                 new CusumeObserver<List<NewsRes.DataBean.AppPushHistoryOutputBOListBean>>(){
                     @Override
                     public void onNext(@NonNull List<NewsRes.DataBean.AppPushHistoryOutputBOListBean> appPushHistoryOutputBOListBeen) {
                         super.onNext(appPushHistoryOutputBOListBeen);
                         if(isViewAttach()){
                             ((NewsInterface)getAttach()).callNewsList(appPushHistoryOutputBOListBeen);
                         }
                     }
                 }

                 /*new Observer<List<NewsRes.DataBean.AppPushHistoryOutputBOListBean>>() {
             @Override
             public void onSubscribe(@NonNull Disposable d) {

             }

             @Override
             public void onNext(@NonNull List<NewsRes.DataBean.AppPushHistoryOutputBOListBean> appPushHistoryOutputBOListBeen) {
                    if(isViewAttach()){
                        ((NewsInterface)getAttach()).callNewsList(appPushHistoryOutputBOListBeen);
                    }
             }

             @Override
             public void onError(@NonNull Throwable e) {
                 if(isViewAttach()){
                     ((NewsInterface)getAttach()).callNewsList(null);
                 }
             }

             @Override
             public void onComplete() {

             }
         }*/);
     }
}
