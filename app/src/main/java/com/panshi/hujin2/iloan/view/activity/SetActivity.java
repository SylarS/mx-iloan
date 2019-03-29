package com.panshi.hujin2.iloan.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.SetPresenter;
import com.panshi.hujin2.iloan.util.AutoUpdateUtil;
import com.panshi.hujin2.iloan.util.PackageManagerUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.SetView;

import java.util.Iterator;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class SetActivity extends MVPBaseActivity implements SetView {
    private SetPresenter setPresenter;
    private LinearLayout mllForgetPasswd;
    private LinearLayout mllForgetPayPasswd;
    private LinearLayout mllModifyPayPasswd;
    private LinearLayout mllModifyPasswd;
    private LinearLayout mllAboutRepay;
    private LinearLayout mllAboutUs;
    private LinearLayout mllQuit;
    private TextView mtvVersion;
    private Toolbar mtoolbar;
    private RelativeLayout mrlVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_layout);
        initView();
        initListener();
    }

    private void initView() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mllForgetPasswd = (LinearLayout) findViewById(R.id.ll_forget_passwd);
        mllForgetPayPasswd = (LinearLayout) findViewById(R.id.ll_forget_pay_passwd);
        mllModifyPasswd = (LinearLayout) findViewById(R.id.ll_modify_passwd);
        mllModifyPayPasswd = (LinearLayout) findViewById(R.id.ll_modify_pay_passwd);
        mllQuit = (LinearLayout) findViewById(R.id.ll_quit);
        mtvVersion = (TextView) findViewById(R.id.tv_version);
        mrlVersion = (RelativeLayout) findViewById(R.id.rl_versoin);
        mllAboutRepay = (LinearLayout) findViewById(R.id.ll_about_repay);
        mllAboutUs = (LinearLayout) findViewById(R.id.ll_about_us);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtvVersion.setText(getString(R.string.current_version,AppStaus.APP_VERSIONNAME));
        mtoolbar.setTitle(getString(R.string.set_my));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getString(R.string.set));
    }

    private void initListener() {
        mrlVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoUpdateUtil.getAutoInfo(SetActivity.this, "iLoan_mx.apk", "iLoan_mx.apk", "", PackageManagerUtil.getVersionCode(SetActivity.this.getApplicationContext()),false);
            }
        });
        mllForgetPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SetActivity.this, ForgetLoginPasswdActivity.class);
                startActivity(intent);
            }
        });
        mllForgetPayPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPresenter.checkPayPasswd(Constant.FORGET_APY_PASS);
            }
        });
        mllModifyPayPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPresenter.checkPayPasswd(Constant.MIDIFY_APY_PASS);

            }
        });
        mllModifyPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetActivity.this, UpdateLoginPasswdActivity.class);
                startActivity(intent);
            }
        });
        mllQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mllQuit.postDelayed(new Runnable() {
                    @Override
                    public void run() {*/
                        // 结束activity队列中的所有activity

                        setPresenter.logout();
                 /*   }
                },400);*/

            }
        });
        /**
         * 关于还款
         */
        mllAboutRepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SetActivity.this,WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_ABOUT_MONNEY_PATH);
                startActivity(intent);
            }
        });
        /**
         * 关于我们
         */
        mllAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SetActivity.this,WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_ABOUT_US_PATH);
                startActivity(intent);
            }
        });
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return setPresenter = new SetPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return mtoolbar;
    }

    @Override
    public void LogoutSucess() {
        Constant.LOGIN_OUT = 1;
        if (AppStaus.activitielists != null) {
            Iterator<Activity> it =AppStaus.activitielists.iterator();
            while (it.hasNext()){

                Activity ac=it.next();
                if(ac.getClass().equals(SetActivity.this.getClass())){
                    continue;
                }
                it.remove();
                ac.finish();
            }
        }
        Intent intent = new Intent(SetActivity.this, LoginLoadActivity.class);
        startActivity(intent);
    }

    @Override
    public void checkBindCardResult(int type) {
        if (type == Constant.FORGET_APY_PASS) {
            Intent intent = new Intent(SetActivity.this, ForgetPayPasswdActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SetActivity.this, UpdatePayPasswdActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void noBindCardResult() {
        ToastUtil.showToast(getString(R.string.no_bindcard_setpasswd));
    }
}
