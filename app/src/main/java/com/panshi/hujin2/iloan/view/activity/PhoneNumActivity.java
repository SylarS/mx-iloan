package com.panshi.hujin2.iloan.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.PhoneNumPresenter;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.DeviceUtil;
import com.panshi.hujin2.iloan.util.LocationUtil;
import com.panshi.hujin2.iloan.util.StringUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.PhoneView;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Iterator;

import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public class PhoneNumActivity extends MVPBaseActivity implements PhoneView {
    private EditText meditPhoneNum;
    private TextView mtvArea;
    private Button mbtnNext;
    private String areaCode = "55";
    private PhoneNumPresenter phoneNumPresenter;
    private Toolbar mtoolbar;

    private String phoneNum;
    private RxPermissions rxPermissions;
    private boolean isLocationPermission = true;
    private boolean isPhoneState = true;
    private boolean isOpenGps;
    private int GPS_REQUEST_CODE = 10;
    private LocationListener locationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_activity);
        rxPermissions = new RxPermissions(this);
        locationListener = new LocationUtil.MyLocation(this.getApplicationContext());
        AppsFlyerLib.getInstance().sendDeepLinkData(this);
        initView();
        initListener();
        readLocation();
        readPhoneState();
    }

    private void initView() {
        meditPhoneNum = (EditText) findViewById(R.id.edit_phone_num);
        mbtnNext = (Button) findViewById(R.id.btn_next);
//        ApkUtil.setBtnColor(this, mbtnNext);
        mtvArea = (TextView) findViewById(R.id.tv_area);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);

        mtvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneNumActivity.this, LocationNumberActivity.class);
                startActivityForResult(intent, Constant.REQUEST_COUNTRY_CODE);
            }
        });
        ImageView mingLoginIcon = (ImageView) findViewById(R.id.imageView);
        ImageView mimgTitle = (ImageView) findViewById(R.id.img_title);
        RelativeLayout rlLoginBg = (RelativeLayout) findViewById(R.id.login_bg);

    }

    private void readPhoneState() {
        rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE).subscribe(new CusumeObserver<Permission>() {
            @Override
            public void onNext(@NonNull Permission permission) {
                super.onNext(permission);
                if (permission.granted) {
                    AppStaus.MAC = DeviceUtil.getUniqueID(PhoneNumActivity.this);
                    DeviceUtil.getTelPhone(PhoneNumActivity.this);
                    String[] country = StringUtil.getCountryZipCode(PhoneNumActivity.this);
                    areaCode = country[0];
                    mtvArea.setText(country[1] + " " + areaCode);
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

    private void initListener() {

        meditPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //  meditPhoneNum.setText(areaCode);
                } else {
                    meditPhoneNum.setHint(getString(R.string.input_phone_num));
                }
            }
        });
        mbtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                String phoneNumCode = meditPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumCode)) {
                    ToastUtil.showToast(getString(R.string.input_phone_num));
                    return;
                }

            /*    int len = 0;
                if (phoneNumCode.contains("+")) {
                    len = 3;
                } else {
                    len = 2;
                }*/
            /*    if (phoneNumCode.contains("-")) {
                    String[] args = phoneNumCode.split("-");
                    phoneNum = args[1];
                    if (args[0].startsWith("+")) {
                        areaCode = args[0].substring(1, args.length + 1);
                    }

                } else {*/
                phoneNum = phoneNumCode;
                //    }

                if (phoneNum.length() > 11 || phoneNum.length() < 8) {
                    ToastUtil.showToast(getString(R.string.phone_num_error));
                    return;
                }
             /*   if (BoolUtil.fastDouble(PhoneNumActivity.this)) {
                    return;
                }*/
                if (!isOpenGps) {
                    //跳转GPS设置界面

                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, GPS_REQUEST_CODE);
                    // LocationUtil.openGPS(PhoneNumActivity.this);
                    ToastUtil.showToast(getString(R.string.set_open_gps));
                    return;
                }
                DebugLog.i("wang", "==areaCode=" + areaCode + "||==" + phoneNum);
                phoneNumPresenter.checkRegit(areaCode, phoneNum);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DebugLog.i("wang","====onrResume====");

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
                            if (LocationUtil.isGPSOPen(PhoneNumActivity.this)) {
                                isOpenGps = true;
                                //   LocationUtil.isGPSOPen(PhoneNumActivity.this);
                                LocationUtil.getLngAndLat(PhoneNumActivity.this.getApplicationContext(), locationListener);
                            } else {
                                isOpenGps = false;
                            }
                        } else {
                            isLocationPermission = false;
                            if(!permission.shouldShowRequestPermissionRationale){
                                ToastUtil.showToast(getString(R.string.no_location_permission));
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        LocationUtil.unRegitLocation(this.getApplicationContext(), locationListener);
        super.onStop();
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return phoneNumPresenter = new PhoneNumPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return mtoolbar;
    }

    @Override
    public void regited() {
        Intent intent = new Intent(PhoneNumActivity.this, LoginActivity.class);
        intent.putExtra("areaCode", areaCode);
        intent.putExtra("phoneNum", phoneNum);
        startActivity(intent);
    }

    @Override
    public void noRegit() {
        Intent intent = new Intent(PhoneNumActivity.this, RegiterActivity.class);
        intent.putExtra("areaCode", areaCode);
        intent.putExtra("phoneNum", phoneNum);
        startActivity(intent);
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
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_COUNTRY_CODE && resultCode == Constant.RESONSE_COUNTRY_CODE) {
            if (data != null) {
                areaCode = data.getStringExtra("countryCode");
                mtvArea.setText(data.getStringExtra("countryName") + " " + areaCode);
            }
        }
    }
}
