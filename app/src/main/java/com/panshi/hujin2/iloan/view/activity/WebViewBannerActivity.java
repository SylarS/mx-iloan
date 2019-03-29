package com.panshi.hujin2.iloan.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.megvii.livenesslib.LivenessActivity;
import com.megvii.livenesslib.util.ConUtil;
import com.panshi.hujin2.iloan.BuildConfig;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.CallRecordReq;
import com.panshi.hujin2.iloan.bean.LacationInfo;
import com.panshi.hujin2.iloan.bean.UserContactInputVOS;
import com.panshi.hujin2.iloan.model.SharePrefrencesModel;
import com.panshi.hujin2.iloan.presenter.BasePresenter;
import com.panshi.hujin2.iloan.presenter.WebBannerPresenter;
import com.panshi.hujin2.iloan.util.BoolUtil;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.DensityUtil;
import com.panshi.hujin2.iloan.util.FileUtils;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.ImageUtil;
import com.panshi.hujin2.iloan.util.LocationUtil;
import com.panshi.hujin2.iloan.util.MD5Util;
import com.panshi.hujin2.iloan.util.PermissionPageUtils;
import com.panshi.hujin2.iloan.util.RxUtils;
import com.panshi.hujin2.iloan.util.ShareSdk;
import com.panshi.hujin2.iloan.util.ToastUtil;
import com.panshi.hujin2.iloan.view.interfaces.WebViewInterface;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;
import com.panshi.hujin2.iloan.view.widget.LoadingDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.panshi.hujin2.iloan.util.ActivityCode.REQUEST_CODE_WEBVIEWBANNER_FACE_ID;
import static com.panshi.hujin2.iloan.util.ImageUtil.readPictureDegree;


public class WebViewBannerActivity extends MVPBaseActivity implements WebViewInterface, ViewTreeObserver.OnGlobalLayoutListener {

    private final static String TGA = "WebViewBannerActivity";
    private ValueCallback<Uri> mUploadMessage = null;
    private ValueCallback<Uri[]> mUploadArrayMessage = null;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int FILECHOOSER_RESULTCODE_ARRAY = 2;
    private static final int FILECHOOSER_RESULTCODE_FROM_JS_CAMERA = 3;            //js回调打开相机
    private static final int FILECHOOSER_RESULTCODE_FROM_JS_ALBUM = 4;            //js回调打开相册
    private static final int RESULTCODE_EditPicture = 5;                        //编辑图片
    private WebView webView;
    // private TextView titleTxt;
    RelativeLayout errRl;
    private String UploadImgURL;                                            //上传图片URL
    private String UploadType;                                                //微钱贷上传照片
    private String UploadUid;
    private String CameraPhotoPath;                                            //拍照文件图片路径
    private BitmapFactory.Options UploadBitmapOptions = null;
    private String url;
    // private Toolbar mToolbar;
    // private RelativeLayout rlTitle;
    private RxPermissions rxPermissions;
    private WebBannerPresenter webBannerPresenter;
    private String tempName, tempName2;                //临时照片文件路径
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private Uri uri;
    private Uri uritempFile;
    private int count = 1;
    private String imageFullPath = null;    //图片地址
    File uploadFile = null;
    private HashMap<String, File> fileMap = new HashMap<>();
    private HashMap<String, String> sizeMap = new HashMap<>();
    private HashMap<String, String> scaleMap = new HashMap<>();
    private HashMap<String, String> pasthMap = new HashMap<>();
    private String fileTag;
    private String path_icon;
    private HashMap<String, String> param;
    private String jsonParanStr = "";
    private TextView mtilte;
    private ImageView mimgBack;
    private Dialog loadingDialog;
    private int misOver = 0;
    private int camaraAccout = 1;
    int lastHeight;
    int lastVisibleHeight;
    private String ContactJSONStr;
    private List<UserContactInputVOS> listcontact;
    private RelativeLayout mrlBack;
    private LocationListener locationListener;
    private TextView mtvTicketCenter;

