package com.panshi.hujin2.iloan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.adapter.NewsAdapter;
import com.panshi.hujin2.iloan.adapter.recyclerview.AddItemDecoration;
import com.panshi.hujin2.iloan.bean.NewsRes;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.NewsPresenter;
import com.panshi.hujin2.iloan.service.UpdateNoReadNewsService;
import com.panshi.hujin2.iloan.util.DensityUtil;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.NewsInterface;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class NewsActivity extends MVPBaseActivity implements NewsInterface {
    private NewsPresenter newsPresenter;
    private XRefreshView mxrvList;
    private RecyclerView mrcvList;
    private NewsAdapter adapter;
    private List<NewsRes.DataBean.AppPushHistoryOutputBOListBean> listData = new ArrayList<>();
    private Toolbar mToorbar;
    private int mNextId = -1;
    private TextView mtvNoReadNews;
    private int goBackToMainActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);
        initView();
        newsPresenter.getListNews(null, Constant.PAGESIZE);
        initListener();
        goBackToMainActivity = getIntent().getIntExtra("goBackToMainActivity", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getString(R.string.news));
        if (goBackToMainActivity != 0) {
            Toolbar toolbar = getToolbar();
            if (toolbar != null) {
                toolbar.setNavigationIcon(R.drawable.back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
    }

    private void initListener() {
        mxrvList.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onRefresh(boolean isPullDown) {

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                newsPresenter.getListNews(mNextId, Constant.PAGESIZE);
            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }

    private void initView() {
        mrcvList = (RecyclerView) findViewById(R.id.rcy_list);
        mxrvList = (XRefreshView) findViewById(R.id.xrv_list);
        mToorbar = (Toolbar) findViewById(R.id.toolbar);
        mToorbar.setNavigationIcon(R.drawable.back);
        mToorbar.setTitle(getString(R.string.news));
        mxrvList.setPullRefreshEnable(false);
        mxrvList.setAutoRefresh(false);
        mxrvList.setPullLoadEnable(true);
        mtvNoReadNews = (TextView) findViewById(R.id.tv_news);
        mrcvList.setLayoutManager(new LinearLayoutManager(this));
        mrcvList.addItemDecoration(new AddItemDecoration(DensityUtil.dip2px(this, 20), 0));
        adapter = new NewsAdapter(this, listData);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        mrcvList.setAdapter(adapter);
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        return newsPresenter = new NewsPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return mToorbar;
    }

    @Override
    public void onFailture(int code, String message) {
        ToastUtil.showToast(message);
    }

    @Override
    public void callNewsList(List<NewsRes.DataBean.AppPushHistoryOutputBOListBean> list) {
        // mNextId=list
        if (list != null) {
            if (list.size() == 0) {
                if (mNextId == -1 && listData.size() == 0) {
                    mxrvList.setVisibility(View.GONE);
                    mtvNoReadNews.setVisibility(View.VISIBLE);
                }
                mxrvList.setLoadComplete(true);
            } else {
                mxrvList.setVisibility(View.VISIBLE);
                mtvNoReadNews.setVisibility(View.GONE);
                listData.addAll(list);
                adapter.notifyDataSetChanged();
                mxrvList.setLoadComplete(false);
            }

        } else {
            if (mNextId == -1 && listData.size() == 0) {
                mxrvList.setVisibility(View.GONE);
                mtvNoReadNews.setVisibility(View.VISIBLE);
            }
            mxrvList.setLoadComplete(true);
        }
    }


    @Override
    public void callNextId(int nextId) {
        mNextId = nextId;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constant.IF_REFRESH_NOREAD_NEWS = 1;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!(mNextId == -1 && listData.size() == 0)) {
            Intent intent = new Intent(NewsActivity.this, UpdateNoReadNewsService.class);
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (goBackToMainActivity != 0) {
            Intent intent = new Intent(NewsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
