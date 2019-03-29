package com.panshi.hujin2.iloan.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.util.DensityUtil;
import com.panshi.hujin2.iloan.util.StatusBarUtil;
import com.panshi.hujin2.iloan.util.StringUtil;
import com.panshi.hujin2.iloan.view.activity.MainActivity;
import com.panshi.hujin2.iloan.view.activity.NewsActivity;
import com.panshi.hujin2.iloan.view.activity.SetActivity;
import com.panshi.hujin2.iloan.view.activity.WebViewBannerActivity;
import com.panshi.hujin2.iloan.view.widget.CusumeBadgeView;

/**
 * Created by Administrator on 2018/7/30 0030.
 */

public class MyFragment extends Fragment {
    private View view;
    private LinearLayout mllLoan;
    private LinearLayout mllMyReward;
    private LinearLayout mllHelp;
    private LinearLayout mllSuggestReplay;
    private LinearLayout mllSetting;
    private LinearLayout mllBankaccount;
    private TextView mtvPhoneNum;
    private ImageView mimgNews;
    private CusumeBadgeView badgeView;
    private RelativeLayout mrlContent;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_fragment_layout, null);
        initView();
        initListener();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            int newsAccount = ((MainActivity) getActivity()).noReadNewsAccoun;
            setTargeView(newsAccount);

        }
    }

    public void setTargeView(int newsAccount) {

        if (badgeView != null) {
            badgeView.setBadgeCount(newsAccount);
            badgeView.requestLayout();
        } else {
            if (newsAccount != 0) {
                badgeView = new CusumeBadgeView(getActivity());
                // badgeView.setText("V");
                badgeView.setTextColor(Color.WHITE);
                //   badgeView.setBackgroundColor(Color.RED);
                //    badgeView.setBadgeMargin(10,0,0,10);//设置边界
                //   badgeView.setBackground(10,Color.RED);
                badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
                badgeView.setBadgeCount(newsAccount);
                badgeView.setTargetView(mimgNews);
            }
        }
    }

    private void initView() {
        mllLoan = view.findViewById(R.id.ll_loan);
        mllMyReward = view.findViewById(R.id.ll_reward);
        mllHelp = view.findViewById(R.id.ll_help_center);
        mllSuggestReplay = view.findViewById(R.id.ll_suggest_replay);
        mllSetting = view.findViewById(R.id.ll_setting);
        mtvPhoneNum = view.findViewById(R.id.tv_phone);
        mimgNews = view.findViewById(R.id.img_news);
        mllBankaccount = view.findViewById(R.id.ll_bankaccount);
        mrlContent = view.findViewById(R.id.rl_content);
        ImageView imgHistory = view.findViewById(R.id.img_history);
        ImageView imgMyReward = view.findViewById(R.id.img_my_reward);
        ImageView imgHelp = view.findViewById(R.id.img_help_center);
        ImageView imgReply = view.findViewById(R.id.img_sugget_reply);
        ImageView imgSet = view.findViewById(R.id.img_set);

        if (AppStaus.userInfo != null) {
            mtvPhoneNum.setText(StringUtil.dealPhone(AppStaus.userInfo.getPhoneNumber()));
        }
        RelativeLayout mrlHeadBg = view.findViewById(R.id.rl_head_bg);
    }


    private void initListener() {
        mimgNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent);
            }
        });
        mllLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_LOAN_HISTORY_PATH);
                startActivity(intent);
            }
        });
        mllHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_HELP_PATH);
                startActivity(intent);
            }
        });
        mllSuggestReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_SUGGEST_REPLAY_PATH);
                startActivity(intent);
            }
        });
        mllSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetActivity.class);
                startActivity(intent);
            }
        });
        mllMyReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_MY_REWARD);
                startActivity(intent);
            }
        });
        mllBankaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                intent.putExtra(Constant.HTML_PARAM_PATH, Constant.HTML_BACK);
                startActivity(intent);
            }
        });
        mrlContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mrlContent.getViewTreeObserver().removeOnPreDrawListener(this);
                int height=  mrlContent.getBottom()+DensityUtil.dip2px(getActivity(), 48)+DensityUtil.dip2px(getActivity(), 48) + StatusBarUtil.getStatusBarHeight(getActivity());
                int diffDistace=height-DensityUtil.getDisplayHeight(getActivity());
                int bottom=mrlContent.getBottom()+Math.abs(diffDistace);
                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) mrlContent.getLayoutParams();
                if(layoutParams!=null){
                    layoutParams.height= mrlContent.getMeasuredHeight()+Math.abs(diffDistace);
                    mrlContent.setLayoutParams(layoutParams);
                }
                return true;
            }
        });
    }


}
