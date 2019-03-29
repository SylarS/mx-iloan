package com.panshi.hujin2.iloan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.SetRegitPasswdPresenter;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.RegitView;

import java.util.HashMap;
import java.util.Map;

public class InviateCodeActivity extends MVPBaseActivity implements RegitView {
    private EditText mtvEditInviteCode;
    private Button mbtnFinishi;
    private TextView mtvPass;
    private String areaCode; //区位号
    private String phoneNum; //手机号
    private String vertifyCode;//验证码
    private String passwd;//密码
    private SetRegitPasswdPresenter setRegitPasswdPresenter;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitecode_activity);
        areaCode = getIntent().getStringExtra("areaCode");
        phoneNum = getIntent().getStringExtra("phoneNum");
        vertifyCode = getIntent().getStringExtra("vertifyCode");
        passwd = getIntent().getStringExtra("passwd");
        initView();
        initListener();
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return setRegitPasswdPresenter = new SetRegitPasswdPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return mtoolbar;
    }

    private void initView() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtvEditInviteCode = findViewById(R.id.edit_invite_code);
        mbtnFinishi = findViewById(R.id.btn_finishi);
        mtvPass = findViewById(R.id.tv_next);
    }

    private void initListener() {
        mtvEditInviteCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = mtvEditInviteCode.getText().toString().trim();
                if (!TextUtils.isEmpty(code)) {
                    mbtnFinishi.setBackgroundResource(R.drawable.buttun_index_shape);
                }else{
                    mbtnFinishi.setBackgroundResource(R.drawable.inviate_code_btn);
                }
            }
        });
        mbtnFinishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mtvEditInviteCode.getText().toString().trim();
                if (!TextUtils.isEmpty(code) && code.length() == 6) {
                    setRegitPasswdPresenter.regit("", areaCode, phoneNum, passwd, vertifyCode, code);
                } else {
                    ToastUtil.showToast(getString(R.string.input_inivte_6code));
                }
            }
        });
        mtvPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRegitPasswdPresenter.regit("", areaCode, phoneNum, passwd, vertifyCode, "");
            }
        });
    }


    @Override
    public void regitSucess() {
        Constant.LOGIN_OUT = 0;
        logCompletedRegistrationEvent();
        Intent intent = new Intent(InviateCodeActivity.this, MainActivity.class);
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

    public void logCompletedRegistrationEvent() {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.LEVEL, 9);
        eventValue.put(AFInAppEventParameterName.SCORE, 100);
        AppsFlyerLib.getInstance().trackEvent(this.getApplicationContext(), AFInAppEventType.COMPLETE_REGISTRATION, eventValue);
        DebugLog.i("wang", "记录注册信息");
    }
}
