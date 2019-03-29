package com.panshi.hujin2.iloan.presenter;

import android.os.Bundle;

import com.megvii.livenesslib.util.ConUtil;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.view.interfaces.FaceVitifyView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class FaceVitifyResultPresenter extends BasePresenter {
    /**
     * 处理 face 返回的结果
     *
     * @param bundle
     */
    public void dealResult(Bundle bundle) {

        final String resultOBJ = bundle.getString("result");
        try {

            JSONObject result = new JSONObject(resultOBJ);
            int resID = result.getInt("resultcode");
            String resultMessage = result.getString("result");
            DebugLog.i("wang", "===resId==" + resID + "===" + resultMessage);
            if (resID == R.string.verify_success) {
                String delta = bundle.getString("delta");
                Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
                final byte[] image_best = images.get("image_best");
                final byte[] image_env = images.get("image_env");
                final byte[] image_action1 = images.get("action1");
                final byte[] image_action2 = images.get("action2");
                final byte[] image_action3 = images.get("action3");
                //N张动作图根据需求自取，对应字段action1、action2 ...
                // byte[] image_action1 = images.get("image_action1);
                final String ImageBestFileName = "image_best";
                final String imageEnvFileName = "image_env";
                final String imageAction1FileName = "image_action1";
                final String imageAction2FileName = "image_action2";
                final String imageAction3FileName = "image_action3";
                if (AppStaus.mContext != null) {
                    ConUtil.saveJPGFile(AppStaus.mContext, image_best, ImageBestFileName);
                    ConUtil.saveJPGFile(AppStaus.mContext, image_env, imageEnvFileName);
                    ConUtil.saveJPGFile(AppStaus.mContext, image_action1, imageAction1FileName);
                    ConUtil.saveJPGFile(AppStaus.mContext, image_action2, imageAction2FileName);
                    ConUtil.saveJPGFile(AppStaus.mContext, image_action3, imageAction3FileName);
                }
                Observable observableAction1 = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        ConUtil.saveJPGFile(AppStaus.mContext, image_action1, imageAction1FileName);
                    }
                }).subscribeOn(Schedulers.io());
                Observable observableAction2 = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        ConUtil.saveJPGFile(AppStaus.mContext, image_action2, imageAction2FileName);
                    }
                }).subscribeOn(Schedulers.io());

                Observable observableAction3 = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        ConUtil.saveJPGFile(AppStaus.mContext, image_action3, imageAction3FileName);
                    }
                }).subscribeOn(Schedulers.io());
                Observable observableBest = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        ConUtil.saveJPGFile(AppStaus.mContext, image_best, ImageBestFileName);
                    }
                }).subscribeOn(Schedulers.io());

                Observable observablEnv = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        ConUtil.saveJPGFile(AppStaus.mContext, image_env, imageEnvFileName);
                    }
                }).subscribeOn(Schedulers.io());

//                Observable.zip(observableAction1,observableAction2,observableAction3)

            } else {

                //  ll_result_image.setVisibility(View.GONE);
            }
            if (isViewAttach()) {
                ((FaceVitifyView) getAttach()).resultStatus(resID, resultMessage);
                ((FaceVitifyView) getAttach()).doRateAnim(resID == R.string.verify_success);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
