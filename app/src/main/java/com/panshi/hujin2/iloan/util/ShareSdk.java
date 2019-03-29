package com.panshi.hujin2.iloan.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.linkedin.LinkedIn;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

;


/**
 * 分享
 *
 * @author wxs
 */
public class ShareSdk {
    private Context mcontext;
    // 分享成功
    public static final int SHARESUCESS = 1;
    // 分享失败
    public static final int SHAREFAILUER = 2;
    // 分享取消
    public static final int SHARECANCLE = 3;
    // 分享平台
    Platform mplatform;
    // 加载dialog
    public LoadingDiaLog mloadingDiaLog;
    // 微信类型
    public static final int SHARE_WEIXIN = 1;
    // 朋友圈类型
    public static final int SHARE_FRIEND = 1;
    // QQ类型
    public static final int SHARE_QQ = 2;
    // 新浪类型
    public static final int SHARE_SINA = 3;
    //facebook分享
    public static final int FACEBOOK = 4;
    //Linkin分享
    public static final int LinkIn = 5;
    //twitter 分享
    public static final int Twitter = 6;

    public int currentType;
    private ShareSucuessCall sucuessCall;
    //分享成功是否统计
    private boolean isCensus = true;
    private int activeId;
    private int shareType;

    /**
     * 分享成功是否统计
     *
     * @param census
     */
    public void setCensus(boolean census) {
        isCensus = census;
    }


    public ShareSdk(Context context) {
        mcontext = context;
        mloadingDiaLog = new LoadingDiaLog();
    }

    /**
     * 实例化sdk
     */
    public void initSdk() {
        //ShareSDK.initSDK(mcontext);

    }

    /**
     *活动id
     */
    public void setActiveId(int activeId) {
       this.activeId=activeId;
    }

    /**
     * 分享类型
     * @param shareType
     */
    public void setShareType(int shareType){
       this.shareType=shareType;
    }

    /**
     * 設置分享平台
     *
     * @param platform
     */
    public void setSharePlatform(Platform platform) {
        mplatform = platform;
    }

    Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 分享
     *
     * @param bitmap
     * @param url
     * @param imgUrl
     */
    public void share(Bitmap bitmap, String url, String imgUrl) {
        String shareContent = "iloan 活动";

        share(bitmap, url, imgUrl, shareContent, "iloan");
    }

    /**
     * 自定义分享参数
     * @param shareParam
     */
    public void share(Platform.ShareParams  shareParam){
        mloadingDiaLog.loading();
          if(mplatform==null){
              try {
                  throw new Exception("请先设置分享平台");
              } catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
              return;
          }
         mplatform.setPlatformActionListener(new ShareListenter());
         mplatform.share(shareParam);
    }

