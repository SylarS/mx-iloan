package com.panshi.hujin2.iloan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.ForgetPresenter;
import com.panshi.hujin2.iloan.util.BoolUtil;
import com.panshi.hujin2.iloan.util.InputUtil;
import com.panshi.hujin2.iloan.util.StringUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.ForgetView;
import com.panshi.hujin2.iloan.view.widget.VerificationCodeView;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public class ForgetPayPasswdActivity extends MVPBaseActivity implements ForgetView {
    private Toolbar mtoobar;
    private ForgetPresenter forgetPresenter;
    private EditText meditPw1;
    private EditText meditPw2;
    private EditText meditPw3;
    private EditText meditPw4;
    private Button mbtnSet;
    private EditText meditPasswd;
    private EditText meditRePasswd;
    private TextView mtvPhoneNum;
    private VerificationCodeView verificationCodeView;
    private int num = 4;
    private int type;
    private String vertifyCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pay_passwd);
        forgetPresenter.getVertifyCode(AppStaus.userInfo.getPhoneNumber(),AppStaus.userInfo.getAreaCode(),Constant.VERTIFY_PAY);
        type=getIntent().getIntExtra("type",-1);
        initView();
        initListener();
    }


    private void initView() {
        mtoobar = (Toolbar) findViewById(R.id.toolbar);
        meditPw1 = (EditText) findViewById(R.id.edit_ps1);
        meditPw2 = (EditText) findViewById(R.id.edit_ps2);
        meditPw3 = (EditText) findViewById(R.id.edit_ps3);
        meditPw4 = (EditText) findViewById(R.id.edit_ps4);
        meditPasswd = (EditText) findViewById(R.id.edit_passwd);
        meditRePasswd = (EditText) findViewById(R.id.edit_re_passwd);
        mtvPhoneNum=(TextView)findViewById(R.id.phone_num);
        mbtnSet = (Button) findViewById(R.id.btn_set);
        verificationCodeView = (VerificationCodeView) findViewById(R.id.verificationcodeview);
        mtoobar.setTitle(getResources().getString(R.string.forget_pay_passwd));
        if(AppStaus.userInfo!=null){
            mtvPhoneNum.setText(StringUtil.dealPhone(AppStaus.userInfo.getPhoneNumber()));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getResources().getString(R.string.forget_pay_passwd));
    }

    private void initListener() {
        verificationCodeView.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onComplete(String content) {
                vertifyCode=content;
                checkComplete();
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
                checkComplete();
            }
        });
        meditPasswd.addTextChangedListener(new TextWatcher() {
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
        meditPw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = meditPw1.getText();
                int len = editable.length();
                if (len > num) {
                    ToastUtil.showToast(getString(R.string.char_limt));
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, num);
                    meditPw1.setText(newStr);
                    editable = meditPw1.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable editable = meditPw1.getText();
                int len = editable.length();
                if (len == 1) {
                    InputUtil.hideSoftInput(ForgetPayPasswdActivity.this, meditPw1);

                    InputUtil.showSoftInput(ForgetPayPasswdActivity.this, meditPw2);


                }
                     checkComplete();
            }
        });
        meditPw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = meditPw2.getText();
                int len = editable.length();
                if (len > num) {
                    ToastUtil.showToast(getString(R.string.char_limt));
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, num);
                    meditPw2.setText(newStr);
                    editable = meditPw2.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable editable = meditPw2.getText();
                int len = editable.length();
                if (len == 1) {
                    InputUtil.hideSoftInput(ForgetPayPasswdActivity.this, meditPw2);
                    InputUtil.showSoftInput(ForgetPayPasswdActivity.this, meditPw3);
                }
                  checkComplete();
            }
        });
        meditPw3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = meditPw3.getText();
                int len = editable.length();
                if (len > num) {
                    ToastUtil.showToast(getString(R.string.char_limt));
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, num);
                    meditPw3.setText(newStr);
                    editable = meditPw3.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable editable = meditPw3.getText();
                int len = editable.length();
                if (len == 1) {
                    InputUtil.hideSoftInput(ForgetPayPasswdActivity.this, meditPw3);
                    InputUtil.showSoftInput(ForgetPayPasswdActivity.this, meditPw4);
                }
                     checkComplete();
            }
        });
        meditPw4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = meditPw4.getText();
                int len = editable.length();
                if (len > num) {
                    ToastUtil.showToast(getString(R.string.char_limt));
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, num);
                    meditPw4.setText(newStr);
                    editable = meditPw4.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable editable = meditPw4.getText();
                int len = editable.length();
                if (len == 1) {
                    InputUtil.hideSoftInput(ForgetPayPasswdActivity.this, meditPw4);
                    // InputUtil.showSoftInput(ForgetPasswdActivity.this,meditPw4);
                }
                checkComplete();
            }
        });
        mbtnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   String pw1 = meditPw1.getText().toString().trim();
                String pw2 = meditPw2.getText().toString().trim();
                String pw3 = meditPw3.getText().toString().trim();
                String pw4 = meditPw4.getText().toString().trim();*/
                String passwd = meditPasswd.getText().toString().trim();
                String rePasswd = meditRePasswd.getText().toString().trim();
                if (!TextUtils.isEmpty(vertifyCode)) {
                    if (!TextUtils.isEmpty(passwd) && !TextUtils.isEmpty(rePasswd)) {
                        if (!TextUtils.equals(passwd, rePasswd)) {
                            ToastUtil.showToast(getString(R.string.old_new_no_mathine));
                            return;
                        }

                        /*if (passwd.matches(Constant.REGEX)) {*/
                            if (BoolUtil.fastDouble(ForgetPayPasswdActivity.this)) {
                                return;
                            }
                        if(passwd.length()==6){
                            forgetPresenter.forget(Constant.PAY_PASSWD,passwd, vertifyCode, AppStaus.userInfo.getPhoneNumber(),AppStaus.userInfo.getAreaCode());
                        }else {
                            ToastUtil.showToast(getResources().getString(R.string.input_pay_6_passwd));
                        }
                          /*  StringBuilder sb = new StringBuilder();
                            sb.append(pw1).append(pw2).append(pw3).append(pw4);*/

                       /* } else {
                            ToastUtil.showToast(getString(R.string.pw_len_error));
                        }*/

                    } else {
                        ToastUtil.showToast(getString(R.string.passwd_empty));
                    }

                } else {
                    ToastUtil.showToast(getString(R.string.vertify_code_empty));
                }
            }
        });
    }

    public void checkComplete() {
       /* String pw1 = meditPw1.getText().toString().trim();
        String pw2 = meditPw2.getText().toString().trim();
        String pw3 = meditPw3.getText().toString().trim();
        String pw4 = meditPw4.getText().toString().trim();*/
        if (!TextUtils.isEmpty(vertifyCode)) {
            String passwd = meditPasswd.getText().toString().trim();
            String rePasswd = meditRePasswd.getText().toString().trim();
            if(!TextUtils.isEmpty(passwd)&&!TextUtils.isEmpty(rePasswd)){
               mbtnSet.setBackgroundResource(R.drawable.login_btn_shape);
               // ApkUtil.setBtnColor(ForgetPayPasswdActivity.this,mbtnSet);
            }else {
                mbtnSet.setBackgroundResource(R.drawable.regit_shape);
            }
        }else {
            mbtnSet.setBackgroundResource(R.drawable.regit_shape);
        }

    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return forgetPresenter = new ForgetPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return mtoobar;
    }

    @Override
    public void setPasswdSucess() {
        Intent intent=new Intent(ForgetPayPasswdActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
