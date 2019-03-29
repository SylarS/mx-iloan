package com.panshi.hujin2.iloan.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.LoginLoadPresenter;
import com.panshi.hujin2.iloan.util.StatusBarUtil;

import java.util.Iterator;

public class LoginLoadActivity extends MVPBaseActivity {
    private LoginLoadPresenter loginLoadPresenter;
    private Button mbtnLogin;
    private Button mbtnRegit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_load);
        initView();
        initListener();
        StatusBarUtil.setColor(this,Color.WHITE);
    }

    private void initView() {
        mbtnLogin = findViewById(R.id.btn_login);
        mbtnRegit = findViewById(R.id.btn_regit);
        findViewById(R.id.rl_back).setVisibility(View.GONE);
    }

    private void initListener() {
        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginLoadActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        mbtnRegit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginLoadActivity.this, RegiterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Constant.LOGIN_OUT == 1) {
            Constant.LOGIN_OUT = 0;
            /*Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);*/
            if (AppStaus.activitielists != null) {
                Iterator<Activity> it = AppStaus.activitielists.iterator();
                while (it.hasNext()) {
                    Activity ac = it.next();
                    it.remove();
                    ac.finish();
                }
            }
            System.exit(0);
            //System.exit(0);
            //  android.os.Process.killProcess(android.os.Process.myPid());

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return loginLoadPresenter = new LoginLoadPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }
}
