package com.panshi.hujin2.iloan.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panshi.hujin2.iloan.BuildConfig;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.model.SharePrefrencesModel;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.MainPresenter;
import com.panshi.hujin2.iloan.util.BoolUtil;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.ShareSdk;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.activity.MainActivity;
import com.panshi.hujin2.iloan.view.activity.WebViewBannerActivity;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;
import com.panshi.hujin2.iloan.view.widget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class ActiveFrament extends MVPBaseFragment {
    private View view;
    private WebView webView;
    private RelativeLayout errRl;
    private Dialog loadingDialog;
    private ImageView mback;
    private TextView mtvTitle;
    private TextView mtvCenterTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.active_layout, null);
        initWebView();
        return view;
    }

    @SuppressLint("JavascriptInterface")
    public void initWebView() {
        mback = view.findViewById(R.id.img_back);
        mtvTitle = view.findViewById(R.id.tv_title);
        mtvTitle.setVisibility(View.GONE);
        mtvCenterTitle = view.findViewById(R.id.tv_title_center);
        mtvCenterTitle.setVisibility(View.VISIBLE);
        mtvCenterTitle.setText(getResources().getString(R.string.discovery));
        mback.setVisibility(View.GONE);
        webView = (WebView) view.findViewById(R.id.wb_active);
        errRl = (RelativeLayout) view.findViewById(R.id.err_rl);
        WebSettings webSettings = webView.getSettings();//获得webSettings设置对象
        //设置支持h5的localStorage属性
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        //下面3行是WebView支持JS并能够和JS代码间进行交互的设置
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setJavaScriptEnabled(true);//设置支持javascript脚本
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//可以自动打开窗口
        webSettings.setSupportZoom(true);//设置是否支持变焦(缩放)
        webSettings.setBuiltInZoomControls(true);//设置显示缩放按钮
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
//		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应屏幕，内容将自动缩放
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.addJavascriptInterface(new ActiveFrament.JsInterface(), "JsInterface");   //注册js回调类
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
        webSettings.setPluginState(WebSettings.PluginState.ON);//这里是支持flash的相关设置
        webView.getSettings().setSupportMultipleWindows(true);
        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT > 19) {
                webView.setWebContentsDebuggingEnabled(true);
            }
        }
        //辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
        webView.setWebViewClient(new ActiveFrament.MyWebViewClient());
        // webView.setDownloadListener(new WebViewBannerActivity.MyWebViewDownLoadListener());
        loadUrl();
    }

    private void loadUrl() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (!TextUtils.isEmpty(mainActivity.mActiveUrl)) {
            webView.loadUrl(mainActivity.mActiveUrl);
        } else {
            webView.loadUrl(Constant.WEB_URL + Constant.HTML_ACITVE);
        }
    }


    @Override
    protected BasePresenter oncreatePresenter() {
        return new MainPresenter();
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            if (url.indexOf("tel:") >= 0) {// 页面上有数字会导致系统会自动连接电话,屏蔽此功能
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return true;
            }
            //	DebugLog.e("sang", "url =>"+url);
            Uri uri = Uri.parse(url);
            String browser = uri.getQueryParameter("browser");
            String fullScreen = uri.getQueryParameter("fullscreen");
            if (url.contains(".apk")) {//下载游戏操作，URL从浏览器打开网页
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else {//正常在webview打开网页
                if (url.startsWith("http://file")) {
                    HashMap<String, String> header = new HashMap<>();
                    header.put("Access-Control-Allow-Origin", "*");
                    header.put("Access-Control-Allow-Methods", "POST, GET, OPTIONS , PUT , DELETE");
                    header.put("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept ,pms-locale");
                    header.put("Access-Control-Allow-Credentials", "true");
                    header.put("g2-token", AppStaus.token);
                    header.put("g2-latitude", AppStaus.latitude);
                    header.put("g2-longitude", AppStaus.longitude);
                    header.put("g2-deviceId", AppStaus.MAC);
                    header.put("g2-uid", String.valueOf(AppStaus.userInfo.getId()));
                    view.loadUrl(url);
                } else if (uri.getBooleanQueryParameter("newWeb", false)) {
                    Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                    intent.putExtra("webUrl", url);
                    startActivity(intent);
                    return true;
                } else {
                    view.loadUrl(url);
                }

            }
            return super.shouldOverrideUrlLoading(webView, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            String uriStr = request.getUrl().toString();
            if (uriStr.startsWith("http://file")) {
                File file = new File(uri.getPath().trim());
                if (file.exists()) {
                    try {
                        FileInputStream inputStream = new FileInputStream(file);
                        WebResourceResponse response = new WebResourceResponse("image/jpg", "UTF-8", inputStream);
                        return response;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
            webView.setVisibility(View.VISIBLE);
            Activity activity = getActivity();
            if (activity != null) {
                if (!activity.isFinishing()) {
                    loadingDialog = LoadingDialog.createLoadingDialog(getActivity(), getResources().getString(R.string.data_loading));
                    loadingDialog.show();
                }

            }

        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = view.getTitle();
            if (!TextUtils.isEmpty(title)) {
                //      mtilte.setText(title);
            }
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
         /*   if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }*/
            webView.setVisibility(View.GONE);
            errRl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @author Administrator JS回调类
     */
    public class JsInterface {
        /**
         * 获取用户信息
         *
         * @return
         */
        @JavascriptInterface
        public String getUserInfo() {
            DebugLog.i("wang", "===js调用用户信息===");
            if (AppStaus.userInfo != null) {
                AppStaus.userInfo.setMac(AppStaus.MAC);
                AppStaus.userInfo.setLatitude(AppStaus.latitude);
                AppStaus.userInfo.setLongitude(AppStaus.longitude);
                AppStaus.userInfo.setVest(Constant.apkType);
                String gson = GonsonUtil.buildGosn().toJson(AppStaus.userInfo);
                DebugLog.i("wang", "==userInfo==" + gson);
                return gson;
            }
            return "";
        }

        /**
         * 复制剪贴板
         *
         * @param cutContent
         */
        @JavascriptInterface
        public void cutText(final String cutContent) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(cutContent)) {
                        if (BoolUtil.fastDouble(getActivity())) {
                            return;
                        }
                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(cutContent);
                        ToastUtil.showToast(getString(R.string.cut_conent));
                    } else {
                        //    ToastUtil.showToast(getString(R.string.cut_conent_failture));
                    }
                }
            });

        }

        @JavascriptInterface
        public void share(String json) {
            DebugLog.i("wang", "==shareJson==" + json);
            try {
                JSONObject obj = new JSONObject(json);
                String url = "";
                String title = "";
                String content = "";
                String imgUrl = "";
                String activeId = "";
                int shareType;
                if (obj.has("url")) {
                    //  if (!obj.isNull(url)) {
                    url = obj.getString("url");
                    //   }
                }
                if (obj.has("title")) {
                    //   if (!obj.isNull("title")) {
                    title = obj.getString("title");
                    //    }
                }
                if (obj.has("content")) {
                    //if (!obj.isNull("content")) {
                    content = obj.getString("content");
                    //}
                }
                if (obj.has("imageUrl")) {
                    // if (!obj.isNull("imageUrl")) {
                    imgUrl = obj.getString("imageUrl");
                    //    }
                }
                if (obj.has("activeiId")) {
                    //    if(!obj.isNull("activeiId")){
                    activeId = obj.getString("activeiId");
                    //     }
                }
                if (!imgUrl.startsWith("http")) {
                    imgUrl = Constant.WEB_URL + imgUrl;
                }
                shareType = obj.getInt("shareType");
                DebugLog.i("wang", "==imageUrl==" + imgUrl);
                doshare(shareType, title, content, url, imgUrl, activeId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface  //退出
        public void LoginOut() {
            SharePrefrencesModel sharePrefrencesModel = new SharePrefrencesModel(AppStaus.mContext);
            sharePrefrencesModel.deleteAll();
            Intent intent = new Intent();
            intent.setAction("com.panshi.hujin2.loancash.login");
            if (AppStaus.mContext != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Constant.LOGIN_OUT = 1;
                AppStaus.mContext.startActivity(intent);
            }
        }

        @JavascriptInterface //分享twiiter
        public void shareTwiiter(String json) {

            try {
                JSONObject obj = new JSONObject(json);
                String activeId = obj.getString("activeiId");
                String cutContent = obj.getString("cutContent");
                shareTwiiter1(activeId, cutContent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void shareTwiiter1(final String activeId, final String cutContent) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(cutContent)) {
                    if (BoolUtil.fastDouble(getActivity())) {
                        return;
                    }
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(cutContent);
                    ToastUtil.showToast(getString(R.string.share_content));
                    Intent intent = new Intent(getActivity(), WebViewBannerActivity.class);
                    intent.putExtra("webUrl", "https://twitter.com/");
                    startActivity(intent);
                    requestTotalShareAccount(Integer.parseInt(activeId), 1);
                } else {
                    //    ToastUtil.showToast(getString(R.string.cut_conent_failture));
                }
            }
        });
    }

    /**
     * 分享
     *
     * @param shareType
     * @param title     标题
     * @param content   内容
     * @param url       分享地址
     * @param imageUrl  分享图片地址
     */
    public void doshare(int shareType, String title, String content, String url, String imageUrl, String activeId) {
        ShareSdk shareSdk = new ShareSdk(getActivity());
        Platform platform = null;
        switch (shareType) {
            case 0:
                platform = shareSdk.getFacebookPlatform();
                break;
            case 1:
                platform = shareSdk.getTwitterPlatform();
                break;
            case 2:
                platform = shareSdk.getLinkInPlatform();
                break;
            default:
                platform = shareSdk.getFacebookPlatform();
                break;

        }
        shareSdk.setCensus(true);
        shareSdk.setActiveId(Integer.parseInt(activeId));
        shareSdk.setSharePlatform(platform);
        shareSdk.setShareSucessListener(new ShareSdk.ShareSucuessCall() {
            @Override
            public void sucessCall() {
                //loadUrl();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.reload();
                    }
                });

            }
        });
        Platform.ShareParams shareParams = new Platform.ShareParams();
        switch (shareType) {
            case 0:  //faceook
                shareParams.setText(content);
                shareParams.setUrl(url);
                shareParams.setImageUrl(imageUrl);
                shareParams.setShareType(Platform.SHARE_WEBPAGE);
                break;
            case 1: //twitter
            case 2: //linkIn
                shareParams.setShareType(Platform.SHARE_WEBPAGE);
                shareParams.setTitle(title);
                shareParams.setUrl(url);
                shareParams.setTitleUrl(url);
                shareParams.setText(content+"\r\n"+url);
                shareParams.setImageUrl(imageUrl);
                break;
            default:
                shareParams.setText(content);
                shareParams.setTitleUrl(url);
                shareParams.setTitle(title);
                shareParams.setUrl(url);
                shareParams.setImageUrl(imageUrl);
                break;
        }
     //   shareSdk.share(null, url, imageUrl, content, title);
       shareSdk.share(shareParams);
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
                //  loadUrl();
                webView.reload();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!isHidden()) {
            loadUrl();
        }
        super.onHiddenChanged(hidden);
    }
}
