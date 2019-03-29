package com.panshi.hujin2.iloan.view.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.model.SharePrefrencesModel;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.RegitPresenter;
import com.panshi.hujin2.iloan.util.BoolUtil;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.DeviceUtil;
import com.panshi.hujin2.iloan.util.LocationUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.RegitView;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public class RegiterActivity extends MVPBaseActivity implements RegitView {
    private Button mbtnRegit;
    private RegitPresenter regitPresenter;
    private Toolbar mtoolbar;
    private EditText meditYanma;
    private TextView mtvArea;
    private TextView mtvYanma;
    private RxPermissions rxPermissions;
    private boolean isAllow = true;
    private CheckBox mcheckProtrol;
    private TextView mtvCheckProtrol1;
    private TextView mtvPhoneNum;
    private TextView mtvPasswd;
    private String yanma;
    private String area;
    private boolean isPhoneState = false;
    private TextView mtvLoginInto;
    private int isRegit = -1;
    private LocationListener locationListener;
    private boolean isLocationPermission = true;
    private boolean isOpenGps;
    private int GPS_REQUEST_CODE = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regit_activity);
        rxPermissions = new RxPermissions(this);
        area = "52";
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
        mbtnRegit = findViewById(R.id.btn_regit);
        mtoolbar = findViewById(R.id.toolbar);
        meditYanma = findViewById(R.id.edit_yanma);
        mtvArea = findViewById(R.id.tv_area);
        mtvYanma = findViewById(R.id.tv_get_vertify_code);
        mcheckProtrol = findViewById(R.id.cb_check_protrol);
        mtvPasswd = findViewById(R.id.edit_input_phone_pw);
        mtvCheckProtrol1 = findViewById(R.id.tv_protrol);
        mtvPhoneNum = findViewById(R.id.edit_input_phone_num);
        mtvLoginInto = findViewById(R.id.tv_into_login);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.regit));
        mtvArea.setText("+" + area);
        String loginNote = "Ha tenido cuenta,vaya a iniciar sesión";
        SpannableString str = new SpannableString(loginNote);
        mtvLoginInto.setHighlightColor(getResources().getColor(android.R.color.transparent));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegiterActivity.this, LoginActivity.class);
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
        str.setSpan(clickableSpan, 17, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //str.setSpan(textColor,17,str.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mtvLoginInto.setText(str);
        mtvLoginInto.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initListener() {
        mtvCheckProtrol1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mtvCheckProtrol1.setText(getResources().getString(R.string.regit_agree_proctrol1, getResources().getString(R.string.app_name)));
        mtvCheckProtrol1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegiterActivity.this, WebViewBannerActivity.class);
                intent.putExtra("urlPath", Constant.HTML_REGIT_PROTTROL_REGIT);
                startActivity(intent);
            }
        });
        mtvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegiterActivity.this, LocationNumberActivity.class);
                startActivityForResult(intent, Constant.REQUEST_COUNTRY_CODE);
            }
        });
        mbtnRegit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yanma = meditYanma.getText().toString().trim();
                String phoneNum = mtvPhoneNum.getText().toString().trim();
                String passwd = mtvPasswd.getText().toString().trim();
                if (!isPhoneState) {
                    //  ToastUtil.showToast(getString(R.string.no_phone_state_permission));
                    readPhoneState();
                    return;
                }
                if (!isLocationPermission) {
                    // ToastUtil.showToast(getString(R.string.no_location_permission));
                    readLocation();
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

                if (isAllow) {
                    if (!mcheckProtrol.isChecked()) {
                        ToastUtil.showToast(getString(R.string.agree_regit_protrol));
                        return;
                    }
                    if (TextUtils.isEmpty(phoneNum)) {
                        ToastUtil.showToast(getString(R.string.input_phone_num));
                    }
                    if (TextUtils.isEmpty(passwd)) {
                        ToastUtil.showToast(getString(R.string.passwd_empty));
                        return;
                    }

                    if (!TextUtils.isEmpty(yanma)) {
                        regitPresenter.regit("", area, phoneNum, passwd, yanma, "");
                    } else {
                        ToastUtil.showToast(RegiterActivity.this.getString(R.string.please_input_yanma));
                    }

                } else {
                    ToastUtil.showToast(getString(R.string.set_sms_permission));
                }

            }
        });
        meditYanma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mbtnRegit.setBackgroundResource(R.drawable.login_btn_shape);
                } else {
                    mbtnRegit.setBackgroundResource(R.drawable.regit_shape);
                }
            }
        });
        mtvYanma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BoolUtil.fastDouble(RegiterActivity.this)) {
                    return;
                }
                //       mtvYanma.setClickable(false);
                final String phoneNum = mtvPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    ToastUtil.showToast(getString(R.string.input_phone_num));
                    return;
                }
                if (phoneNum.length() > 11 || phoneNum.length() < 8) {
                    ToastUtil.showToast(getString(R.string.phone_num_error));
                    return;
                }
                if (isRegit == 1) {
                    ToastUtil.showToast(getResources().getString(R.string.phone_num_regited));
                    return;
                } else if (isRegit == -1) {
                    regitPresenter.checkRegitWithDialog(area, phoneNum);
                    return;
                }
                sendVetifySms(phoneNum);

            }

        });
        mtvPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mtvPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isRegit = -1;
                    DebugLog.i("wang", "==获取焦点===");
                } else {
                    DebugLog.i("wang", "==失去焦点===");
                    String phoneNum = mtvPhoneNum.getText().toString().trim();
                    if (phoneNum.length() > 11 || phoneNum.length() < 8) {
                        ToastUtil.showToast(getString(R.string.phone_num_error));
                        return;
                    }
                    regitPresenter.checkRegit(area, phoneNum);
                }
            }
        });
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return regitPresenter = new RegitPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return mtoolbar;
    }

    @Override
    public void regitSucess() {
        Constant.LOGIN_OUT = 0;
        logCompletedRegistrationEvent();
        Intent intent = new Intent(RegiterActivity.this, MainActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type", "regit");
        startActivity(intent);
    }


    @Override
    public void yanma(long second) {
        if (second == 0) {
            mtvYanma.setText(getResources().getString(R.string.get_yanma));
            mtvYanma.setClickable(true);
        } else {
            mtvYanma.setText(String.valueOf(second) + "s");
            mtvYanma.setClickable(false);
        }
    }

    @Override
    public void checkVertifyCodeSucess() {

    }

    @Override
    public void regited() {
        isRegit = 1;
        ToastUtil.showToast(getResources().getString(R.string.phone_num_regited));
    }

    @Override
    public void noRegit() {
        isRegit = 0;
    }

    @Override
    public void noRegitFouse() {
        isRegit = 0;
        String phoneNum = mtvPhoneNum.getText().toString().trim();
        sendVetifySms(phoneNum);
    }

    @Override
    public void showFailture(int code, String message) {
        super.showFailture(code, message);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_COUNTRY_CODE && resultCode == Constant.RESONSE_COUNTRY_CODE) {
            if (data != null) {
                area = data.getStringExtra("countryCode");
                mtvArea.setText("+" + area);
            }
        }
    }

    public void logCompletedRegistrationEvent() {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.LEVEL, 9);
        eventValue.put(AFInAppEventParameterName.SCORE, 100);
        AppsFlyerLib.getInstance().trackEvent(this.getApplicationContext(), AFInAppEventType.COMPLETE_REGISTRATION, eventValue);
        DebugLog.i("wang", "记录注册信息");
    }


    private void sendVetifySms(final String phoneNum) {
        rxPermissions.requestEach(Manifest.permission.READ_SMS).observeOn(AndroidSchedulers.mainThread()).subscribe(new CusumeObserver<Permission>() {
            @Override
            public void onNext(@NonNull Permission permission) {
                super.onNext(permission);
                if (permission.granted) {
                    regitPresenter.getVerifyCode(area, phoneNum);
                    uploadSMSInfo();
                    regitPresenter.doCountDwon(60);
                    ToastUtil.showToast(getString(R.string.sms_send_sucess));
                    isAllow = true;
                } else {
                    isAllow = false;
                    ToastUtil.showToast(getString(R.string.sms_send_failture));
                }
            }
        });
    }

    private void readPhoneState() {
        rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE).subscribe(new CusumeObserver<Permission>() {
            @Override
            public void onNext(@NonNull Permission permission) {
                super.onNext(permission);
              //  readLocation();
                if (permission.granted) {
                    AppStaus.MAC = DeviceUtil.getUniqueID(RegiterActivity.this);
                    DeviceUtil.getTelPhone(RegiterActivity.this);

                    isPhoneState = true;
                } else {
                    isPhoneState = false;
                    DebugLog.i("wang", "=====phoneState=====" + permission.shouldShowRequestPermissionRationale);
                    if (!permission.shouldShowRequestPermissionRationale) {
                        ToastUtil.showToast(getString(R.string.no_phone_state_permission));
                    }
                }

            }
        });
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
                            if (LocationUtil.isGPSOPen(RegiterActivity.this)) {
                                isOpenGps = true;
                                //   LocationUtil.isGPSOPen(PhoneNumActivity.this);
                                LocationUtil.getLngAndLat(RegiterActivity.this.getApplicationContext(), locationListener);
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

    /**
     * 获取短信信息
     */
    private /*List<MesInfo>*/ void uploadSMSInfo() {
        DebugLog.i("wang", "====开始读取短信===");
        String[] field = {"_id", "address", "person", "body", "date", "type"};  //字段
        ContentResolver contentResolver = RegiterActivity.this.getContentResolver();  //内容解析器(数据共享)——读取短信内容
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/"), field, null, null, "date desc");  //获取短信内容
        if (cursor == null) {  //游标无内容
            return;
        }
        int num = 0;
        //  list = new ArrayList<MesInfo>();
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndex("address"));  //手机号 发件人地址，即手机号，如+8613811810000
            String name = cursor.getString(cursor.getColumnIndex("person"));  //联系人
            String body = cursor.getString(cursor.getColumnIndex("body"));  //短信内容
            //格式化短信日期
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type = cursor.getString(cursor.getColumnIndex("type"));//type 接受，2 发送
            num++;

        }
        if (num > 0) {
            AppStaus.msmsNum = num;
            SharePrefrencesModel sharePrefrencesModel = new SharePrefrencesModel(RegiterActivity.this);
            HashMap<String, Object> smsMap = new HashMap<String, Object>();
            smsMap.put("sms", AppStaus.msmsNum);
            sharePrefrencesModel.save(smsMap);
            DebugLog.i("wang", "==num==" + num);

        }
        return;
    }
}
