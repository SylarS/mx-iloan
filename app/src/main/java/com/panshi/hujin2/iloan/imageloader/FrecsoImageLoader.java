package com.panshi.hujin2.iloan.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.panshi.hujin2.iloan.util.DensityUtil;
import com.panshi.hujin2.iloan.util.StreamTool;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * frecso Imageloader
 * Created by Administrator on 2018/6/13 0013.
 */

public class FrecsoImageLoader implements IImageLoader {
    private Context context;
    /**
     * 默认压缩图片 降低内存 单位为DP
     */
    private static final int DEFAULT_WIDTH = 120, DEFAULT_HEIGHT = 120;

    @Override
    public void initialize(Context context) {
        this.context = context;
        Fresco.initialize(context, ImageLoaderConfig.getInstance(context).getImagePipelineConfig());
    }

    @Override
    public void loadUrlImage(ImageView view, String url) {
        if (checkload(view, url)) return;
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view;
        Uri uri = Uri.parse(url);
        loadImage(simpleDraweeView, uri, DensityUtil.dip2px(simpleDraweeView.getContext(), DEFAULT_WIDTH), DensityUtil.dip2px(simpleDraweeView.getContext(), DEFAULT_HEIGHT), null, null, false);
    }


    private boolean checkload(ImageView view, String url) {
        if (TextUtils.isEmpty(url) || view == null) {
            return true;
        }
        if (!(view instanceof SimpleDraweeView)) {
            try {
                throw new Exception(" imageView of frecso must be simpleDraweeView");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void loadUrlImage(String url, IImageResult result) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        loadOriginalImage(context, url, result, UiThreadImmediateExecutorService.getInstance());
    }

    @Override
    public void loadFileImage(ImageView view, String filePath) {
        checkload(view, filePath);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view;
        loadImage(simpleDraweeView, uri, DensityUtil.dip2px(simpleDraweeView.getContext(), DEFAULT_WIDTH), DensityUtil.dip2px(simpleDraweeView.getContext(), DEFAULT_HEIGHT), null, null, false);
    }

    @Override
    public void loadDrawbleImage(ImageView view, int resId) {

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(resId))
                .build();
        if (!(view instanceof SimpleDraweeView)) {
            try {
                throw new Exception(" imageView of frecso must be simpleDraweeView");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view;
        loadImage(simpleDraweeView, uri, DensityUtil.dip2px(simpleDraweeView.getContext(), DEFAULT_WIDTH), DensityUtil.dip2px(simpleDraweeView.getContext(), DEFAULT_HEIGHT), null, null, false);

    }

    @Override
    public void loadDownImage(String url, IDownloadResult result) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        downloadImage(context, url, result);
    }

    @Override
    public void loadAssetImage(ImageView view, String fileName) {

        if (checkload(view, fileName)) return;
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view;
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_ASSET_SCHEME)
                .path(fileName)
                .build();
        loadImage(simpleDraweeView, uri, DensityUtil.dip2px(simpleDraweeView.getContext(), DEFAULT_WIDTH), DensityUtil.dip2px(simpleDraweeView.getContext(), DEFAULT_HEIGHT), null, null, false);
    }

    public void loadImage(SimpleDraweeView simpleDraweeView,
                          Uri uri,
                          final int reqWidth,
                          final int reqHeight,
                          BasePostprocessor postprocessor,
                          ControllerListener<ImageInfo> controllerListener,
                          boolean isSmall) {

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        imageRequestBuilder.setRotationOptions(RotationOptions.autoRotate());

        // 不支持图片渐进式加载，理由：https://github.com/facebook/fresco/issues/1204
        imageRequestBuilder.setProgressiveRenderingEnabled(false);

        if (isSmall) {
            imageRequestBuilder.setCacheChoice(ImageRequest.CacheChoice.SMALL);
        }

        if (reqWidth > 0 && reqHeight > 0) {
            imageRequestBuilder.setResizeOptions(new ResizeOptions(reqWidth, reqHeight));
        }

        if (UriUtil.isLocalFileUri(uri)) {
            imageRequestBuilder.setLocalThumbnailPreviewsEnabled(true);
        }

        if (postprocessor != null) {
            imageRequestBuilder.setPostprocessor(postprocessor);
        }

        ImageRequest imageRequest = imageRequestBuilder.build();

        PipelineDraweeControllerBuilder draweeControllerBuilder = Fresco.newDraweeControllerBuilder();
        draweeControllerBuilder.setOldController(simpleDraweeView.getController());
        draweeControllerBuilder.setImageRequest(imageRequest);

        if (controllerListener != null) {
            draweeControllerBuilder.setControllerListener(controllerListener);
        }

        draweeControllerBuilder.setTapToRetryEnabled(true); // 开启重试功能
        draweeControllerBuilder.setAutoPlayAnimations(true); // 自动播放gif动画
        DraweeController draweeController = draweeControllerBuilder.build();
        simpleDraweeView.setController(draweeController);
    }

    /**
     * 根据提供的图片URL加载原始图（该方法仅针对大小在100k以内的图片，若不确定图片大小，
     * 请使用下面的downloadImage(String url, final DownloadImageResult loadFileResult) ）
     *
     * @param url             图片URL
     * @param loadImageResult LoadImageResult
     * @param executor        的取值有以下三个：
     *                        UiThreadImmediateExecutorService.getInstance() 在回调中进行任何UI操作
     *                        CallerThreadExecutor.getInstance() 在回调里面做的事情比较少，并且不涉及UI
     *                        Executors.newSingleThreadExecutor() 你需要做一些比较复杂、耗时的操作，并且不涉及UI（如数据库读写、文件IO），你就不能用上面两个Executor。
     *                        你需要开启一个后台Executor，可以参考DefaultExecutorSupplier.forBackgroundTasks。
     */
    public void loadOriginalImage(Context context, String url, final IImageResult<Bitmap> loadImageResult, Executor executor) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        context = context.getApplicationContext();
        Uri uri = Uri.parse(url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest imageRequest = builder.build();
        // 获取已解码的图片，返回的是Bitmap
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableBitmap>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<CloseableBitmap>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }

                CloseableReference<CloseableBitmap> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableBitmap> closeableReference = imageReference.clone();
                    try {
                        CloseableBitmap closeableBitmap = closeableReference.get();
                        Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                        if (bitmap != null && !bitmap.isRecycled()) {
                            // https://github.com/facebook/fresco/issues/648
                            final Bitmap tempBitmap = bitmap.copy(bitmap.getConfig(), false);
                            loadImageResult.onResult(tempBitmap);
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
                }
                loadImageResult.onResult(null);
            }
        };
        dataSource.subscribe(dataSubscriber, executor);
    }

    /**
     * 从网络下载图片
     * 1、根据提供的图片URL，获取图片数据流
     * 2、将得到的数据流写入指定路径的本地文件
     *
     * @param url            URL
     * @param loadFileResult LoadFileResult
     */
    public void downloadImage(Context context, String url, final IDownloadResult loadFileResult) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest imageRequest = builder.build();

        // 获取未解码的图片数据
        DataSource<CloseableReference<PooledByteBuffer>> dataSource = imagePipeline.fetchEncodedImage(imageRequest, context);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                if (!dataSource.isFinished() || loadFileResult == null) {
                    return;
                }

                CloseableReference<PooledByteBuffer> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<PooledByteBuffer> closeableReference = imageReference.clone();
                    try {
                        PooledByteBuffer pooledByteBuffer = closeableReference.get();
                        InputStream inputStream = new PooledByteBufferInputStream(pooledByteBuffer);
                        String photoPath = loadFileResult.getFilePath();
                        byte[] data = StreamTool.read(inputStream);
                        StreamTool.write(photoPath, data);
                        loadFileResult.onResult(photoPath);
                    } catch (IOException e) {
                        loadFileResult.onResult(null);
                        e.printStackTrace();
                    } finally {
                        imageReference.close();
                        closeableReference.close();

                    }
                }
            }

            @Override
            public void onProgressUpdate(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                int progress = (int) (dataSource.getProgress() * 100);
                if (loadFileResult != null) {
                    loadFileResult.onProgress(progress);
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (loadFileResult != null) {
                    loadFileResult.onResult(null);
                }

                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
                }
            }
        }, Executors.newSingleThreadExecutor());
    }
}
