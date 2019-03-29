package com.panshi.hujin2.iloan.factory;

import com.panshi.hujin2.iloan.imageloader.FrecsoImageLoader;
import com.panshi.hujin2.iloan.imageloader.IImageLoader;

/**
 * Created by Administrator on 2018/6/13 0013.
 */

public class ImageLoaderFactory {
    static FrecsoImageLoader frecsoImageLoader = new FrecsoImageLoader();

    /**
     * 创建 frcso Imageloader
     *
     * @return
     */
    public static IImageLoader createImageLoader() {
        return frecsoImageLoader;
    }


}
