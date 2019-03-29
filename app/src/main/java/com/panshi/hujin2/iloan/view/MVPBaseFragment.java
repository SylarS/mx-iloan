package com.panshi.hujin2.iloan.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.util.ToastUtil;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/6/11 0011.
 * 基层View
 */

public abstract class MVPBaseFragment<V, T extends BasePresenter<V>> extends Fragment implements BaseViewInterface{
    protected T mpresenterBase;
    public View view;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mpresenterBase = (T) oncreatePresenter();
        if (mpresenterBase != null) {
            mpresenterBase.attachView((V) this);
        } else {
            try {
                throw new Exception("you must be oncreatePresnter");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    /**
     * 获取状态栏高度
     * @return
     */
    protected int getStatusHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mpresenterBase != null && mpresenterBase.isViewAttach()) {
            mpresenterBase.detachView((V) this);
        }
    }

    @Override
    public void startActivity(Intent intent) {
      /*  ComponentName componentName = intent.getComponent();
        String className = componentName.getClassName();
        SharePrefrencesModel sharePrefrencesModel=new SharePrefrencesModel(getActivity());
        if (!TextUtils.isEmpty(className) && !className.equals(LoginActivity.class.getName())
                && !className.equals(MainActivity.class.getName())
                && !className.equals(RegiterActivity.class.getName())&&!className.equals(WebViewBannerActivity.class.getName())
                &&!className.equals(SetingActivity.class.getName())&& AppStaus.userInfo==null &&
                (sharePrefrencesModel.query(Constant.USER_TOKEN_KEY)==null||sharePrefrencesModel.query(Constant.USER_ID_KEY)==null)) {
            intent.setClass(getActivity(), LoginActivity.class);
        }*/
        super.startActivity(intent);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void showProgessDialog() {

    }

    @Override
    public void hiddenProgessDialog() {

    }

    @Override
    public void showFailture(int code, String message) {
        ToastUtil.showToast(message);
    }

    @Override
    public void loadMoreCompele(boolean complete) {

    }

    protected abstract BasePresenter<V> oncreatePresenter();
}
