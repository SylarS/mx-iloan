package com.panshi.hujin2.iloan.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.megvii.livenesslib.util.ConUtil;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.FaceVitifyResultPresenter;
import com.panshi.hujin2.iloan.view.interfaces.FaceVitifyView;
import com.panshi.hujin2.iloan.view.widget.RotaterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class FaceVetifyResultActivity extends MVPBaseActivity implements View.OnClickListener, FaceVitifyView {
    private TextView textView;
    private ImageView mImageView;
    private LinearLayout ll_result_image;
    private ImageView bestImage, envImage;
    private String bestPath;
    private String envPath;
    private FaceVitifyResultPresenter faceVitifyResultPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        init();
        Bundle bundle = getIntent().getExtras();
        faceVitifyResultPresenter.dealResult(bundle);
        // initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String resultOBJ = bundle.getString("result");
        try {
            JSONObject result = new JSONObject(resultOBJ);
            textView.setText(result.getString("result"));
            int resID = result.getInt("resultcode");

            checkID(resID);
            boolean isSuccess = result.getString("result").equals(
                    getResources().getString(R.string.verify_success));
            mImageView.setImageResource(isSuccess ? R.drawable.result_success
                    : R.drawable.result_failded);
            if (isSuccess) {
                String delta = bundle.getString("delta");
                Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
                byte[] image_best = images.get("image_best");
                byte[] image_env = images.get("image_env");
                byte[] image_action1 = images.get("action1");
                byte[] image_action2 = images.get("action2");
                byte[] image_action3 = images.get("action3");
                //N张动作图根据需求自取，对应字段action1、action2 ...
                // byte[] image_action1 = images.get("image_action1);
                ll_result_image.setVisibility(View.VISIBLE);
                bestImage.setImageBitmap(BitmapFactory.decodeByteArray(image_best, 0, image_best.length));
                envImage.setImageBitmap(BitmapFactory.decodeByteArray(image_env, 0, image_env.length));
                String bestImageBestFileName = "image_best";
                String imageEnvFileName = "image_env";
                String imageAction1FileName = "image_action1";
                String imageAction2FileName = "image_action2";
                String imageAction3FileName = "image_action3";
                ConUtil.saveJPGFile(this, image_best, bestImageBestFileName);
                ConUtil.saveJPGFile(this, image_env, imageEnvFileName);
                ConUtil.saveJPGFile(this, image_action1, imageAction1FileName);
                ConUtil.saveJPGFile(this, image_action2, imageAction2FileName);
                ConUtil.saveJPGFile(this, image_action3, imageAction3FileName);
                //保存图片
                //bestPath = ConUtil.saveJPGFile(this, image_best, "image_best");
                //envPath = ConUtil.saveJPGFile(this, image_env, "image_env");
                //调用活体比对API
                //imageVerify(images,delta);
            } else {
                ll_result_image.setVisibility(View.GONE);
            }
            doRotate(isSuccess);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkID(int resID) {
        if (resID == R.string.verify_success) {
            doPlay(R.raw.meglive_success);
        } else if (resID == R.string.liveness_detection_failed_not_video) {
            doPlay(R.raw.meglive_failed);
        } else if (resID == R.string.liveness_detection_failed_timeout) {
            doPlay(R.raw.meglive_failed);
        } else if (resID == R.string.liveness_detection_failed) {
            doPlay(R.raw.meglive_failed);
        } else {
            doPlay(R.raw.meglive_failed);
        }
    }

    private void init() {
        mImageView = (ImageView) findViewById(R.id.result_status);
        textView = (TextView) findViewById(R.id.result_text_result);
        ll_result_image = (LinearLayout) findViewById(R.id.ll_result_image);
        bestImage = (ImageView) findViewById(R.id.iv_best);
        envImage = (ImageView) findViewById(R.id.iv_env);
        findViewById(R.id.result_next).setOnClickListener(this);
    }

    /*  */

    /**
     * 如何调用Verify2.0方法  详细API字段请参考Verify文档描述
     *//*
    public void imageVerify(Map<String, byte[]> images, String delta) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("api_key", "API_KEY");
        requestParams.put("api_secret", "API_SECRET");
        requestParams.put("comparison_type", "1");
        requestParams.put("face_image_type", "meglive");
        requestParams.put("idcard_name", "身份证姓名");
        requestParams.put("idcard_number", "身份证号码");
        requestParams.put("delta", delta);
        for (Map.Entry<String, byte[]> entry : images.entrySet()) {
            requestParams.put(entry.getKey(),
                    new ByteArrayInputStream(entry.getValue()));
        }
//        try {
//            requestParams.put("image_best", new File(bestPath));
//            requestParams.put("image_env", new File(envPath));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "https://api.megvii.com/faceid/v2/verify";
        asyncHttpClient.post(url, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        //请求成功
                        if (bytes != null) {
                            String success = new String(bytes);
                            Log.e("TAG", "成功信息：" + success);
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers,
                                          byte[] bytes, Throwable throwable) {
                        // 请求失败
                        if (bytes != null) {
                            String error = new String(bytes);
                            Log.e("TAG", "失败信息：" + error);
                        } else {
                            Log.e("TAG", "失败信息：" + throwable);
                        }


                    }
                });
    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, WebViewBannerActivity.class);
        startActivity(intent);
    }

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, FaceVetifyResultActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.result_next) {
            finish();
        }
    }

    private void doRotate(boolean success) {
        RotaterView rotaterView = (RotaterView) findViewById(R.id.result_rotater);
        rotaterView.setColour(success ? 0xff4ae8ab : 0xfffe8c92);
        final ImageView statusView = (ImageView) findViewById(R.id.result_status);
        statusView.setVisibility(View.INVISIBLE);
        statusView.setImageResource(success ? R.drawable.result_success
                : R.drawable.result_failded);

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(rotaterView,
                "progress", 0, 100);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.setDuration(600);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Animation scaleanimation = AnimationUtils.loadAnimation(
                        FaceVetifyResultActivity.this, R.anim.scaleoutin);
                statusView.startAnimation(scaleanimation);
                statusView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    private MediaPlayer mMediaPlayer = null;

    private void doPlay(int rawId) {
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();

        mMediaPlayer.reset();
        try {
            AssetFileDescriptor localAssetFileDescriptor = getResources()
                    .openRawResourceFd(rawId);
            mMediaPlayer.setDataSource(
                    localAssetFileDescriptor.getFileDescriptor(),
                    localAssetFileDescriptor.getStartOffset(),
                    localAssetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception localIOException) {
            localIOException.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
    }

    @Override
    protected BasePresenter oncreatePresenter() {
        faceVitifyResultPresenter = new FaceVitifyResultPresenter();
        return faceVitifyResultPresenter;
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Override
    public void resultStatus(int status, String message) {
        checkID(status);
        mImageView.setImageResource(status == R.string.verify_success ? R.drawable.result_success
                : R.drawable.result_failded);
    }

    @Override
    public void doRateAnim(boolean isSucess) {
        doRotate(isSucess);
    }
}