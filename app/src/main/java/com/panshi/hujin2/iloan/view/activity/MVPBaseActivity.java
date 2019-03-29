package com.panshi.hujin2.iloan.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.StatusBarUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.BaseViewInterface;
import com.panshi.hujin2.iloan.view.widget.LoadingDialog;

import android.support.design.widget.AppBarLayout;
import android.widget.TextView;


/**
 * Created by Administrator on 2018/6/11 0011.
 * 基层View
 */

public abstract class MVPBaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity implements BaseViewInterface {
    protected T mpresenterBase;
    protected boolean useThemestatusBarColor = false;//是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = true;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    private Dialog loadingDialog;
    private TextView mtvTitle;
    private RelativeLayout mrlTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    setStatusBar();
        AppStaus.regitActivity(this);
        mpresenterBase = (T) oncreatePresenter();
        if (mpresenterBase != null) {
            mpresenterBase.attachView((V) this);
        } else {
            try {
                // throw new Exception("you must be oncreatePresnter");
                DebugLog.i("wang", "====you must be oncreatePresnter");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        initToobar();
    }

    private void initToobar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            /*if(toolbar.getParent() instanceof AppBarLayout){
                setStatubar(toolbar);
            }else if(toolbar.getParent() instanceof RelativeLayout){
                setStatubarR(toolbar);
            }else if(toolbar.getParent() instanceof LinearLayout){
                setStatubarL(toolbar);
            }*/

        }
        if (findViewById(R.id.tv_title) != null) {
            mtvTitle = (TextView) findViewById(R.id.tv_title);
        }
        if (findViewById(R.id.rl_back) != null) {
            mrlTitle = (RelativeLayout) findViewById(R.id.rl_back);
            mrlTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    public void setTitle(String title) {
        if (mtvTitle != null) {
            mtvTitle.setText(title);
        }
    }

    public void setStatubar(Toolbar toolbar) {
        StatusBarUtil.setTranslucentForImageView(this, toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, getStatusHeight(this), 0, 0);
        toolbar.setLayoutParams(params);
    }

    public void setStatubarL(Toolbar toolbar) {
        StatusBarUtil.setTranslucentForImageView(this, toolbar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, getStatusHeight(this), 0, 0);
        toolbar.setLayoutParams(params);
    }

    public void setStatubarR(Toolbar toolbar) {
        StatusBarUtil.setTranslucentForImageView(this, toolbar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, getStatusHeight(this), 0, 0);
        toolbar.setLayoutParams(params);
    }

    @Override
    public void startActivity(Intent intent) {
      /*  ComponentName componentName = intent.getComponent();
        if (componentName != null) {
            String className = componentName.getClassName();
            SharePrefrencesModel sharePrefrencesModel = new SharePrefrencesModel(this);
            if (!TextUtils.isEmpty(className) && !className.equals(LoginActivity.class.getName())
                    && !className.equals(MainActivity.class.getName())
                    && !className.equals(RegiterActivity.class.getName()) &&
                    AppStaus.userInfo == null && (sharePrefrencesModel.query(Constant.USER_TOKEN_KEY) == null || sharePrefrencesModel.query(Constant.USER_ID_KEY) == null)) {
                intent.setClass(this, LoginActivity.class);
            }
        }*/
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
      /*  ComponentName componentName = intent.getComponent();
        if (componentName != null) {
            String className = componentName.getClassName();
            SharePrefrencesModel sharePrefrencesModel = new SharePrefrencesModel(this);
            if (!TextUtils.isEmpty(className) && !className.equals(LoginActivity.class.getName())
                    && !className.equals(MainActivity.class.getName())
                    && !className.equals(RegiterActivity.class.getName()) && AppStaus.userInfo == null
                    && (sharePrefrencesModel.query(Constant.USER_TOKEN_KEY) == null || sharePrefrencesModel.query(Constant.USER_ID_KEY) == null)) {
                intent.setClass(this, LoginActivity.class);
            }
        }*/
        super.startActivityForResult(intent, requestCode);
        //   callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!getClass().equals(MainActivity.class)) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected int getStatusHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStaus.unRegitActivity(this);
        if (mpresenterBase != null && mpresenterBase.isViewAttach()) {
            mpresenterBase.detachView((V) this);
        }

    }

    @Override
    public void loadMoreCompele(boolean complete) {

    }

    @Override
    public void showFailture(int code, String message) {
        ToastUtil.showToast(message);
    }

    @Override
    public void showProgessDialog() {
        if (!isFinishing()) {
            loadingDialog = LoadingDialog.createLoadingDialog(MVPBaseActivity.this, getResources().getString(R.string.data_loading));
            loadingDialog.show();
        }

    }

    @Override
    public void hiddenProgessDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    protected abstract BasePresenter<V> oncreatePresenter();

    protected abstract Toolbar getToolbar();


}
