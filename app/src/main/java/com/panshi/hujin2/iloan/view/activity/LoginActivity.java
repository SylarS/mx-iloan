package com.panshi.hujin2.iloan.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.LoginPresenter;
import com.panshi.hujin2.iloan.util.BoolUtil;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.DeviceUtil;
import com.panshi.hujin2.iloan.util.LocationUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.LoginView;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;
import com.panshi.hujin2.iloan.view.widget.LoadingDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public class LoginActivity extends MVPBaseActivity implements LoginView {
    private LoginPresenter loginPresenter;
    private Toolbar mtoobar;
    private Button mbtnLogin;
    private String phoneNum;
    // private String areaCode;
    private EditText meditPasswd;
    private TextView mtvForgetPw;
    private Dialog loadingDialog;
    private TextView mtvRegitProctrol;
    private TextView mtvAera;
    private ImageView mingLoginIcon;
    private EditText meditPhoneNum;
    private boolean isOpenGps;
    private RxPermissions rxPermissions;
    private boolean isLocationPermission = true;
    private LocationListener locationListener;
    private int GPS_REQUEST_CODE = 10;
    private String areaCode = "52";
    private boolean isPhoneState=false;
    private boolean isRegit=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //   phoneNum = getIntent().getStringExtra("phoneNum");
        //   areaCode = getIntent().getStringExtra("areaCode");
        rxPermissions = new RxPermissions(this);
        locationListener = new LocationUtil.MyLocation(this.getApplicationContext());
        initView();
        initListener();

        readPhoneState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readLocation();
    }

    private void initView() {
        mtoobar = (Toolbar) findViewById(R.id.toolbar);
        mbtnLogin = (Button) findViewById(R.id.btn_login);
        meditPasswd = (EditText) findViewById(R.id.edit_passwd);
        mtvForgetPw = (TextView) findViewById(R.id.tv_forget_pw);
        mtvRegitProctrol = (TextView) findViewById(R.id.tv_protrol);
        mingLoginIcon = (ImageView) findViewById(R.id.imageView);
        TextView mtvRegited=findViewById(R.id.tv_regited);
        RelativeLayout rlLoginBg = (RelativeLayout) findViewById(R.id.login_bg);
        ImageView mimgTitle = (ImageView) findViewById(R.id.img_title);
        meditPhoneNum = (EditText) findViewById(R.id.edit_phone_num);
        mtvAera = (TextView) findViewById(R.id.tv_area);
        mtvAera.setText("+"+areaCode);
        mtvRegitProctrol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mtvRegitProctrol.setText(getResources().getString(R.string.regit_agree_proctrol1,getResources().getString(R.string.app_name)));
        String loginNote="No tiene cuenta,vaya a registrase";
        SpannableString str=new SpannableString(loginNote);
        mtvRegited.setHighlightColor(getResources().getColor(android.R.color.transparent));
        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent=new Intent(LoginActivity.this,RegiterActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue_txt));
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        };
        //StrikethroughSpan strikethroughSpan=new StrikethroughSpan();
        // ForegroundColorSpan textColor=new ForegroundColorSpan(getResources().getColor(R.color.blue_txt));
        str.setSpan(clickableSpan,16,str.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //str.setSpan(textColor,17,str.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mtvRegited.setText(str);
        mtvRegited.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initListener() {
        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String passed = meditPasswd.getText().toString().trim();
                String phoneNumCode = meditPhoneNum.getText().toString().trim();
                phoneNum = phoneNumCode;
                if (TextUtils.isEmpty(phoneNumCode)) {
                    ToastUtil.showToast(getString(R.string.input_phone_num));
                    return;
                }
                if (phoneNumCode.length() > 11 || phoneNumCode.length() < 8) {
                    ToastUtil.showToast(getString(R.string.phone_num_error));
                    return;
                }
                if (!isLocationPermission) {
                    // ToastUtil.showToast(getString(R.string.no_location_permission));
                    readLocation();
                    return;
                }

                if (!isPhoneState) {
                    //  ToastUtil.showToast(getString(R.string.no_phone_state_permission));
                    readPhoneState();
                    return;
                }
                if (!isOpenGps) {
                    //跳转GPS设置界面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, GPS_REQUEST_CODE);
                    // LocationUtil.openGPS(PhoneNumActivity.this);
                    ToastUtil.showToast(getString(R.string.set_open_gps));
                    return;
                }
                if (TextUtils.isEmpty(passed)) {
                    ToastUtil.showToast(getString(R.string.input_passwd));
                    return;
                }

                if (passed.matches(Constant.REGEX)) {
                    if (BoolUtil.fastDouble(LoginActivity.this)) {
                        return;
                    }
                    loginPresenter.login(phoneNumCode, passed, areaCode);
                } else {
                    ToastUtil.showToast(getString(R.string.pw_len_error));
                }

            }
        });

        meditPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //  meditPhoneNum.setText(areaCode);
                } else {
                    meditPhoneNum.setHint(getString(R.string.input_phone_num));
                    String phoneNum=meditPhoneNum.getText().toString().trim();
                    if (phoneNum.length() > 11 || phoneNum.length() < 8) {
                        ToastUtil.showToast(getString(R.string.phone_num_error));
                        return;
                    }
                    loginPresenter.checkRegit(areaCode,phoneNum);
                }
            }
        });
        mtvAera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LocationNumberActivity.class);
                startActivityForResult(intent, Constant.REQUEST_COUNTRY_CODE);
            }
        });
        mtvForgetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum=meditPhoneNum.getText().toString().trim();
                if (phoneNum.length() > 11 || phoneNum.length() < 8) {
                    ToastUtil.showToast(getString(R.string.phone_num_error));
                    return;
                }
                if (!TextUtils.isEmpty(phoneNum)) {
                    Intent intent = new Intent(LoginActivity.this, ForgetLoginPasswdActivity.class);
                    intent.putExtra("type", Constant.LOGIN_PASSWD);
                    intent.putExtra("areaCode", areaCode);
                    intent.putExtra("phoneNum", phoneNum);
                    startActivity(intent);
                } else {
                    ToastUtil.showToast(getString(R.string.input_phone_num));
                }
            }
        });
        mtvRegitProctrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WebViewBannerActivity.class);
                intent.putExtra("urlPath", Constant.HTML_REGIT_PROTTROL);
                startActivity(intent);
            }
        });

    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return loginPresenter = new LoginPresenter();
    }

    @Override
    public void showProgessDialog() {
        // super.showProgessDialog();
        loadingDialog = LoadingDialog.createLoadingDialog(LoginActivity.this, getResources().getString(R.string.login_loading));
        loadingDialog.show();
    }

    @Override
    public void hiddenProgessDialog() {
        //   super.hiddenProgessDialog();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void readLocation() {
        rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new CusumeObserver<Permission>() {
                    @Override
                    public void onNext(@NonNull Permission permission) {
                        super.onNext(permission);
                        DebugLog.i("wang", "==p===" + permission.granted);
                        if (permission.granted) {
                            isLocationPermission = true;
                            if (LocationUtil.isGPSOPen(LoginActivity.this)) {
                                isOpenGps = true;
                                //   LocationUtil.isGPSOPen(PhoneNumActivity.this);
                                LocationUtil.getLngAndLat(LoginActivity.this.getApplicationContext(), locationListener);
                            } else {
                                isOpenGps = false;
                            }
                        } else {
                            isLocationPermission = false;
                            if (!permission.shouldShowRequestPermissionRationale) {
                                ToastUtil.showToast(getString(R.string.no_location_permission));
                            }
                        }
                    }
                });
    }
    private void readPhoneState() {
        rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE).subscribe(new CusumeObserver<Permission>() {
            @Override
            public void onNext(@NonNull Permission permission) {
                super.onNext(permission);
               // readLocation();
                if (permission.granted) {
                    AppStaus.MAC = DeviceUtil.getUniqueID(LoginActivity.this);
                    DeviceUtil.getTelPhone(LoginActivity.this);
                    //String[] country = StringUtil.getCountryZipCode(LoginActivity.this);
                    //areaCode = country[0];
                   // mtvArea.setText(country[1] + " " + areaCode);
                    isPhoneState = true;
                } else {
                    isPhoneState = false;
                    DebugLog.i("wang","=====phoneState====="+permission.shouldShowRequestPermissionRationale);
                    if(!permission.shouldShowRequestPermissionRationale){
                        ToastUtil.showToast(getString(R.string.no_phone_state_permission));
                    }
                }

            }
        });
    }

    @Override
    protected Toolbar getToolbar() {
        return mtoobar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_COUNTRY_CODE && resultCode == Constant.RESONSE_COUNTRY_CODE) {
            if (data != null) {
                areaCode = data.getStringExtra("countryCode");
                mtvAera.setText("+" + areaCode);
            }
        }
    }

    @Override
    public void loginSucess() {
        // logLoginSucessEvent();
        LoginSucessEvent();
        Constant.LOGIN_OUT = 0;
        ToastUtil.showToast(getString(R.string.login_sucess));
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //   AppEventsLogger.newLogger(this).logEvent();
        //  finish();

    }

    @Override
    public void regited() {
        isRegit=true;

    }

    @Override
    public void noRegit() {
        isRegit=false;
        ToastUtil.showToast(getResources().getString(R.string.phone_num_no_regited));
    }
    /*  */

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
   /* public void logLoginSucessEvent () {
        AppEventsLogger.newLogger(this).logEvent("login_sucess");
    }*/
    public void LoginSucessEvent() {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.LEVEL, 9);
        eventValue.put(AFInAppEventParameterName.SCORE, 100);
        AppsFlyerLib.getInstance().trackEvent(this.getApplicationContext(), AFInAppEventType.LOGIN, eventValue);
        DebugLog.i("wang", "=====loginSucessEvent===");
    }
}
