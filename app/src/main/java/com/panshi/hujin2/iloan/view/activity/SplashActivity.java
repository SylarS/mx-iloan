package com.panshi.hujin2.iloan.view.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.SplashPresenter;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.view.interfaces.SplashView;

import static android.content.Intent.FLAG_RECEIVER_FOREGROUND;
import static com.megvii.livenesslib.util.ICamera.getSdkVersion;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public class SplashActivity extends MVPBaseActivity implements SplashView {
    private SplashPresenter splashPresenter;
    private TextView mtvNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.d("intentintent", getIntent().toString() + AppStaus.notificationArrived);
        //notification来的 去往其他应用回来
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N) {
            if (getIntent().getFlags() == 0x10400000 && AppStaus.notificationArrived != 0) {
                goToNewsActivity();
                return;
            }
            //notification来的 FLAG_RECEIVER_FOREGROUND 从notification和第一次安装 0x10000000 没去其他应用
            if (getIntent().getFlags() == FLAG_RECEIVER_FOREGROUND && AppStaus.notificationArrived != 0) {
                goToNewsActivity();
                return;
            }
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无title
        setContentView(R.layout.splash_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏
        initView();
    }

    private void initView() {
        mtvNext = (TextView) findViewById(R.id.tv_next);
        ImageView mimgSplash = findViewById(R.id.img_back);
        //  ApkUtil.setSplashImage(this,mimgSplash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        splashPresenter.doCountDwon(3);
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return splashPresenter = new SplashPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Override
    public void splash(long code) {
        mtvNext.setText(String.valueOf(code));
        if (code == 3) {
            Intent intent = new Intent(SplashActivity.this, LoginLoadActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void goToNewsActivity() {
        Intent intent = new Intent(SplashActivity.this, NewsActivity.class);
        intent.putExtra("goBackToMainActivity", 1);
        startActivity(intent);
        finish();
    }
}
