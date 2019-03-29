package com.panshi.hujin2.iloan.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/6/11 0011.
 * presenter 层基类
 */

public  class BasePresenter<T> implements PresenterInterace<T> {
    protected Reference<T> mviewRef;

    @Override
    public void attachView(T view) {
        mviewRef = new WeakReference<T>(view);
    }

    public T getAttach() {
        return mviewRef.get();
    }

    @Override
    public boolean isViewAttach() {
        if(mviewRef==null){
            return false;
        }
        return mviewRef.get() != null ? true : false;
    }

    @Override
    public void detachView(T view) {
        if (mviewRef != null) {
            mviewRef.clear();
            mviewRef = null;
        }
    }

}
