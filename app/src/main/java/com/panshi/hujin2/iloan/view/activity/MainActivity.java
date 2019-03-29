package com.panshi.hujin2.iloan.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.ActiveRes;
import com.panshi.hujin2.iloan.bean.MainDiscountCouponBean;
import com.panshi.hujin2.iloan.bean.MainDiscountCouponOuterBean;
import com.panshi.hujin2.iloan.factory.ImageLoaderFactory;
import com.panshi.hujin2.iloan.imageloader.IImageResult;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.MainPresenter;
import com.panshi.hujin2.iloan.util.AutoUpdateUtil;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.DensityUtil;
import com.panshi.hujin2.iloan.util.PackageManagerUtil;
import com.panshi.hujin2.iloan.view.ActiveFrament;
import com.panshi.hujin2.iloan.view.IndexFragment;
import com.panshi.hujin2.iloan.view.MyFragment;
import com.panshi.hujin2.iloan.view.interfaces.MainView;
import com.panshi.hujin2.iloan.view.widget.BaseDialog;
import com.panshi.hujin2.iloan.view.widget.CusumeBadgeView;
import com.panshi.hujin2.iloan.view.widget.VerificationCodeView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static android.content.Intent.FLAG_RECEIVER_FOREGROUND;
import static com.panshi.hujin2.iloan.util.ViewTools.ScreenTools.sp2px;
import static com.panshi.hujin2.iloan.util.ViewTools.TextColorNumberTools.setTextColorfulNumber;

public class MainActivity extends MVPBaseActivity implements MainView {