    /**
     * 分享 "http://sharesdk.cn"
     */
    public void share(Bitmap bitmap, final String url, String imgUrl, String shareContent, String title) {
        mloadingDiaLog.loading();
        if (mplatform == null) {
            try {
                throw new Exception("请先设置分享平台");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }

        final Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        if (currentType == FACEBOOK || currentType == LinkIn) {
            sp.setUrl(url);
            sp.setTitleUrl(url);
        } else {
            sp.setTitleUrl(url);
        }
        if (currentType == SHARE_WEIXIN) {
            sp.setTitle(shareContent);
        } else {
            sp.setTitle(title);
        }
        if (currentType == SHARE_SINA||currentType==LinkIn) {
            sp.setText(shareContent +"\r\n  "+ url);
        } else {
            sp.setText(shareContent);
        }
        //sp.setImageData(bitmap);

        if (imgUrl != null && !imgUrl.startsWith("http")) {
            imgUrl = "https://" + imgUrl;
        }

        if (!TextUtils.isEmpty(imgUrl)) {
            sp.setImageUrl(imgUrl);


        }

        mplatform.setPlatformActionListener(new ShareListenter()); // 设置分享事件回调
        // 执行图文分享
        mplatform.share(sp);

    }

    /**
     * 分享监听
     *
     * @author wxs
     */
    class ShareListenter implements PlatformActionListener {

        @Override
        public void onCancel(Platform plat, int action) {
            // TODO Auto-generated method stub
            Message msg = Message.obtain();
            msg.arg1 = 3;
            msg.arg2 = action;
            msg.obj = plat;
            handerUI.sendMessage(msg);
        }

        @Override
        public void onComplete(Platform plat, int action, HashMap<String, Object> t) {
            // TODO Auto-generated method stub
            Message msg = Message.obtain();
            msg.arg1 = 1;
            msg.arg2 = action;
            msg.obj = plat;
            handerUI.sendMessage(msg);
        }

        @Override
        public void onError(Platform plat, int action, Throwable t) {
            // TODO Auto-generated method stub
            t.printStackTrace();
            DebugLog.e("======" + t.toString());
            Message msg = Message.obtain();
            msg.arg1 = 2;
            msg.arg2 = action;
            handerUI.sendMessage(msg);
        }

    }

    Handler handerUI = new Handler() {
        public void handleMessage(Message msg) {
            String text;
            if (mloadingDiaLog != null && mloadingDiaLog.dialog.isShowing()) {
                mloadingDiaLog.cancel();
            }
            switch (msg.arg1) {

                case SHARESUCESS:
                    // 成功
                    // text = "分享成功";
                    if (isCensus) {
                      //  Toast.makeText(mcontext, "分享成功", Toast.LENGTH_SHORT).show();
                        requestTotalShareAccount(activeId,shareType);
                    } else {
                   //     Toast.makeText(mcontext, "分享成功", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case SHAREFAILUER:
                    // 失败
                    text = "fracaso en compartir";
                    Toast.makeText(mcontext, text, Toast.LENGTH_SHORT).show();
                    break;
                case SHARECANCLE:
                    // 取消
                    text = "分享已取消";
                    if(isCensus){
                        requestTotalShareAccount(activeId,shareType);
                    }
                  //  Toast.makeText(mcontext, text, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    /**
     * 判断应用是否安装
     *
     * @param pageName 包名
     * @return
     */
    public boolean isAvilible(String pageName) {
        PackageManager manager = mcontext.getPackageManager();
        List<PackageInfo> pInfos = manager.getInstalledPackages(0);
        for (int i = 0; i < pInfos.size(); i++) {
            if (pageName.equals(pInfos.get(i).packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 微信平台
     *
     * @return
     *//*
	public Platform getWinXinSharePlatform() {
		currentType = SHARE_WEIXIN;
		return ShareSDK.getPlatform(mcontext, Wechat.NAME);

	}
*/
    /*
     */
/**
 * 朋友圈平台
 *
 * @return
 *//*

	public Platform getFriendsSharePlatform() {
		currentType = SHARE_FRIEND;
		return ShareSDK.getPlatform(mcontext, WechatMoments.NAME);
	}
*/

    /*
     */
/**
 * 新浪平台
 *
 * @return
 *//*

	public Platform getSinaSharePlatform() {
		currentType = SHARE_SINA;
		return ShareSDK.getPlatform(mcontext, SinaWeibo.NAME);
	}
*/

    /*
     */
/**
 * qq分享平台
 *
 * @return
 *//*

	public Platform getQQSharePlatform() {
		currentType = SHARE_QQ;
		return ShareSDK.getPlatform(mcontext, QQ.NAME);
	}
*/

    /**
     * facebook platform
     *
     * @return
     */
    public Platform getFacebookPlatform() {
        currentType = FACEBOOK;
        return ShareSDK.getPlatform(Facebook.NAME);
    }

    /**
     * twitter platform
     *
     * @return
     */
    public Platform getTwitterPlatform() {
        currentType = Twitter;
        return ShareSDK.getPlatform(cn.sharesdk.twitter.Twitter.NAME);
    }

    /**
     * linkin platform
     *
     * @return
     */
    public Platform getLinkInPlatform() {
        currentType = LinkIn;
        return ShareSDK.getPlatform(LinkedIn.NAME);
    }

    public class LoadingDiaLog {
        ProgressDialog dialog = new ProgressDialog(mcontext);

        public void loading() {
            dialog.setTitle("");
            dialog.setMessage("正在分享...");
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        public void cancel() {
            dialog.cancel();
        }
    }

    /**
     * 4是facebook 6是twiiter 5是linkIn
     */
    public void requestTotalShareAccount(int activeId, int channelsType) {
        RetrofitFactory.createFactoty().create(UserApi.class).uploadShareSucess(activeId, channelsType).subscribeOn(Schedulers.io()).filter(new Predicate<Response>() {
            @Override
            public boolean test(Response response) throws Exception {
                if (response.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                return false;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new CusumeObserver<Response>() {
            @Override
            public void onNext(Response response) {
                DebugLog.i("wang", "分享统计成功");
                super.onNext(response);
                if(sucuessCall!=null){
                    sucuessCall.sucessCall();
                }
            }
        });
    }

    public interface ShareSucuessCall {
        public void sucessCall();
    }

    public void setShareSucessListener(ShareSucuessCall sucuessCall) {
        this.sucuessCall = sucuessCall;
    }

    /**
     * 清楚消息
     */
    public void removeMessage() {

        if (mloadingDiaLog != null) {
            mloadingDiaLog.cancel();
        }
        if (handerUI != null) {
            handerUI.removeCallbacksAndMessages(null);
            handerUI = null;
        }
    }

}
