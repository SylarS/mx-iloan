package com.panshi.hujin2.iloan.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.bean.OrderDetailRes;
import com.panshi.hujin2.iloan.bean.OrderInfoRes;
import com.panshi.hujin2.iloan.bean.YearRateRes;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.IndexPresenter;
import com.panshi.hujin2.iloan.util.DateUtil;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.DensityUtil;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.NumberUtil;
import com.panshi.hujin2.iloan.util.StatusBarUtil;
import com.panshi.hujin2.iloan.util.StringUtil;
import com.panshi.hujin2.iloan.view.activity.NewsActivity;
import com.panshi.hujin2.iloan.view.activity.WebViewBannerActivity;
import com.panshi.hujin2.iloan.view.interfaces.IndexView;
import com.zhouyou.view.seekbar.SignSeekBar;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class IndexFragment extends MVPBaseFragment implements IndexView {
    private View view;
    private SignSeekBar signSeekBar;
    private SignSeekBar signSeekBarDay;
    private Button mbtnFastJie;
    private TextView mtvJieInfo;
    private int loanAmout;
    private int loanDay;
    private IndexPresenter indexPresenter;
    // private int phoneAcount = 1;
    RelativeLayout mrlRepayment, mrlDueDate, mrlWithDrawCash, mrlReviewing, mrlFastLoan, mrlRefsue, mrlPaymentFailture;
    private ImageView mimgvHelp;
    private TextView mavailAmout;
    private TextView mheadTitle;
    public int availAmout = 3000;
    public ImageView mingNews;
    private TextView mtvLoanDay;
    private SwipeRefreshLayout msrlRefrsh;
    private TextView mtvPayAmout;
    private TextView mtvPayAmoutDue;
    private Button btnRepayment;
    private Button btnDue;
    private TextView mtvYearRate; //年化利率
    private TextView mtvCate;//年度总费用
    private RelativeLayout mrlContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.index_fragment, null);
        initView();
        indexPresenter.queryOderInfo();
        initListener();
        initAnimation();
        return view;
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return indexPresenter = new IndexPresenter();
    }

    private void initView() {
        mtvJieInfo = view.findViewById(R.id.tv_jie_info);
        mimgvHelp = view.findViewById(R.id.imgv_help);
        mavailAmout = view.findViewById(R.id.tv_amout);
        mheadTitle = view.findViewById(R.id.head_tile);
        mingNews = view.findViewById(R.id.imgv_news);
        mtvLoanDay = view.findViewById(R.id.tv_loan_day);
        msrlRefrsh = view.findViewById(R.id.srl_refresh);
        mrlContent = view.findViewById(R.id.rl_content);
        RelativeLayout mrlHeadBg = view.findViewById(R.id.rl_head_bg);
        msrlRefrsh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        msrlRefrsh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        initLoanView();
        signSeekBar.getConfigBuilder()
                .min(1000)
                .max(3000)
                .progress(1000)
                .sectionCount(1000)
                .trackColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .secondTrackColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .thumbColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .sectionTextColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .sectionTextSize(16)
                .thumbTextColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .thumbTextSize(18)
                .signColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .signTextSize(18)
                .setReverseUnit("mxn")
                .reverse()
                .setUnit("$")
                .autoAdjustSectionMark()
                .build();
        signSeekBarDay.getConfigBuilder()
                .min(7)
                .max(14)
                .progress(7)
                .sectionCount(7)
                .trackColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .secondTrackColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .thumbColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .sectionTextColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .sectionTextSize(16)
                .thumbTextColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .thumbTextSize(18)
                .signColor(ContextCompat.getColor(getContext(), R.color.blue_txt))
                .signTextSize(18)
                .setUnit(getString(R.string.loan_days))
                .autoAdjustSectionMark()
                /* .sectionTextPosition(SignSeekBar.TextPosition.SIDES)*/
                .build();

    }

    /**
     * 借款
     */
    private void initLoanView() {
        mrlFastLoan = view.findViewById(R.id.rl_jiekuan);
        mrlFastLoan.setVisibility(View.VISIBLE);
        signSeekBar = view.findViewById(R.id.seek_bar);
        signSeekBarDay = view.findViewById(R.id.seek_bar_day);
        mbtnFastJie = view.findViewById(R.id.btn_jie);
        mtvYearRate = view.findViewById(R.id.tv_tv_lilv_year);
        mtvCate = view.findViewById(R.id.tv_cat);
        mtvLoanDay.setVisibility(View.GONE);
        mheadTitle.setText(getString(R.string.amout_avail));
        mavailAmout.setText(NumberUtil.forMat(getActivity(), availAmout));
        signSeekBar.setOnProgressChangedListener(new SignSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
                loanAmout = progress;
                DebugLog.i("wang", "loanAmout：" + loanAmout);
            }

            @Override
            public void getProgressOnActionUp(SignSeekBar signSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
        signSeekBarDay.setOnProgressChangedListener(new SignSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
                loanDay = progress;
                DebugLog.i("wang", "loanDay：" + loanDay);
            }

            @Override
            public void getProgressOnActionUp(SignSeekBar signSeekBar, int progress, float progressFloat) {
                DebugLog.i("wang", "==actionUp==" + progress);
                indexPresenter.queryYearRate(progress);
            }

            @Override
            public void getProgressOnFinally(SignSeekBar signSeekBar, int progress, float progressFloat, boolean fromUser) {
                DebugLog.i("wang", "==finally==" + progress);
                indexPresenter.queryYearRate(progress);
            }
        });
        mbtnFastJie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                intent.putExtra("urlPath", Constant.HTML_LOAN_PATH);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("money", String.valueOf(loanAmout));
                map.put("days", String.valueOf(loanDay));
                intent.putExtra(Constant.HTML_PARAM, map);
                DebugLog.i("wang", "==jsonStr=" + GonsonUtil.buildGosn().toJson(map));
                startActivity(intent);
            }
        });
    }

    /**
     * 审核中
     */
    private void reviewinView(OrderInfoRes.DataBean bean) {
        mrlReviewing = view.findViewById(R.id.rl_reviewing);
        mrlReviewing.setVisibility(View.VISIBLE);
        mtvLoanDay.setText(getString(R.string.days, bean.getLoanDays()));
        mtvLoanDay.setVisibility(View.VISIBLE);
        mheadTitle.setText(getString(R.string.amout_applay));
        TextView mtvLilv = view.findViewById(R.id.tv_shenhe_lilv_value);
        TextView mtvAmout = view.findViewById(R.id.tv_shenhe_cash_amout);
        mtvLilv.setText(NumberUtil.forMat(getActivity(), bean.getLendRate() * 100) + "%");
        mtvAmout.setText(NumberUtil.forMat(getActivity(), bean.getApplyMoney()));
        mavailAmout.setText(NumberUtil.forMat(getActivity(), bean.getApplyMoney()));
    }

    /**
     * 审核拒绝
     */
    private void reviewingRefuseView(final OrderInfoRes.DataBean bean) {
        mrlRefsue = view.findViewById(R.id.rl_review_refuse);
        mrlRefsue.setVisibility(View.VISIBLE);
        Button mbtnRefuse = view.findViewById(R.id.btn_refuse);
        TextView mtvRefuse = view.findViewById(R.id.tv_refuse);
        mtvLoanDay.setText(getString(R.string.days, bean.getLoanDays()));
        mtvLoanDay.setVisibility(View.VISIBLE);
        mheadTitle.setText(getString(R.string.amout_applay));
        mavailAmout.setText(NumberUtil.forMat(getActivity(), bean.getApplyMoney()));
        if (bean.getExpireRejectDays() > 0) {
            mtvRefuse.setText(getString(R.string.review_refuse_30_note));
            mbtnRefuse.setEnabled(false);
            mbtnRefuse.setClickable(false);
            mbtnRefuse.setBackgroundResource(R.drawable.regit_shape);
        } else {
            mtvRefuse.setText(getString(R.string.review_refuse_note));
            mbtnRefuse.setEnabled(true);
            mbtnRefuse.setClickable(true);
            mbtnRefuse.setBackgroundResource(R.drawable.buttun_index_shape);
        }
        mbtnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
               /* intent.putExtra("money",String.valueOf(bean.getApplyMoney()));
                intent.putExtra("days",String.valueOf(bean.getLoanDays()));*/
                intent.putExtra("urlPath", Constant.HTML_LOAN_PATH);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("money", String.valueOf(bean.getApplyMoney()));
                map.put("days", String.valueOf(bean.getLoanDays()));
                intent.putExtra(Constant.HTML_PARAM, map);
                startActivity(intent);

            }
        });
    }

    /**
     * 提现 打款中，打款失败
     */
    private void initWithDrawCashView(final OrderInfoRes.DataBean bean) {
        mrlWithDrawCash = view.findViewById(R.id.rl_withcash);
        mrlWithDrawCash.setVisibility(View.VISIBLE);
        //   mavailAmout.setText(NumberUtil.forMat(getActivity(), availAmout));
        TextView mtvAmout = view.findViewById(R.id.withdraw_cash_amout);
        TextView mtvnote = view.findViewById(R.id.tv_withdraw_avail);
        Button mbtnAmout = view.findViewById(R.id.btn_withdraw);
        TextView mtvLilv = view.findViewById(R.id.tv_lilv_value);
        mavailAmout.setText(NumberUtil.forMat(getActivity(), bean.getAuditMoney()));
        mtvLilv.setText(NumberUtil.forMat(getActivity(), bean.getLendRate() * 100) + "%");
        mtvAmout.setText(NumberUtil.forMat(getActivity(), bean.getAuditMoney()));
        mavailAmout.setText(NumberUtil.forMat(getActivity(), bean.getAuditMoney()));
        mtvLoanDay.setText(getString(R.string.days, bean.getLoanDays()));
        mtvLoanDay.setVisibility(View.VISIBLE);
        if (bean.getStatus() == Constant.OrderStatus.STATUS_3 || bean.getStatus() == Constant.OrderStatus.STATUS_13) {
            mbtnAmout.setBackgroundResource(R.drawable.buttun_index_blue_shape);
            mbtnAmout.setText(getResources().getString(R.string.repaymenting));
            mbtnAmout.setClickable(false);
            mheadTitle.setText(getString(R.string.payment_amout));
        } else {
            mbtnAmout.setBackgroundResource(R.drawable.buttun_index_shape);
            mbtnAmout.setText(getResources().getString(R.string.withdraw_cash));
            mheadTitle.setText(getString(R.string.amout_shouxin));
            mtvnote.setText(getString(R.string.amout_withdraw));
            mbtnAmout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                    intent.putExtra(Constant.HTML_PARAM_Type, Constant.HTML_PARAM_Str);
                    String jsonStr = "";
                    if (bean != null) {
                        jsonStr = GonsonUtil.buildGosn().toJson(bean);
                    }
                    intent.putExtra(Constant.HTML_PARAM_Data, jsonStr);
                    intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_WTTHDRAWCASH_PATH);
                    startActivity(intent);
                }
            });
        }

    }

    /**
     * 打款失败
     */
    private void initRepaymentFailture(final OrderInfoRes.DataBean bean) {
        mrlPaymentFailture = view.findViewById(R.id.rl_repayement_failture);
        mrlPaymentFailture.setVisibility(View.VISIBLE);
        //   mavailAmout.setText(NumberUtil.forMat(getActivity(), availAmout));
        TextView mtvAmout = view.findViewById(R.id.repayment_cash_amout);
        TextView mtvLilv = view.findViewById(R.id.tv_pay_lilv_value);
        mtvLilv.setText(NumberUtil.forMat(getActivity(), bean.getLendRate() * 100) + "%");
        mtvAmout.setText(NumberUtil.forMat(getActivity(), bean.getAuditMoney()));
        mavailAmout.setText(NumberUtil.forMat(getActivity(), bean.getAuditMoney()));
        mheadTitle.setText(getString(R.string.payment_amout));
        mtvLoanDay.setText(getString(R.string.days, bean.getLoanDays()));
        mtvLoanDay.setVisibility(View.VISIBLE);
        Button btnRepaymentFailture = view.findViewById(R.id.btn_payfailture);
        btnRepaymentFailture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
               /* intent.putExtra("money",String.valueOf(bean.getApplyMoney()));
                intent.putExtra("days",String.valueOf(bean.getLoanDays()));*/
                intent.putExtra("urlPath", Constant.HTML_LOAN_PATH);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("money", String.valueOf(bean.getApplyMoney()));
                map.put("days", String.valueOf(bean.getLoanDays()));
                intent.putExtra(Constant.HTML_PARAM, map);
                startActivity(intent);
            }
        });
    }

    /**
     * 待还款
     */
    private void initRepaymentView(final OrderInfoRes.DataBean bean) {
        // rl_re_payment
        mrlRepayment = view.findViewById(R.id.rl_re_payment);
        TextView mtvRate = view.findViewById(R.id.tv_jie_day_rate);
        mtvPayAmout = view.findViewById(R.id.tv_pay_amount);
        mrlRepayment.setVisibility(View.VISIBLE);
        btnRepayment = view.findViewById(R.id.btn_repayment);
        mtvLoanDay.setText(getString(R.string.days, bean.getLoanDays()));
        mtvLoanDay.setVisibility(View.VISIBLE);
        mavailAmout.setText(NumberUtil.forMat(getActivity(), bean.getAuditMoney()));
        mheadTitle.setText(getString(R.string.payment_amout));
        mtvRate.setText(getString(R.string.due_jie_date_rate, NumberUtil.forMat(getActivity(), bean.getLendRate() * 100)) + "%");
        // btnRepayment.setText(getResources().getString(R.string.repay_day_btn, bean.getLoanDays()));
        btnRepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_Type, Constant.HTML_PARAM_Str);
                String jsonStr = "";
                if (bean != null) {
                    jsonStr = GonsonUtil.buildGosn().toJson(bean);
                }
                intent.putExtra(Constant.HTML_PARAM_Data, jsonStr);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_ORDER_DETAIL);
                startActivity(intent);
            }
        });
    }

    /**
     * 逾期
     */
    private void initDueDateView(final OrderInfoRes.DataBean bean) {
        mrlDueDate = view.findViewById(R.id.rl_due_date);
        TextView mtvRate = view.findViewById(R.id.tv_day_rate);
        mtvPayAmoutDue = view.findViewById(R.id.tv_pay_amount_due);
        mrlDueDate.setVisibility(View.VISIBLE);
        mavailAmout.setText(NumberUtil.forMat(getActivity(), bean.getAuditMoney()));
        mtvLoanDay.setText(getString(R.string.days, bean.getLoanDays()));
        mtvLoanDay.setVisibility(View.VISIBLE);
        mheadTitle.setText(getString(R.string.payment_amout));
        // mtvRate.setText(getResources().getString(R.string.due_date_rate,NumberUtil.forMat(getActivity(),bean.getLendRate()*100)+"%"));
        mtvRate.setText(getString(R.string.due_date_rate, NumberUtil.forMat(getActivity(), bean.getLendRate() * 100) + "%,IVA de 16%"));
        btnDue = view.findViewById(R.id.btn_due);
        // btnDue.setText(getResources().getString(R.string.due_day_btn, bean.getLoanDays()));
        btnDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_Type, Constant.HTML_PARAM_Str);
                String jsonStr = "";
                if (bean != null) {
                    jsonStr = GonsonUtil.buildGosn().toJson(bean);
                }
                intent.putExtra(Constant.HTML_PARAM_Data, jsonStr);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_ORDER_DETAIL);
                startActivity(intent);
            }
        });
    }


    private void initAnimation() {
        //  final String[] phoneaArry = getResources().getStringArray(R.array.phone_arry);
        final Animation animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_in);
        mtvJieInfo.startAnimation(animationIn);//开始动画
        int phone = StringUtil.getRamdomValue(9000, 1000);
        int amount = (StringUtil.getRamdomValue(2000, 1000) / 100) * 100;
        mtvJieInfo.setText(getResources().getString(R.string.loan_phone_num, phone, amount));
        // mtvJieInfo.setText(phoneaArry[0]);
        animationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                mtvJieInfo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out);
                        mtvJieInfo.startAnimation(animation1);//开始动画
                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                               /* phoneAcount++;
                                if (phoneAcount < phoneaArry.length) {

                                } else {
                                    phoneAcount = 0;
                                }*/
                                int phone = StringUtil.getRamdomValue(9000, 1000);
                                int amount = (StringUtil.getRamdomValue(2000, 1000) / 100) * 100;
                                mtvJieInfo.setText(getResources().getString(R.string.loan_phone_num, phone, amount));
                                // mtvJieInfo.setText(phoneaArry[phoneAcount]);
                                mtvJieInfo.startAnimation(animationIn);//开始动画
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initListener() {
        mrlContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (mrlContent != null) {
                    mrlContent.getViewTreeObserver().removeOnPreDrawListener(this);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mrlContent.getLayoutParams();
                  /*  lp.width= DensityUtil.getDisplayWidth(getActivity());
                    lp.height= DensityUtil.getDisplayHeight(getActivity())-DensityUtil.dip2px(getActivity(),48)*2;*/
                    mrlFastLoan.getBottom();
                    int contentHeigt = DensityUtil.getDisplayHeight(getActivity()) - DensityUtil.dipToPixels(getActivity(), 48);
                    // DebugLog.i("wang","==h=="+mrlContent.getHeight());

                    DebugLog.i("wang", "==content===" + mrlContent.getBottom() + "||===" + mrlFastLoan.getBottom() + "==||==" + contentHeigt + "==||==" + StatusBarUtil.getStatusBarHeight(getActivity()));
                    int buttom = (int) (contentHeigt - (mrlContent.getBottom() + DensityUtil.dip2px(getActivity(), 48) + StatusBarUtil.getStatusBarHeight(getActivity())));
                    if (buttom >= 0) {
                        lp.height = mrlContent.getHeight() + buttom;
                        mrlContent.setLayoutParams(lp);
                    } else {
                        lp.height = mrlContent.getHeight() + Math.abs(buttom);
                        mrlContent.setLayoutParams(lp);
                    }

                }
                return true;
            }
        });
        msrlRefrsh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrlRefrsh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        indexPresenter.queryOderInfo();
                        msrlRefrsh.setRefreshing(false);
                    }
                }, 400);
            }
        });
        mimgvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_HELP_PATH);
                startActivity(intent);
            }
        });
        mingNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.REFRESH_ORDER == 1) {
            Constant.REFRESH_ORDER = 0;
            DebugLog.i("wang", "==刷新订单==");
            indexPresenter.queryOderInfo();
        }

    }

    /**
     * 查询订单信息
     *
     * @param dataBeans
     */
    @Override
    public void callOrderInfo(OrderInfoRes.DataBean dataBeans) {
        hidden();
        if (dataBeans == null) {
            initLoanView();
            return;
        }
        if (dataBeans.isHasDue()) { //逾期
            initDueDateView(dataBeans);
            indexPresenter.QueryOrderDetailInfo(dataBeans.getId());
            return;
        }
        switch (dataBeans.getStatus()) {
            case Constant.OrderStatus.STATUS_6:
            case Constant.OrderStatus.STATUS_10:  //已完成
                initLoanView();
                break;
            case Constant.OrderStatus.STATUS_1: //待审核
                reviewinView(dataBeans);
                break;
            case Constant.OrderStatus.STATUS_2: //审核过 ，提现
            case Constant.OrderStatus.STATUS_3:
            case Constant.OrderStatus.STATUS_8:
            case Constant.OrderStatus.STATUS_13:

                initWithDrawCashView(dataBeans);
                break;
            case Constant.OrderStatus.STATUS_12://审核拒绝
            case Constant.OrderStatus.STATUS_9:
                reviewingRefuseView(dataBeans);
                break;
         /*   case Constant.OrderStatus.STATUS_13: //打款失败
                initRepaymentFailture(dataBeans);
                break;*/

            case Constant.OrderStatus.STATUS_4: //待还款
            case Constant.OrderStatus.STATUS_7:
                initRepaymentView(dataBeans);
                indexPresenter.QueryOrderDetailInfo(dataBeans.getId());
                break;
            case Constant.OrderStatus.STATUS_5://逾期
                initDueDateView(dataBeans);
                indexPresenter.QueryOrderDetailInfo(dataBeans.getId());
                break;

        }

    }

    public void hidden() {
        if (mrlDueDate != null) {
            mrlDueDate.setVisibility(View.GONE);
        }
        if (mrlFastLoan != null) {
            mrlFastLoan.setVisibility(View.GONE);
        }
        if (mrlRepayment != null) {
            mrlRepayment.setVisibility(View.GONE);
        }
        if (mrlWithDrawCash != null) {
            mrlWithDrawCash.setVisibility(View.GONE);
        }
        if (mrlReviewing != null) {
            mrlReviewing.setVisibility(View.GONE);
        }
        if (mrlRefsue != null) {
            mrlRefsue.setVisibility(View.GONE);
        }
        if (mrlPaymentFailture != null) {
            mrlPaymentFailture.setVisibility(View.GONE);
        }
    }

    /**
     * 查询订单详情
     *
     * @param dataBean
     */

    @Override
    public void callOrderDetail(OrderDetailRes.DataBean dataBean) {
        if (mrlRepayment != null && mrlRepayment.getVisibility() == View.VISIBLE) {
            TextView mtvRepayAccount = mrlRepayment.findViewById(R.id.tv_repay_accout);
            TextView mtvRepayCode = mrlRepayment.findViewById(R.id.tv_repay_code);
            TextView mtvRepayDate = mrlRepayment.findViewById(R.id.tv_repay_date);
            TextView mtvRepayBankName = mrlRepayment.findViewById(R.id.tv_repay_bankName);
            TextView mtvRepayEletricAccount = mrlRepayment.findViewById(R.id.tv_repay_eletri_accout);

            if (!TextUtils.isEmpty(dataBean.getReceiveAccount())) {
                mtvRepayAccount.setText(dataBean.getReceiveAccount());
            }
            if (!TextUtils.isEmpty(dataBean.getReceiveBankName())) {
                mtvRepayBankName.setText(dataBean.getReceiveBankName());
            }
            if (!TextUtils.isEmpty(dataBean.getReceiveAccountName())) {
                mtvRepayCode.setText(dataBean.getReceiveAccountName());
            }
            if (!TextUtils.isEmpty(dataBean.getRepayTime())) {
                mtvRepayDate.setText(DateUtil.TimeStamp2Date(dataBean.getRepayTime(), getString(R.string.repayment_date), getActivity().getResources().getConfiguration().locale));
            }
            if (btnRepayment != null) {
                btnRepayment.setText(getResources().getString(R.string.repay_day_btn, dataBean.getDistanceRepayDay()));
            }
            if (mtvPayAmout != null) {
                mtvPayAmout.setText(NumberUtil.forMat(getActivity(), dataBean.getCurrentRepay()));
            }
            if (!TextUtils.isEmpty(dataBean.getReceiveClabe())) {
                mtvRepayEletricAccount.setText(dataBean.getReceiveClabe());
            }
        }

        if (mrlDueDate != null && mrlDueDate.getVisibility() == View.VISIBLE) {
            TextView mtvRepayAccount = mrlDueDate.findViewById(R.id.tv_return_accout);
            TextView mtvRepayCode = mrlDueDate.findViewById(R.id.tv_return_code);
            TextView mtvRepayDate = mrlDueDate.findViewById(R.id.tv_return_date);
            TextView mtvRetrunBankName = mrlDueDate.findViewById(R.id.tv_return_bankName);
            TextView mtvRepayEletricAccount = mrlDueDate.findViewById(R.id.tv_return_eletric_accout);
            if (!TextUtils.isEmpty(dataBean.getReceiveBankName())) {
                mtvRetrunBankName.setText(dataBean.getReceiveBankName());
            }
            if (mtvPayAmoutDue != null) {
                mtvPayAmoutDue.setText(NumberUtil.forMat(getActivity(), dataBean.getCurrentRepay()));
            }
            if (!TextUtils.isEmpty(dataBean.getReceiveAccount())) {
                mtvRepayAccount.setText(dataBean.getReceiveAccount());
            }
            if (!TextUtils.isEmpty(dataBean.getReceiveAccountName())) {
                mtvRepayCode.setText(dataBean.getReceiveAccountName());
            }
            if (!TextUtils.isEmpty(dataBean.getRepayTime())) {
                mtvRepayDate.setText(DateUtil.TimeStamp2Date(dataBean.getRepayTime(), getString(R.string.repayment_date), getActivity().getResources().getConfiguration().locale));
            }
            if (btnDue != null) {
                btnDue.setText(getResources().getString(R.string.due_day_btn, dataBean.getDistanceRepayDay()));
            }
            if (!TextUtils.isEmpty(dataBean.getReceiveClabe())) {
                mtvRepayEletricAccount.setText(dataBean.getReceiveClabe());
            }
        }
    }

    @Override
    public void callYearRate(YearRateRes.DataBean dataBean) {
        DebugLog.i("wang", "==callYearRate==" + dataBean.getCat() + "||" + dataBean.getYearRate());
        if (mtvYearRate != null) {
            mtvYearRate.setText(dataBean.getYearRate() * 100 + "%");
        }
        if (mtvCate != null) {
            mtvCate.setText(dataBean.getCat() * 100 + "%");
        }
    }

}