    private LinearLayout mllIndexTab, mllMytab, mllActive;
    private IndexFragment indexFragment;
    private MyFragment myFragment;
    private ActiveFrament activeFrament;
    private FragmentManager fragmentManager;
    private ImageView mingIndex;
    private ImageView mimgMy;
    private ImageView mimgDiscovery;
    private TextView mtvDiscoery;
    private TextView mtvIndex;
    private TextView mtvMy;
    private MainPresenter mainPresenter;
    private long exitTime;
    public int noReadNewsAccoun = 0;
    CusumeBadgeView badgeView;
    public String mActiveUrl;
    private TextView textViewDiscountCoupon;
    private TextView textViewDiscountCouponTime;
    private TextView textViewDiscountCouponPercent;
    private TextView tv_go;
    private FrameLayout framlayoutBack;
    private Dialog incitationDilog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        if (savedInstanceState != null) {
            indexFragment = (IndexFragment) getSupportFragmentManager().findFragmentByTag(IndexFragment.class.getName());
            myFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag(MyFragment.class.getName());
            activeFrament = (ActiveFrament) getSupportFragmentManager().findFragmentByTag(ActiveFrament.class.getName());
        }
        SystemClock.elapsedRealtime();
        mainPresenter.getNoReadzNews();
        mainPresenter.getActiveInfo();
        String type = getIntent().getStringExtra("type");
        if ("regit".equals(type)) {  //注册入口进入
            showInvitionWindow();
        }
        AutoUpdateUtil.getAutoInfo(this, "iLoan_mx.apk", "iLoan_mx.apk", "", PackageManagerUtil.getVersionCode(this.getApplicationContext()), false);
        mainPresenter.getDiscountCouponStatus();
        mainPresenter.sendDevice();
    }

    /**
     * 显示注册窗口
     */
    private void showInvitionWindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_invation, null);
        TextView mtvPass = contentView.findViewById(R.id.tv_next);
        Button btnFinshi = contentView.findViewById(R.id.finshi);
        ImageView mingCancel = contentView.findViewById(R.id.img_cancel);
        VerificationCodeView verificationCodeView = contentView.findViewById(R.id.verify_code);
        final String[] invateCode = new String[1];
        contentView.measure(-1, -1);
        int height = contentView.getMeasuredHeight();
        int with = contentView.getMeasuredWidth();
        DebugLog.i("wang", "===with==" + with + "==height==" + height);
        incitationDilog = new BaseDialog.Build(this).setContentView(contentView).setSzie(with, height).setCanceledOnTouchOutside(false).setCancelable(false).show();
        mtvPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incitationDilog != null && incitationDilog.isShowing()) {
                    incitationDilog.dismiss();
                }
            }
        });
        verificationCodeView.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onComplete(String content) {
                invateCode[0] = content;
            }
        });
        mingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incitationDilog != null && incitationDilog.isShowing()) {
                    incitationDilog.dismiss();
                }
            }
        });
        btnFinshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(invateCode[0])) {
                    DebugLog.i("wang", "=====invateCode======" + invateCode[0]);
                    mainPresenter.postInviteCode(invateCode[0]);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return mainPresenter = new MainPresenter();
    }

    public void refreshNews() {
        if (mainPresenter != null) {
            DebugLog.i("wang", "==刷新消息。。==");
            mainPresenter.getNoReadzNews();
        }
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    private void initView() {
        mingIndex = (ImageView) findViewById(R.id.img_index);
        mllIndexTab = (LinearLayout) findViewById(R.id.ll_index);
        mllMytab = (LinearLayout) findViewById(R.id.ll_my);
        mimgMy = (ImageView) findViewById(R.id.img_my);
        mtvIndex = (TextView) findViewById(R.id.tv_index);
        mtvMy = (TextView) findViewById(R.id.tv_my);
        mimgDiscovery = (ImageView) findViewById(R.id.img_active);
        mtvDiscoery = (TextView) findViewById(R.id.tv_active);
        mllActive = (LinearLayout) findViewById(R.id.ll_active);

        textViewDiscountCouponTime = findViewById(R.id.tv_time);
        textViewDiscountCouponPercent = findViewById(R.id.tv_percent);
        textViewDiscountCoupon = findViewById(R.id.tv_discount_coupon);
        tv_go = findViewById(R.id.tv_go);
        framlayoutBack = findViewById(R.id.fl_back);
        setTextMarquee(textViewDiscountCouponPercent);
        textViewDiscountCoupon.setText(getResources().getText(R.string.main_discount_coupon_tittle));
    }

    public static void setTextMarquee(TextView textView) {
        if (textView != null) {
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine(true);
            textView.setSelected(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
        }
    }

    private class OnTvGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            textViewDiscountCoupon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            final String newText = autoSplitText(textViewDiscountCoupon);
            if (!TextUtils.isEmpty(newText)) {
                textViewDiscountCoupon.setText(newText);
            }
        }
    }

    private String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度
        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("r", "").split("n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("n");
        }
        //把结尾多余的n去掉
        if (!rawText.endsWith("n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }

    private void initListener() {
        if (indexFragment == null) {
            indexFragment = new IndexFragment();
        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_content, indexFragment, indexFragment.getClass().getName()).commit();
        mllIndexTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFragment();
                mtvIndex.setTextColor(getResources().getColor(R.color.blue_txt));
                mingIndex.setBackgroundResource(R.drawable.index_select);
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                    fragmentManager.beginTransaction().add(R.id.fl_content, indexFragment, indexFragment.getClass().getName()).commit();
                } else {
                    fragmentManager.beginTransaction().show(indexFragment).commit();
                }
            }
        });

        mllMytab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFragment();
                mtvMy.setTextColor(getResources().getColor(R.color.blue_txt));
                mimgMy.setBackgroundResource(R.drawable.my_select);
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    fragmentManager.beginTransaction().add(R.id.fl_content, myFragment, myFragment.getClass().getName()).commit();
                } else {
                    fragmentManager.beginTransaction().show(myFragment).commit();
                }
            }
        });
        mllActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActiveFragment();

            }
        });
        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                framlayoutBack.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showDiscountpon();
                    }
                }, 1000);
            }
        });
        framlayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void showActiveFragment() {
        hideFragment();
        mtvDiscoery.setTextColor(getResources().getColor(R.color.blue_txt));
        mimgDiscovery.setBackgroundResource(R.drawable.discovery_select);
        if (activeFrament == null) {
            activeFrament = new ActiveFrament();
            fragmentManager.beginTransaction().add(R.id.fl_content, activeFrament, activeFrament.getClass().getName()).commit();
        } else {
            fragmentManager.beginTransaction().show(activeFrament).commit();
        }
       /* Intent intent=new Intent(this,WebViewBannerActivity.class);
        intent.putExtra("webUrl","http://10.0.52.121:3000/escrowwap/firstProm");
        startActivity(intent);*/
    }

    private void hideFragment() {
        mingIndex.setBackgroundResource(R.drawable.index_unselect);
        mimgMy.setBackgroundResource(R.drawable.my_unselect);
        mtvIndex.setTextColor(getResources().getColor(R.color.gray_acacac));
        mtvMy.setTextColor(getResources().getColor(R.color.gray_acacac));
        mtvDiscoery.setTextColor(getResources().getColor(R.color.gray_acacac));
        mimgDiscovery.setBackgroundResource(R.drawable.discovery);
        Observable.fromIterable(fragmentManager.getFragments()).subscribe(new Consumer<Fragment>() {
            @Override
            public void accept(Fragment fragment) throws Exception {
                fragmentManager.beginTransaction().hide(fragment).commit();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // return super.onKeyDown(keyCode, event);
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                // Object mHelperUtils;
                Toast.makeText(this, getString(R.string.quit_back_note), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();

            } else {// 不弹窗口.直接退出
                AppStaus.notificationArrived = 0;
                // AppCounter.Mark(6);//记录退出登录的在线时长操作
                // 结束activity队列中的所有activity
                Iterator<Activity> it = AppStaus.activitielists.iterator();
                while (it.hasNext()) {
                    Activity ac = it.next();
                    it.remove();
                    ac.finish();
                }
                // 清除通知栏
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
                System.exit(0);//结束整个进程
            }
            return true;
        }
        return true;
    }

    @Override
    public void callNoReadNews(int count) {
        DebugLog.i("wang", "==new==" + count);
        noReadNewsAccoun = count;
        if (badgeView != null) {
            badgeView.setBadgeCount(count);
            badgeView.requestLayout();
            if (myFragment != null) {
                myFragment.setTargeView(count);
            }
        } else {
            if (count != 0) {
                badgeView = new CusumeBadgeView(this);
                // badgeView.setText("V");
                badgeView.setTextColor(Color.WHITE);
                //   badgeView.setBackgroundColor(Color.RED);
                //    badgeView.setBadgeMargin(10,0,0,10);//设置边界
                //   badgeView.setBackground(10,Color.RED);
                badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
                badgeView.setBadgeCount(count);
                if (indexFragment != null) {
                    badgeView.setTargetView(indexFragment.mingNews);
                }
                if (myFragment != null) {
                    myFragment.setTargeView(count);
                }
                badgeView.requestLayout();
            }
        }
    }

    @Override
    public void resultActive(final ActiveRes.DataBean dataBean) {
        if (dataBean != null) {
            mllActive.setVisibility(View.VISIBLE);
            if (!dataBean.getForwardUrl().startsWith("http")) {
                mActiveUrl = "http://" + dataBean.getForwardUrl();
            } else {
                mActiveUrl = dataBean.getForwardUrl();
            }
            if (dataBean.getIsShow() == 0) {
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.active_dialog, null);
                view.setBackgroundColor(getResources().getColor(R.color.translate));
                ImageView imgCancel = view.findViewById(R.id.img_cancel);
                final SimpleDraweeView imgBg = view.findViewById(R.id.img_bg);
                //ImageLoaderFactory.createImageLoader().loadUrlImage(imgBg, dataBean.getImageUrl());
                final Dialog dialog = new BaseDialog.Build(this).setCanceledOnTouchOutside(true).setCancelable(true).setSzie(DensityUtil.getDisplayWidth(this), DensityUtil.getDisplayHeight(this)).setContentView(view, getResources().getColor(R.color.translate)).createDialog();
                imgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();

                        }
                    }
                });
                imgBg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showActiveFragment();
                    }
                });
                ImageLoaderFactory.createImageLoader().loadUrlImage(dataBean.getImageUrl(), new IImageResult() {
                    @Override
                    public void onResult(Object o) {
                        if (o != null && o instanceof Bitmap) {
                            Bitmap bitmap = (Bitmap) o;
                            DebugLog.i("wang", "===bitmap==" + bitmap);
                            RelativeLayout.LayoutParams layoutParams = null;
                            if (imgBg.getLayoutParams() != null) {
                                layoutParams = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
                            } else {
                                layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            }
                            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                            int with = bitmap.getWidth();
                            int heigt = bitmap.getHeight();
                            DebugLog.i("wang", "===with===" + DensityUtil.getDisplayWidth(MainActivity.this) + "||=====height==" + DensityUtil.getDisplayHeight(MainActivity.this) + "==bw==" + with + "==bh=" + heigt);
                            if (DensityUtil.getDisplayWidth(MainActivity.this) < with) {
                                lp.width = DensityUtil.getDisplayWidth(MainActivity.this) - DensityUtil.dip2px(MainActivity.this, 20);
                                int a = lp.width / with;
                                lp.height = heigt * a;
                                with = lp.width;
                                heigt = lp.height;
                            } else {
                                lp.width = with;
                                lp.height = heigt;
                            }

                          /*  lp.width=bitmap.getWidth();
                            lp.height=bitmap.getHeight();*/
                            dialog.getWindow().setAttributes(lp);
                            //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            layoutParams.width = with;
                            layoutParams.height = heigt;
                            imgBg.setLayoutParams(layoutParams);
                            imgBg.setImageBitmap(bitmap);
                            if (!MainActivity.this.isFinishing()) {
                                dialog.show();
                            }
                        }
                    }
                });

            }
        } else {
            mllActive.setVisibility(View.GONE);
        }
    }

    private ArrayList<MainDiscountCouponBean> mainDiscountCouponBeans = new ArrayList<>();

    @Override
    public void setDiscountCoupon(MainDiscountCouponOuterBean mainDiscountCouponBean) {
        mainDiscountCouponBeans = mainDiscountCouponBean.getArrayList();
        showDiscountpon();
    }

    public OnTvGlobalLayoutListener onTvGlobalLayoutListener = new OnTvGlobalLayoutListener();

    public void showDiscountpon() {
        if (mainDiscountCouponBeans != null) {
            if (mainDiscountCouponBeans.size() > 0) {
                MainDiscountCouponBean mainDiscountCouponBean = mainDiscountCouponBeans.remove(0);
                if (mainDiscountCouponBean.isJump()) {
                    DebugLog.d("mainDiscountCouponBean", mainDiscountCouponBean.toString());
                    framlayoutBack.setVisibility(View.VISIBLE);
//                textViewDiscountCoupon.getViewTreeObserver().addOnGlobalLayoutListener(onTvGlobalLayoutListener);

                    String prefix = "";
                    String counponVakue = removeZero(mainDiscountCouponBean.getCouponValue());

                    switch (mainDiscountCouponBean.getCouponType()) {
                        case 1://减息卷
                            prefix = counponVakue + getResources().getString(R.string.main_discount_coupon_dollor);
                            break;
                        case 2://减利率
                            prefix = counponVakue + "%";
                            break;
                        case 3://免天数
                            prefix = counponVakue + getResources().getString(R.string.main_discount_coupon_day);
                            break;
                        default:
                            break;
                    }

                    String all = prefix + "   " + mainDiscountCouponBean.getCouponContent();
                    SpannableString spannableString = setTextColorfulNumber(all, counponVakue, 30, R.color.main_discount_coupon_button);
                    textViewDiscountCouponPercent.setText(spannableString);
                    if (TextUtils.isEmpty(mainDiscountCouponBean.getExpDescription()) || mainDiscountCouponBean.getExpDescription().equals("null")) {
                        textViewDiscountCouponTime.setTextSize(11);
                        textViewDiscountCouponTime.setText(getResources().getString(R.string.main_discount_coupon_time) + "    " + mainDiscountCouponBean.getBeginDate() + "-" + mainDiscountCouponBean.getEndDate());
                    } else {
                        textViewDiscountCouponTime.setTextSize(13);
                        textViewDiscountCouponTime.setText(mainDiscountCouponBean.getExpDescription());
                    }
                }
            } else {
                try {
//                if (onTvGlobalLayoutListener != null) {
//                    textViewDiscountCoupon.getViewTreeObserver().removeOnGlobalLayoutListener(onTvGlobalLayoutListener);
//                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String removeZero(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        return decimalFormat.format(number);
    }

    public void dismissInvitationDialog() {
        if (incitationDilog != null && incitationDilog.isShowing()) {
            incitationDilog.dismiss();
        }
    }
}
