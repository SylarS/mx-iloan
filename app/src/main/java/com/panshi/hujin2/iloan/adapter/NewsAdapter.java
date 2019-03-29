package com.panshi.hujin2.iloan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.bean.NewsRes;
import com.panshi.hujin2.iloan.util.DateUtil;


import java.util.List;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class NewsAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NewsRes.DataBean.AppPushHistoryOutputBOListBean> list;

    public NewsAdapter(Context context, List<NewsRes.DataBean.AppPushHistoryOutputBOListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new NewsViewHolder(view,false);
    }

    @Override
    public RecyclerView.ViewHolder createHeaderHolder(View view) {
        return new NewsViewHolder(view,false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.news_item, null);
        return new NewsViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, boolean isItem) {
        if (holder instanceof NewsViewHolder) {
            NewsViewHolder newsAdapter = (NewsViewHolder) holder;
            newsAdapter.mtvTitle.setText(list.get(position).getTitle());
            newsAdapter.mtvText.setText(Html.fromHtml(list.get(position).getText()));

            newsAdapter.mtvDate.setText(DateUtil.TimeStamp2Date(String.valueOf(list.get(position).getCreateTime()),
                    context.getResources().getString(R.string.date_format), context.getResources().getConfiguration().locale));
        }
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView mtvTitle;
        public TextView mtvText;
        public TextView mtvDate;
        public ImageView mingIcon;

        public NewsViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                mtvTitle = itemView.findViewById(R.id.tv_title);
                mtvText = itemView.findViewById(R.id.tv_news);
                mtvDate = itemView.findViewById(R.id.tv_date);
                mingIcon = itemView.findViewById(R.id.img_icon);
            }

        }
    }
}
