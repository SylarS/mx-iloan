package com.panshi.hujin2.iloan.imageloader;

import android.content.Context;

import com.panshi.hujin2.iloan.util.FileUtils;

import java.io.File;

/**
 * 下载图片的结果监听器
 */
public abstract class IDownloadResult implements IImageResult<String> {

    private String mFilePath;

    public IDownloadResult(Context context, String url) {
        this.mFilePath = FileUtils.getImageDownloadPath(context, url);
    }

    public IDownloadResult(Context context) {
        this.mFilePath = FileUtils.getImageDownloadPath(context);
    }

    public String getFilePath() {
        return mFilePath;
    }

    public File getFile() {
        return new File(mFilePath);
    }

    public void onProgress(int progress) {

    }

    @Override
    public abstract void onResult(String filePath);

}
