package com.panshi.hujin2.iloan.listener;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public interface ProgressListener {
    /**
     * 下载开始
     */
    void onStart();

    /**
     * 下载进度
     * @param progress
     */
    void onProgess(long progress);

    /**
     * 下载完成
     */
    void onFinish();

    /**
     * 下载失败
     */
    void onFail();
}
