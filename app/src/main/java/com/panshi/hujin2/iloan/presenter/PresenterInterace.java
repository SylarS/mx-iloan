package com.panshi.hujin2.iloan.presenter;

/**
 * Created by Administrator on 2018/6/11 0011.
 */

interface PresenterInterace<T> {
    //添加view
    public void attachView(T view);
   //视图是否添加
    public boolean isViewAttach();
   //删除
    public void detachView(T view);
}