    private void initUploadBitmapOptions() {
        UploadBitmapOptions = new BitmapFactory.Options();
        UploadBitmapOptions.inSampleSize = 4;
        UploadBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        UploadBitmapOptions.inPurgeable = true;
        UploadBitmapOptions.inInputShareable = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_bannerview);
        rxPermissions = new RxPermissions(this);
        // url ="http://10.0.52.223:8100/#/review";
        //url = "http://10.0.52.223:8100";
        url = Constant.WEB_URL;
        String urlPath = getIntent().getStringExtra("urlPath");
        if (!TextUtils.isEmpty(urlPath)) {
            url = url + urlPath;
        }
        //webview 跳转过来url
        String webUrl = getIntent().getStringExtra("webUrl");
        if (!TextUtils.isEmpty(webUrl)) {
            url = webUrl;
        }
        DebugLog.i("wang", "===url" + url);
        String type = getIntent().getStringExtra(Constant.HTML_PARAM_Type);
        if (!TextUtils.isEmpty(type) && TextUtils.equals(type, Constant.HTML_PARAM_Str)) {
            jsonParanStr = getIntent().getStringExtra(Constant.HTML_PARAM_Data);
        } else {
            if (getIntent().getSerializableExtra(Constant.HTML_PARAM) != null) {
                param = (HashMap<String, String>) getIntent().getSerializableExtra(Constant.HTML_PARAM);
            }
        }
        init();
        initListener();
        //   ordenSucessEven();
        if (!BuildConfig.DEBUG) {
            //   postContact();
        }
        DebugLog.i("wang", "==main_thread==" + Thread.currentThread().getName());
        locationListener = new WebLocation(this.getApplicationContext());
        webView.getViewTreeObserver().addOnGlobalLayoutListener(this);
//        findViewById(R.id.cccccc).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLocationPermission();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (goSetting == 1) {
            getLocationPermission();
        }
    }

    private int goSetting = 0;
    private AlertDialog permissionDialog;

    //定位权限
    public void getLocationPermission() {
        rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new CusumeObserver<Permission>() {
            @Override
            public void onNext(@NonNull Permission permission) {
                super.onNext(permission);
                if (permission.granted) {
                    LocationUtil.getLngAndLat(WebViewBannerActivity.this.getApplicationContext(), locationListener);
                    goSetting = 0;
                    if (permissionDialog != null && permissionDialog.isShowing()) {
                        permissionDialog.cancel();
                    }
                } else {
                    if (permission.shouldShowRequestPermissionRationale) {
                        getLocationPermission();
                        goSetting = 0;
                        if (permissionDialog != null && permissionDialog.isShowing()) {
                            permissionDialog.cancel();
                        }
                    } else {
                        if (permissionDialog == null || !permissionDialog.isShowing()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewBannerActivity.this);
                            builder.setMessage(R.string.permission_location);
                            builder.setCancelable(false);
                            builder.setPositiveButton(getResources().getString(R.string.permission_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                            permissionDialog = builder.create();
                            permissionDialog.show();
                            permissionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    goSetting = 1;
                                    PermissionPageUtils permissionPageUtils = new PermissionPageUtils(WebViewBannerActivity.this);
                                    permissionPageUtils.jumpPermissionPage();
                                }
                            });
                        }
                    }
                }
            }
        });
    }


    @Override
    protected BasePresenter oncreatePresenter() {
        return webBannerPresenter = new WebBannerPresenter();
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }


    private void init() {
        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });*/
        webView = (WebView) findViewById(R.id.webview_content);
        errRl = (RelativeLayout) findViewById(R.id.err_rl);
        mtvTicketCenter = (TextView) findViewById(R.id.tv_ticket_center);
        mtilte = (TextView) findViewById(R.id.tv_title);
        mrlBack = (RelativeLayout) findViewById(R.id.rl_back1);
        mimgBack = (ImageView) findViewById(R.id.img_back);
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
        webView.addJavascriptInterface(new JsInterface(), "JsInterface");   //注册js回调类
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
        webSettings.setPluginState(WebSettings.PluginState.ON);//这里是支持flash的相关设置
        webView.getSettings().setSupportMultipleWindows(true);
        if (BuildConfig.DEBUG) {
            if (Build.VERSION.SDK_INT > 19) {
                webView.setWebContentsDebuggingEnabled(true);
            }
        }
        //辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.loadUrl(url);
    }

    private void initListener() {
        mrlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.i("wang", "==isOver===" + misOver);
                //      faceBookShare();
                //       linkShare();
                //    doshare(2,"111","'Aquí hay sorteos de iPhone XS, Samsung S8, auriculares y otros preciosos premios  más recientes son gratis! Los premios son limitados, participen de inmediato'","http://52.52.134.68:9020/escrowwap/firstProm","http://52.52.134.68:9020/escrowwap/static/media/shure.b3900cd7.png","0");
                if (misOver == 1) {
                    Constant.REFRESH_ORDER = 1;
                    Intent intent = new Intent(WebViewBannerActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
     /*   mimgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });*/

        mtvTicketCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                webView.loadUrl(Constant.WEB_URL + Constant.HTML_TCIKET_CENTER);
            }
        });
    }

    @Override
    public void onGlobalLayout() {
        Rect visible = new Rect();
        Rect size = new Rect();
        webView.getWindowVisibleDisplayFrame(visible);
        webView.getHitRect(size);

        int height = size.bottom - size.top;
        int visibleHeight = visible.bottom - visible.top;
        DebugLog.i("wang", "==buttom=" + size.bottom + "||==top==" + size.top);
        if (height == lastHeight && lastVisibleHeight == visibleHeight) return;

        lastHeight = height;
        lastVisibleHeight = visibleHeight;
        DebugLog.i("wang", "==lastHeight=" + lastHeight + "||==lastVisibleHeight==" + lastVisibleHeight);
        String js = String.format("javascript:heightChange(%1$d , %2$d)", height, visibleHeight);
        webView.loadUrl(js);
    }


    private class MyWebChromeClient extends WebChromeClient {
        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            WebViewBannerActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            WebViewBannerActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            WebViewBannerActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadArrayMessage = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            WebViewBannerActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE_ARRAY);
            return true;
        }

        //
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String mtitle) {
            super.onReceivedTitle(view, mtitle);
            if (!TextUtils.isEmpty(mtitle)) {
                mtilte.setText(mtitle);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
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
                } else {
                    view.loadUrl(url);
                }

            }
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
           /* Map<String,String> header= request.getRequestHeaders();
            for(Map.Entry<String,String> h:header.entrySet()){
                DebugLog.i("wang","==key=="+h.getKey()+"==value=="+h.getValue());
            }*/
            Uri uri = request.getUrl();
            String uriStr = request.getUrl().toString();
            if (uriStr.startsWith("https://file")) {
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
            if (!isFinishing()) {
                loadingDialog = LoadingDialog.createLoadingDialog(WebViewBannerActivity.this, getResources().getString(R.string.data_loading));
                loadingDialog.show();
            }


        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = view.getTitle();
            if (!TextUtils.isEmpty(title)) {
                mtilte.setText(title);
            }
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            webView.setVisibility(View.GONE);
            errRl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //     webView.loadUrl("javascript:showInfoFromJava('" + msg + "')");
            //    webView.loadUrl("javascript:historyBack()");
            if (misOver == 1) {
                Constant.REFRESH_ORDER = 1;
                Intent intent = new Intent(WebViewBannerActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                WebViewBannerActivity.this.finish();
            }
        }
        return true;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
         * 获取贷款
         *
         * @return
         */
        @JavascriptInterface
        public String getLoanInfo() {
            if (param != null) {
                DebugLog.i("wang", GonsonUtil.buildGosn().toJson(param));
                return GonsonUtil.buildGosn().toJson(param);
            }
            return "";
        }

        /**
         * 获取订单信息
         *
         * @return
         */
        @JavascriptInterface
        public String getOrderInfo() {
            if (!TextUtils.isEmpty(jsonParanStr)) {
                return jsonParanStr;
            }
            return "";
        }

        /**
         * 获取订单信息
         *
         * @return
         */
        @JavascriptInterface
        public String getOrderDetail() {
            if (!TextUtils.isEmpty(jsonParanStr)) {
                return jsonParanStr;
            }
            return "";
        }

        @JavascriptInterface
        public void backToIndex(String reload) {
            DebugLog.i("wang", "====backToIndex======" + reload);
            if (reload.equals("true")) {
                DebugLog.i("wang", "==reload=" + reload);
                Constant.REFRESH_ORDER = 1;
            }
            Intent intent = new Intent(WebViewBannerActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            //   finish();
        }

        /**
         * 复制剪贴板
         *
         * @param cutContent
         */
        @JavascriptInterface
        public void cutText(final String cutContent) {
            WebViewBannerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(cutContent)) {
                        if (BoolUtil.fastDouble(WebViewBannerActivity.this)) {
                            return;
                        }
                        ClipboardManager cm = (ClipboardManager) WebViewBannerActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(cutContent);
                        ToastUtil.showToast(getString(R.string.cut_conent));
                    } else {
                        //    ToastUtil.showToast(getString(R.string.cut_conent_failture));
                    }
                }
            });

        }

        /**
         * 上传实名
         *
         * @param realJson
         */
        @JavascriptInterface
        public void postRealName(String realJson) {
            try {
                DebugLog.i("wang", "====str===" + realJson);
                JSONObject object = new JSONObject(realJson);
                boolean isFirst = object.getBoolean("isFirst");
                if (isFirst) {
                    if (fileMap != null) {
                        DebugLog.i("wang", "是第一次上传==");
                        webBannerPresenter.uploadFile(fileMap, pasthMap, sizeMap, scaleMap, realJson);
                    } else {
                        ToastUtil.showToast(getString(R.string.please_upload_card));
                    }
                } else {
                    DebugLog.i("wang", "===不是第一次上传==");
                    webBannerPresenter.uploadFile(fileMap, pasthMap, sizeMap, scaleMap, realJson);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 上传收入
         *
         * @param realJson
         */
        @JavascriptInterface
        public void postInCome(String realJson) {
            try {
                DebugLog.i("wang", "====str===" + realJson);
                JSONObject object = new JSONObject(realJson);

                if (fileMap != null) {
                    DebugLog.i("wang", "是第一次上传==");
                    webBannerPresenter.uploadIncomeFile(fileMap, pasthMap, sizeMap, scaleMap, realJson);
                } else {
                    ToastUtil.showToast(getString(R.string.please_upload_card));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        /*  *
         *s上传家庭信息
         */
        @JavascriptInterface
        public void uploadFamalyInfo(String famalyInfo) {
            webBannerPresenter.uploFamailyFile(fileMap, pasthMap, sizeMap, scaleMap, famalyInfo);
        }

        /**
         * 本地选择文件并上传（相机 or相册  ）
         * source	活动来源  111微钱贷(暂时没用到)
         */
        @JavascriptInterface
        public void openCamera(String fileTag) {
            DebugLog.i("wang", "====camera==" + fileTag);
            // DebugLog.e("sang", "zkzszd at localOpenFileUpload" + UploadUrl);
            WebViewBannerActivity.this.fileTag = fileTag;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).observeOn(AndroidSchedulers.mainThread()).subscribe(new CusumeObserver<Permission>() {
                        @Override
                        public void onNext(@NonNull Permission permission) {
                            super.onNext(permission);
                            if (permission.granted) {
                                DebugLog.i("wang", "==cc===" + camaraAccout);
                                if (camaraAccout == 1) {
                                    camaraAccout = 0;
                                    Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    try {
                                        path_icon = FileUtils.getRootDir(WebViewBannerActivity.this);
                                        tempName = path_icon + System.currentTimeMillis() + "_temp.jpg";
                                        tempName2 = path_icon + System.currentTimeMillis() + "yuantu" + "_temp.jpg";
                    /*这段代码不兼容7.0以上android系统
                    uri = Uri.fromFile(new File(tempName));
					*/
                                        ContentValues contentValues = new ContentValues(1);
                                        contentValues.put(MediaStore.Images.Media.DATA, tempName);
                                        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                                        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        startActivityForResult(cameraintent, PHOTO_REQUEST_CAMERA);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });

                }
            });
        }

        /**
         * 查询通讯率
         */
        @JavascriptInterface
        public void postContact(final String isUpload) {
            DebugLog.i("wang", "==js_thread==" + Thread.currentThread().getName());
            ;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DebugLog.i("wang", "==run_thread==" + Thread.currentThread().getName());
                    rxPermissions.requestEach(Manifest.permission.READ_CONTACTS).observeOn(AndroidSchedulers.mainThread()).subscribe(new CusumeObserver<Permission>() {
                        @Override
                        public void onNext(@NonNull Permission permission) {
                            super.onNext(permission);
                            if (permission.granted) {
                                if (listcontact == null) {
                                    listcontact = readContact();
                                }
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put(Constant.JS_CODE, Constant.JS_SUCESS_CODE);
                                map.put(Constant.JS_DATA, listcontact);
                                DebugLog.i("wang", "==contact=" + GonsonUtil.buildGosn().toJson(map));
                                DebugLog.i("wang", "======================js contact=============================" + System.currentTimeMillis());
                                webView.loadUrl("javascript:notifyContact('" + GonsonUtil.buildGosn().toJson(map) + "')");
                                // webView.loadUrl("javascript:notifyRealName('" + 1 + "," + "成功" + "')");
                                if (listcontact.size() > 0) {
                                    //   if (!BuildConfig.DEBUG) {
                                    if ("true".equals(isUpload)) {
                                        webBannerPresenter.uploadContact(listcontact);
                                    }
                                    // }

                                } else {
                                    DebugLog.i("wang", "没读取到通讯率");
                                }
                            } else {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put(Constant.JS_CODE, Constant.JS_FAILTURE_CODE);
                                map.put(Constant.JS_DATA, getResources().getString(R.string.refure_read_contact));
                                webView.loadUrl("javascript:notifyContact('" + GonsonUtil.buildGosn().toJson(map) + "')");
                            }

                        }
                    });
                }
            });


        }

        @JavascriptInterface
        public void isOver(String isOver) {
            DebugLog.i("wang", "==isOver==" + isOver);
            if (("true").equals(isOver)) {
                misOver = 1;
            } else {
                misOver = 0;
            }
        }

        @JavascriptInterface
        public void uploadCallRecord() {
            DebugLog.i("wang", "===js===");
            readCallLog();
        }

        @JavascriptInterface
        public void uploadSms() {
            DebugLog.i("wang", "===js===sms");
            uploadSMSInfo();

        }

        @JavascriptInterface
        public void regitLocation() {
            getLocationPermission();
        }

        @JavascriptInterface
        public void unregitLocation() {
            LocationUtil.unRegitLocation(WebViewBannerActivity.this.getApplicationContext(), locationListener);
        }

        @JavascriptInterface
        public void ordenSucess() {
            DebugLog.i("wang", "====js调用===ordenSucess");
            ordenSucessEven();
        }

        @JavascriptInterface
        public void share(String json) {
            try {
                JSONObject obj = new JSONObject(json);
                String url = "";
                String title = "";
                String content = "";
                String imgUrl = "";
                String activeId = "";
                int shareType;
                if (obj.has("url")) {
                    if (!obj.isNull(url)) {
                        url = obj.getString("url");
                    }
                }
                if (obj.has("title")) {
                    if (!obj.isNull("title")) {
                        title = obj.getString("title");
                    }
                }
                if (obj.has("content")) {
                    if (!obj.isNull("content")) {
                        content = obj.getString("content");
                    }
                }
                if (obj.has("imageUrl")) {
                    if (!obj.isNull("imageUrl")) {
                        imgUrl = obj.getString("imageUrl");
                    }
                }
                if (obj.has("activeiId")) {
                    if (!obj.isNull("activeiId")) {
                        activeId = obj.getString("activeiId");
                    }
                }
                shareType = obj.getInt("shareType");
                if (!imgUrl.startsWith("http")) {
                    imgUrl = Constant.WEB_URL + imgUrl;
                }
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

        @JavascriptInterface   //领券中心
        public void showTicket(final String isShow) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ("1".equals(isShow)) {
                        if (mtvTicketCenter != null) {
                            mtvTicketCenter.setText(getResources().getString(R.string.ticket_center));
                            mtvTicketCenter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (mtvTicketCenter != null) {
                            mtvTicketCenter.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

        @JavascriptInterface   //领券中心
        public void requestFaceCamera() {
            requestFaceCamera();
        }

        @JavascriptInterface   //faceid
        public void uploadfaceNow() {
            goFaceId();
        }

    }

    public void ordenSucessEven() {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.LEVEL, 9);
        eventValue.put(AFInAppEventParameterName.SCORE, 100);
        AppsFlyerLib.getInstance().trackEvent(WebViewBannerActivity.this.getApplicationContext(), "ordenSucess", eventValue);
        DebugLog.i("wang", "=====ordenSucessEvent===");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        DebugLog.i("wang", "===相机==" + requestCode + "||" + resultCode);
        if (requestCode == PHOTO_REQUEST_CAMERA) { //取消裁剪功能，暂时不用
            camaraAccout = 1;
            if (resultCode == Activity.RESULT_OK) {
                //    startPhotoZoom(uri);

                getImageToView(uri);
                //获取照片大小
//                Bitmap bitmap = getBitmapFromUri(getApplication().getContentResolver(), uri);
//                Log.i("wzx",bitmap.getWidth()+"*"+bitmap.getHeight());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                webView.loadUrl("javascript:notifyImg('" + "cancelCamera" + "')");
            }
        } else if (requestCode == REQUEST_CODE_WEBVIEWBANNER_FACE_ID && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            webBannerPresenter.uploadFaceFile(bundle);
        }
        /*else if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    ToastUtil.showToast(getResources().getString(R.string.select_file_error));
                    return;
                }

                Uri selectedImage = data.getData();
                if (selectedImage == null) {
                    ToastUtil.showToast(getResources().getString(R.string.select_file_error));
                    return;
                } else {
                    startPhotoZoom(data.getData(), false);
                }
            }
        } *//*else if (requestCode == 4) {
            //   if (data != null) {
            getImageToView(data);
            //    } else {
//				Toast.makeText(this, "裁剪返回出错", Toast.LENGTH_SHORT).show();
            //     }
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @param uri 路径
     */
    public void startPhotoZoom(Uri uri) {

        startPhotoZoom(uri, true);
    }

    /*
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri, boolean isRotate) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", DensityUtil.dip2px(this, 150));
        intent.putExtra("outputY", DensityUtil.dip2px(this, 150));
        intent.putExtra("scale", true);//黑边
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        /*这段代码不兼容7.0以上android系统
        intent.putExtra("return-data", true);
		startActivityForResult(intent, 4);*/
        count++;
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + count + "small.jpg");

        if (!TextUtils.isEmpty(tempName) && isRotate) {
            int degree = readPictureDegree(tempName);
            if (!(degree == 0 || degree == 360)) {
                Bitmap newBitmap = rotaingImageView(degree, ImageUtil.getBitmap(tempName));

                //   ImageUtil.saveBitmap(newBitmap, tempName);
            }
        }
        getImageToView(uri);
        DebugLog.i("wang", "====" + tempName);

     /*   intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 4);*/
    }

    /**
     * @param degree 旋转角度
     * @param bitmap 原图片
     * @return 旋转后的图片
     */
    private Bitmap rotaingImageView(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap newBitmap = null;
        try {
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            throw new OutOfMemoryError("内存溢出");
        } finally {
            if (newBitmap == null) {
                return bitmap;
            } else {
                if (bitmap != null && !bitmap.isRecycled())
                    bitmap.recycle();
            }
        }
        return newBitmap;
    }


    /* 保存裁剪之后的图片数据
     * @param picdata
     */
    private void getImageToView(Uri uri) {
        /*这段代码不兼容7.0以上android系统
        Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			//保存本地
			saveImage(photo);
		}*/
        Bitmap photo;
        try {
            photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            saveImage(photo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveImage(Bitmap bm) {
        try {
            imageFullPath = path_icon + MD5Util.md5("headurl_" + "_temp") + System.currentTimeMillis() + fileTag + ".jpg";//保存头像在本地

            DebugLog.i("wang", "==temp==size=" + new File(tempName).length());
            ImageUtil.saveBitmap(bm, imageFullPath, new File(tempName), false);

            Bitmap btm = bm;
            String imageFullPathYuantu = path_icon + MD5Util.md5("headurl_" + "_temp") + System.currentTimeMillis() + fileTag + "yuantu" + ".jpg";//保存头像在本地
            ImageUtil.saveBitmap(btm, imageFullPathYuantu, new File(tempName2), true);

            Log.i("wzx", btm.getWidth() + "*" + btm.getHeight());
            String size = getFileSizeByUrl(imageFullPathYuantu);
            Log.i("wzx", "imageFullPath 大小" + size);
            //imageFullPath uri


            if (tempName != null && new File(tempName).exists()) {
                new File(tempName).delete();
            }
            if (tempName2 != null && new File(tempName2).exists()) {
                new File(tempName2).delete();
            }
            File uploadFile = new File(imageFullPath);
            if (uploadFile.exists()) {
                fileMap.put(fileTag, uploadFile);
                sizeMap.put(fileTag + "ResolutionCap", btm.getWidth() + "*" + btm.getHeight());
                scaleMap.put(fileTag + "Size", size);
                pasthMap.put(fileTag + "Path", imageFullPath);

                final String url = "https://" + "file" + uploadFile.getPath();
                DebugLog.i("wang", "==url==" + url);
                webView.loadUrl("javascript:notifyImg('" + url + "')");

            } else {
                webView.loadUrl("javascript:notifyImg('" + "null" + "')");
                DebugLog.i("wang", "==文件不存在");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
            }
        }
    }

    private List<UserContactInputVOS> readContact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<UserContactInputVOS> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //   list.add(displayName + '\n' + number);
                UserContactInputVOS userContactInputVOS = new UserContactInputVOS();
                userContactInputVOS.setContactName(displayName);
                userContactInputVOS.setContactPhone(number);
                DebugLog.i("wang", "==name==" + displayName + "||" + "=phone==" + number);
                if (!list.contains(userContactInputVOS)) {
                    list.add(userContactInputVOS);
                }
                DebugLog.i("wang", "==time==" + (System.currentTimeMillis() - start));
            }
            //notify公布

        }
        return list;
    }

    ;

    /**
     * 读取通话记录
     */
    public void readCallLog() {
        final List<CallRecordReq.UserCallRecordInputVOSBean> listContact = new ArrayList<>();
        rxPermissions.requestEach(Manifest.permission.READ_CALL_LOG).observeOn(AndroidSchedulers.mainThread()).subscribe(new CusumeObserver<Permission>() {
            @Override
            public void onNext(@NonNull Permission permission) {
                super.onNext(permission);
                if (permission.granted) {
                    ContentResolver cr = getContentResolver();
                    Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
                            CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION
                    }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
                    while (cursor.moveToNext()) {
                        CallRecordReq.UserCallRecordInputVOSBean userCallRecordInputVOSBean = new CallRecordReq.UserCallRecordInputVOSBean();
                        /*  for (cursor.moveToFirst(); (!cursor.isAfterLast())*//* && i < num; cs.moveToNext(), i++*//*) {*/
                        String callName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //名称
                        String callNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
                        //如果名字为空，在通讯录查询一次有没有对应联系人
                        if (callName == null || callName.equals("")) {
                            callName = "未知";
                               /* String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME};
                                //设置查询条件
                                String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + "='" + callNumber + "'";
                                Cursor cs = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        cols, selection, null, null);
                                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                                if (cs.getCount() > 0) {
                                    cs.moveToFirst();
                                    callName = cs.getString(nameFieldColumnIndex);
                                }
                                cs.close();*/
                        }
                        //通话类型
                        int callType = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
                        String callTypeStr = "";
                        switch (callType) {
                            case CallLog.Calls.INCOMING_TYPE:
                                //   callTypeStr = CallLogInfo.CALLIN;
                                break;
                            case CallLog.Calls.OUTGOING_TYPE:
                                //    callTypeStr = CallLogInfo.CALLOUT;
                                break;
                            case CallLog.Calls.MISSED_TYPE:
                                //  callTypeStr = CallLogInfo.CAllMISS;
                                break;
                            default:
                                //其他类型的，例如新增号码等记录不算进通话记录里，直接跳过
                                Log.i("ssss", "" + callType);
                                // i--;
                                continue;
                        }
                        String callDurationStr = "";
                        if (callType == CallLog.Calls.MISSED_TYPE) {

                        } else {
                            callDurationStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                        }
                        String callDateStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                        //     String callDurationStr = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                        Log.i("Msg", "callName" + callName);
                        Log.i("Msg", "callNumber" + callNumber);
                        Log.i("Msg", "callTypeStr" + callType);
                        Log.i("Msg", "callDateStr" + callDateStr);
                        Log.i("Msg", "callDurationStr" + callDurationStr);
                        userCallRecordInputVOSBean.setContactName(callName);
                        userCallRecordInputVOSBean.setContactPhone(callNumber);
                        userCallRecordInputVOSBean.setRecordType(callType);
                        userCallRecordInputVOSBean.setStartTime(callDateStr);
                        if (!TextUtils.isEmpty(callDurationStr)) {
                            userCallRecordInputVOSBean.setTime(Integer.parseInt(callDurationStr));
                        } else {
                            userCallRecordInputVOSBean.setTime(0);
                        }

                        listContact.add(userCallRecordInputVOSBean);
                    }
                    cursor.close();
                    if (listContact.size() > 0) {
                        CallRecordReq callRecordReq = new CallRecordReq();
                        callRecordReq.setUserCallRecordInputVOS(listContact);
                        webBannerPresenter.uploadCallRecord(callRecordReq);
                    }
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.JS_CODE, Constant.JS_SUCESS_CODE);
                    map.put(Constant.JS_DATA, "");
                    DebugLog.i("wang", "==contact=" + GonsonUtil.buildGosn().toJson(map));
                    webView.loadUrl("javascript:notifyUploadCall('" + GonsonUtil.buildGosn().toJson(map) + "')");
                } else {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.JS_CODE, Constant.JS_FAILTURE_CODE);
                    map.put(Constant.JS_DATA, "");
                    DebugLog.i("wang", "==contact=" + GonsonUtil.buildGosn().toJson(map));
                    webView.loadUrl("javascript:notifyUploadCall('" + GonsonUtil.buildGosn().toJson(map) + "')");
                }
            }
            /*      }*/
        });
    }

    /**
     * 获取短信信息
     */
    private /*List<MesInfo>*/ void uploadSMSInfo() {
        DebugLog.i("wang", "====开始读取短信===");

        rxPermissions.requestEach(Manifest.permission.READ_SMS).observeOn(AndroidSchedulers.mainThread()).subscribe(new CusumeObserver<Permission>() {
            @Override
            public void onNext(@NonNull Permission permission) {
                super.onNext(permission);
                DebugLog.i("wang", "==permission==" + permission.granted);
                if (permission.granted) {
                    String[] field = {"_id", "address", "person", "body", "date", "type"};  //字段
                    ContentResolver contentResolver = WebViewBannerActivity.this.getContentResolver();  //内容解析器(数据共享)——读取短信内容
                    Cursor cursor = contentResolver.query(Uri.parse("content://sms/"), field, null, null, "date desc");  //获取短信内容
                    if (cursor == null) {  //游标无内容
                        return;
                    }
                    int num = 0;
                    //  list = new ArrayList<MesInfo>();
                    while (cursor.moveToNext()) {
                        String phoneNumber = cursor.getString(cursor.getColumnIndex("address"));  //手机号 发件人地址，即手机号，如+8613811810000
                        String name = cursor.getString(cursor.getColumnIndex("person"));  //联系人
                        String body = cursor.getString(cursor.getColumnIndex("body"));  //短信内容
                        //格式化短信日期
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date time = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))));
            String date = simpleDateFormat.format(time);*/
                        String date = cursor.getString(cursor.getColumnIndex("date"));
                        String type = cursor.getString(cursor.getColumnIndex("type"));//type 接受，2 发送
                        num++;
                        DebugLog.i("wang", "==phoneNum==" + phoneNumber + "===name==" + name + "==body===" + body + "==date==" + date + "==type==" + type);
                        //保存短信信息
                        //   list.add(new MesInfo(name, phoneNumber, date, body));
                    }
                    DebugLog.i("wang", "==num=" + num);
                    if (num > 0) {
                        webBannerPresenter.uploadSmsNum(num);
                    }
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.JS_CODE, Constant.JS_SUCESS_CODE);
                    map.put(Constant.JS_DATA, "");
                    DebugLog.i("wang", "==contact=" + GonsonUtil.buildGosn().toJson(map));
                    webView.loadUrl("javascript:notifySms('" + GonsonUtil.buildGosn().toJson(map) + "')");
                } else {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.JS_CODE, Constant.JS_FAILTURE_CODE);
                    map.put(Constant.JS_DATA, "");
                    DebugLog.i("wang", "==contact=" + GonsonUtil.buildGosn().toJson(map));
                    webView.loadUrl("javascript:notifySms('" + GonsonUtil.buildGosn().toJson(map) + "')");
                }

            }
        });

        return;
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
        ShareSdk shareSdk = new ShareSdk(this);
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
        shareSdk.setActiveId(Integer.parseInt(activeId));

        shareSdk.setSharePlatform(platform);
        shareSdk.share(null, url, imageUrl, content, title);
    }


    public void faceBookShare() {
        ShareSdk shareSdk = new ShareSdk(this);
        shareSdk.setSharePlatform(shareSdk.getFacebookPlatform());
        shareSdk.share(null, "http://www.sina.com/", "http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
    }

    public void linkShare() {
        ShareSdk shareSdk = new ShareSdk(this);
        shareSdk.setSharePlatform(shareSdk.getLinkInPlatform());
        shareSdk.share(null, "http://www.sina.com/", null);
    }


    @Override
    public void onUploadSucess() {
        DebugLog.i("wang", "==contact_uplaod_sucess");
    }

    @Override
    public void onUploadRealSucess() {
        DebugLog.i("wang", "===uploadRealSucess==");
        fileMap.clear();
        scaleMap.clear();
        sizeMap.clear();
        pasthMap.clear();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.JS_CODE, 0);
        map.put(Constant.JS_DATA, "成功");
        //    ToastUtil.showToast("上传成功");
        webView.loadUrl("javascript:notifyRealName('" + GonsonUtil.buildGosn().toJson(map) + "')");
    }

    @Override
    public void onUploadRealFailture(int code, String message) {
        DebugLog.i("wang", "===uploadRealFailtrue==");
        //   fileMap.clear();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.JS_CODE, code);
        map.put(Constant.JS_DATA, message);
        // ToastUtil.showToast(message);
        webView.loadUrl("javascript:notifyRealName('" + GonsonUtil.buildGosn().toJson(map) + "')");
    }


    /**
     * 位置信息监听
     */
    public class WebLocation implements LocationListener {
        private Context context;

        public WebLocation(Context context) {
            this.context = context;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                AppStaus.longitude = String.valueOf(location.getLongitude());
                AppStaus.latitude = String.valueOf(location.getLatitude());
                DebugLog.i("wang", "longitude:" + AppStaus.longitude + "||latitude:" + AppStaus.latitude);
                final HashMap<String, Object> map = new HashMap<String, Object>();
                LacationInfo lacationInfo = new LacationInfo();
                lacationInfo.setLatitude(location.getLatitude());
                lacationInfo.setLongitude(location.getLongitude());
                map.put(Constant.JS_CODE, 1);
                map.put(Constant.JS_DATA, lacationInfo);
                WebViewBannerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:getLocInfo('" + GonsonUtil.buildGosn().toJson(map) + "')");
                    }
                });
                LocationUtil.unRegitLocation(context.getApplicationContext(), this);
            } else {
                AppStaus.longitude = "0";
                AppStaus.latitude = "0";
                DebugLog.i("wang", "定位信息获取失败====开启网络定位");
                // LocationUtil.getLngAndLatWithNetwork(PhoneNumActivity.this,this);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private Disposable disposable;

    //人脸识别
    public void goFaceId() {
        rxPermissions.requestEach(Manifest.permission.CAMERA).subscribe(new CusumeObserver<Permission>() {
            @Override
            public void onNext(Permission permission) {
                super.onNext(permission);
                if (permission.granted) {
                    disposable = Flowable.
                            create(new FlowableOnSubscribe<Boolean>() {
                                @Override
                                public void subscribe(FlowableEmitter<Boolean> e) {
                                    Manager manager = new Manager(WebViewBannerActivity.this);
                                    LivenessLicenseManager licenseManager = new LivenessLicenseManager(WebViewBannerActivity.this);
                                    manager.registerLicenseManager(licenseManager);
                                    String uuid = ConUtil.getUUIDString(WebViewBannerActivity.this);
                                    manager.takeLicenseFromNetwork(uuid);
                                    e.onNext(licenseManager.checkCachedLicense() > 0);
                                }
                            }, BackpressureStrategy.BUFFER).
                            subscribeOn(Schedulers.newThread()).
                            observeOn(AndroidSchedulers.mainThread()).
                            subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) {
                                    if (aBoolean) {
                                        startActivityForResult(new Intent(WebViewBannerActivity.this, LivenessActivity.class), REQUEST_CODE_WEBVIEWBANNER_FACE_ID);
                                    } else {
                                        webView.loadUrl("javascript:uploadfacefinish('" + getResources().getString(R.string.meglive_camera) + "')");
                                    }
                                }
                            });
                } else {
                    webView.loadUrl("javascript:uploadfacefinish('failure')");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.cancelDisposable(disposable);
    }

    @Override
    public void onUploadFaceSuccessed() {
        webView.loadUrl("javascript:uploadfacefinish('success')");
    }

    @Override
    public void onUploadFaceFail(int s) {
        webView.loadUrl("javascript:uploadfacefinish('" + getResources().getString(s) + "')");
    }

    public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
        InputStream input = null;
        try {
            input = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            input.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取File大小
    public static String getFileSizeByUrl(Uri uri) {
        File file = null;
        try {
            file = new File(new URI(uri.toString()));
            if (file.exists() && file.isFile()) {
                return getPrintSize(file.length());
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String getFileSizeByUrl(String fil) {
        File file = new File(fil);
        if (file.exists() && file.isFile()) {
            return getPrintSize(file.length());
        }
        return "0";
    }

    public static String getPrintSize(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();

    }

}
