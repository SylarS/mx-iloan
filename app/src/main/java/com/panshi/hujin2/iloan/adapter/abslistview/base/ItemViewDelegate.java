package com.panshi.hujin2.iloan.adapter.abslistview.base;


import com.panshi.hujin2.iloan.adapter.abslistview.ViewHolder;

public interface ItemViewDelegate<T>
{

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHolder holder, T t, int position);



}
