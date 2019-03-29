package com.panshi.hujin2.iloan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.SetRegitPasswdPresenter;
import com.panshi.hujin2.iloan.util.BoolUtil;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.RegitView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class SetRegitPasswdActivity extends MVPBaseActivity implements RegitView {
    private SetRegitPasswdPresenter setRegitPasswdPresenter;
    private Toolbar mtoolbar;
    private Button mbtnSure;
    private EditText meditPasswd;
    private EditText meditRePasswd;
    private String areaCode;
    private String phoneNum;
    private String vertifyCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_user_passwd);
        areaCode = getIntent().getStringExtra("areaCode");
        phoneNum = getIntent().getStringExtra("PhoneNum");
        vertifyCode = getIntent().getStringExtra("yanma");
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getString(R.string.regit));
    }

    private void initView() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mbtnSure = (Button) findViewById(R.id.btn_confir);
        meditPasswd = (EditText) findViewById(R.id.edit_passwd);
        meditRePasswd = (EditText) findViewById(R.id.edit_re_passwd);
    }

    private void initListener() {
        meditPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                complete();
            }
        });
        meditRePasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                complete();
            }
        });
        mbtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd = meditPasswd.getText().toString().trim();
                String rePasswd = meditRePasswd.getText().toString().trim();
                if (!TextUtils.isEmpty(passwd) && !TextUtils.isEmpty(rePasswd)) {
                    if (!TextUtils.equals(passwd, rePasswd)) {
                        ToastUtil.showToast(getString(R.string.old_new_no_mathine));
                        return;
                    }
                    if (passwd.matches(Constant.REGEX)) {
                        if (BoolUtil.fastDouble(SetRegitPasswdActivity.this)) {
                            return;
                        }
                        Intent intent=new Intent(SetRegitPasswdActivity.this,InviateCodeActivity.class);
                        intent.putExtra("areaCode",areaCode);
                        intent.putExtra("phoneNum",phoneNum);
                        intent.putExtra("passwd",passwd);
                        intent.putExtra("vertifyCode",vertifyCode);
                        startActivity(intent);
                    } else {
                        ToastUtil.showToast(getString(R.string.pw_len_error));
                    }

                } else {
                    ToastUtil.showToast(getString(R.string.passwd_empty));
                }
            }
        });
    }
   public void complete(){
       String passwd = meditPasswd.getText().toString().trim();
       String rePasswd = meditRePasswd.getText().toString().trim();
       if(!TextUtils.isEmpty(passwd)&&!TextUtils.isEmpty(rePasswd)){
           mbtnSure.setBackgroundResource(R.drawable.buttun_index_shape);
       }else{
           mbtnSure.setBackgroundResource(R.drawable.regit_shape);
       }
   }
    @Override
    protected BasePresenter oncreatePresenter() {
        return setRegitPasswdPresenter = new SetRegitPasswdPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return mtoolbar;
    }

    @Override
    public void regitSucess() {
        Constant.LOGIN_OUT=0;
        logCompletedRegistrationEvent();
        Intent intent = new Intent(SetRegitPasswdActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void yanma(long second) {

    }

    @Override
    public void checkVertifyCodeSucess() {

    }

    @Override
    public void regited() {

    }

    @Override
    public void noRegit() {

    }

    @Override
    public void noRegitFouse() {

    }



    /* *//**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
   /* public void logCompletedRegistrationEvent (String registrationMethod) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, registrationMethod);
        AppEventsLogger.newLogger(this).logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, params);
    }*/
   public void  logCompletedRegistrationEvent(){
       Map<String, Object> eventValue = new HashMap<String, Object>();
       eventValue.put(AFInAppEventParameterName.LEVEL,9);
       eventValue.put(AFInAppEventParameterName.SCORE,100);
       AppsFlyerLib.getInstance().trackEvent(this.getApplicationContext(),AFInAppEventType.COMPLETE_REGISTRATION,eventValue);
       DebugLog.i("wang","记录注册信息");
   }
}
