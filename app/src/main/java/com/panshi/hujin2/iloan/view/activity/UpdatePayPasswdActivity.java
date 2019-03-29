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
import android.widget.TextView;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.UpdatePasswdPresenter;
import com.panshi.hujin2.iloan.util.BoolUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.UpdatePasswdView;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class UpdatePayPasswdActivity extends MVPBaseActivity implements UpdatePasswdView {
    private Button mbtnSet;
    private EditText meditOldPasswd;
    private EditText meditNewPasswd;
    private EditText meditReNewPasswd;
    private Toolbar mtoolbar;
    private UpdatePasswdPresenter updatePasswdPresenter;
    private TextView mtvForgetPayPw;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pay_passwd);
        initView();
        initListener();
    }

    private void initView() {
        mbtnSet = (Button) findViewById(R.id.btn_set);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        meditOldPasswd = (EditText) findViewById(R.id.edit_old_passwd);
        meditNewPasswd = (EditText) findViewById(R.id.edit_new_passwd);
        meditReNewPasswd = (EditText) findViewById(R.id.edit_re_new_passwd);
        mtvForgetPayPw = (TextView) findViewById(R.id.tv_forget_pay_pw);
        mtoolbar.setTitle(getResources().getString(R.string.modify_pay_passwd));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getResources().getString(R.string.modify_pay_passwd));
    }

    private void initListener() {
        mtvForgetPayPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent=new Intent(UpdatePayPasswdActivity.this,ForgetPayPasswdActivity.class);
                 startActivity(intent);
            }
        });
        meditOldPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkComplete();
            }
        });
        meditNewPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkComplete();
            }
        });
        meditReNewPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkComplete();
            }
        });
        mbtnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd = meditNewPasswd.getText().toString().trim();
                String rePasswd = meditReNewPasswd.getText().toString().trim();
                String oldPasswd = meditOldPasswd.getText().toString().trim();
                if (!TextUtils.isEmpty(passwd) && !TextUtils.isEmpty(rePasswd) && !TextUtils.isEmpty(oldPasswd)) {
                    if (!TextUtils.equals(passwd, rePasswd)) {
                        ToastUtil.showToast(getString(R.string.old_new_no_mathine));
                        return;
                    }
                    if (TextUtils.equals(passwd, oldPasswd)) {
                        ToastUtil.showToast(getString(R.string.old_new_same));
                        return;
                    }
                  /*  if (passwd.matches(Constant.REGEX)) {*/
                    if (BoolUtil.fastDouble(UpdatePayPasswdActivity.this)) {
                        return;
                    }
                    if(passwd.length()==6){
                        updatePasswdPresenter.updatePasswd(Constant.PAY_PASSWD, oldPasswd, passwd);
                    }else{
                        ToastUtil.showToast(getResources().getString(R.string.input_pay_6_passwd));
                    }
                    //     setRegitPasswdPresenter.regit("", areaCode, phoneNum, passwd, vertifyCode);
                 /*   } else {
                        ToastUtil.showToast(getString(R.string.pw_len_error));
                    }*/

                } else {
                    ToastUtil.showToast(getString(R.string.passwd_empty));
                }

            }
        });
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return updatePasswdPresenter = new UpdatePasswdPresenter();
    }

    private void checkComplete() {
        String passwd = meditNewPasswd.getText().toString().trim();
        String rePasswd = meditReNewPasswd.getText().toString().trim();
        String oldPasswd = meditOldPasswd.getText().toString().trim();
        if (!TextUtils.isEmpty(passwd) && !TextUtils.isEmpty(rePasswd) && !TextUtils.isEmpty(oldPasswd)) {
            mbtnSet.setBackgroundResource(R.drawable.buttun_index_shape);
        } else {
            mbtnSet.setBackgroundResource(R.drawable.regit_shape);
        }
    }

    @Override
    protected Toolbar getToolbar() {
        return mtoolbar;
    }

    @Override
    public void updateSucess() {
        ToastUtil.showToast(getString(R.string.update_passwd_sucess));
        Intent intent = new Intent(UpdatePayPasswdActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
