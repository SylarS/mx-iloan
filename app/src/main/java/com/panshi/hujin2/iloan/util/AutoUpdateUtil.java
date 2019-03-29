package com.panshi.hujin2.iloan.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panshi.hujin2.iloan.BuildConfig;
import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.R;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.AutoUpdateInfo;
import com.panshi.hujin2.iloan.bean.UpdateInfoRes;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.listener.ProgressListener;
import com.panshi.hujin2.iloan.view.widget.BaseDialog;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;
import com.panshi.hujin2.iloan.view.widget.ProgeressResponseBody;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * APK更新
 *
 * @version 时间：2016-10-11
 */
public class AutoUpdateUtil {

    /**
     * 服务器获取JSON数据,并判断版本号执行APK下载更新相关操作
     *
     * @param context
     * @param oldApkName 上次下载到本地的APK的名字
     * @param newApkName 本次下载到本地的APK的名字
     * @param url        服务器下发用来检测APK下载的网络地址
     * @param vercode    已安装的版本号
     */
    public static void getAutoInfo(final Activity context, final String oldApkName, final String newApkName, String url, final int vercode, final boolean isNoteVersion) {
        DebugLog.d("checkUpdate", "11111");
        final AutoUpdateInfo mInfo = new AutoUpdateInfo();
        RetrofitFactory.createFactoty().create(UserApi.class).getUpdateInfo().subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).filter(new Predicate<UpdateInfoRes>() {
            @Override
            public boolean test(@NonNull UpdateInfoRes updateInfoRes) throws Exception {
                DebugLog.d("checkUpdate", updateInfoRes.getCode() + ":::::" + updateInfoRes.getMessage() + "))" + updateInfoRes.isSuccess());
                if (updateInfoRes.getCode() == Constant.SUCESS_CODE) {
                    return true;
                }
                return false;
            }
        }).map(new Function<UpdateInfoRes, UpdateInfoRes.DataBean>() {
            @Override
            public UpdateInfoRes.DataBean apply(@NonNull UpdateInfoRes updateInfoRes) throws Exception {
                return updateInfoRes.getData();
            }
        }).subscribe(new Observer<UpdateInfoRes.DataBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull UpdateInfoRes.DataBean dataBean) {
                AutoUpdateInfo autoUpdateInfo = new AutoUpdateInfo();
                autoUpdateInfo.setApk_url(dataBean.getApkUrl());
                autoUpdateInfo.setNew_version(dataBean.getNewVersion());
                autoUpdateInfo.setNew_versionname(dataBean.getNewVersionName());
                autoUpdateInfo.setUpdate_log(dataBean.getUpdateLog());
                autoUpdateInfo.setStrong(dataBean.isForceUpdate());
                DebugLog.d("checkUpdate", "++++++");
//                checkUpdate(context, autoUpdateInfo, oldApkName, newApkName, vercode, Integer.parseInt(autoUpdateInfo.getNew_version()), isNoteVersion);
                if (Integer.valueOf(autoUpdateInfo.getNew_version()) > BuildConfig.VERSION_CODE) {
                    UpdateKotlin.popUpUpdate(autoUpdateInfo, context);
                }
            }


            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                DebugLog.d("checkUpdate", "_____");
            }

            @Override
            public void onComplete() {

            }
        });
        /*HttpUtil.get(url,new JsonHttpResponseHandler(){
            @Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (response != null) {
					try {
						mInfo.setApk_url(response.getString("apk_url"));
						mInfo.setNew_versionname(response.getString("new_versionname"));
						mInfo.setNew_version(response.getString("new_version"));
						mInfo.setUpdate_log(response.getString("update_log"));
						checkUpdate(context,mInfo,oldApkName,newApkName,vercode, Integer.valueOf(mInfo.getNew_version()));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});*/
    }

    /**
     * 检查版本号,服务器版本大于本地App版本显示更新弹窗
     *
     * @param context
     * @param info       服务器下发的json数据（包含版本号，版本code，APK下载链接地址等）
     * @param oldApkName
     * @param newApkName
     * @param vercode
     * @param newVercode isNoteVersion 是否提示最新版本
     */
    public static void checkUpdate(final Activity context, final AutoUpdateInfo info, final String oldApkName, String newApkName, int vercode, int newVercode, boolean isNoteVersion) {
        DebugLog.d("checkUpdate", newVercode + "|||" + vercode + "||" + info.isStrong());
        if (newVercode > vercode) {
            showDialogAndDownloadApk(context, info, oldApkName, newApkName, null);
        } else {
            if (isNoteVersion) {
                ToastUtil.showToast(context.getResources().getString(R.string.update_new));
            }
        }
    }

    /**
     * 显示更新下载提示框
     *
     * @param info       服务器下发的json数据（包含版本号，版本code，APK下载链接地址等）
     * @param oldApkName
     * @param newApkName
     * @param onClick    传null即可
     */
    public static void showDialogAndDownloadApk(final Activity context, final AutoUpdateInfo info, final String oldApkName, final String newApkName, OnClickListener onClick) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView mtvUpdate = new TextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mtvUpdate.setLayoutParams(lp);
        if (!TextUtils.isEmpty(info.getUpdate_log())) {
            mtvUpdate.setText(Html.fromHtml(info.getUpdate_log()));
        }
        BaseDialog.Build dialogBuild = new BaseDialog.Build(context).setTitle(context.getResources().getString(R.string.update_version_note)).setContentView(mtvUpdate)
                .setPositiveButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkLocalUpdate(context, info, oldApkName, newApkName);
                    }
                });
        if (!info.isStrong()) {
            dialogBuild.setNegativeButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
        }
        dialogBuild.show();

		/*Builder builder = new Builder(context);
        ViewInterface inflate = LayoutInflater.from(context).inflate(R.layout.umeng_update_dialog, null);
		Button cancel = (Button) inflate.findViewById(R.id.umeng_update_id_cancel);
		Button ok = (Button) inflate.findViewById(R.id.umeng_update_id_ok);
		TextView content = (TextView) inflate.findViewById(R.id.umeng_update_content);
		builder.setCancelable(false);
		builder.setView(inflate);
		builder.setInverseBackgroundForced(true);
		final AlertDialog dialog = builder.create();
		
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(ViewInterface v) {*/
        //apkFile 下载的最新APK的文件路径

	/*	String s = "最新版本:" + info.getNew_versionname() + "\n" +
                "最新内容:" + "\n";*/

        //	content.setText(s + sb.toString());
    }

    /**
     * 检查本地是否下载过apk
     *
     * @param context
     * @param info
     * @param oldApkName
     * @param newApkName
     */
    private static void checkLocalUpdate(final Activity context, final AutoUpdateInfo info, String oldApkName, final String newApkName) {
        final String apkFile = Environment.getExternalStorageDirectory() + "/download/" + oldApkName;
//		        DebugLog.e("sang", "未安装apk的文件路径 =》"+apkFile);
        //根据路径检查本地文件存不存在
        final boolean[] isAllow = {false, false};
        RxPermissions rxPermissions = new RxPermissions(context);
        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new CusumeObserver<Permission>() {
            @Override
            public void onNext(@NonNull Permission permission) {
                super.onNext(permission);
                if (permission.name.equals(Manifest.permission.READ_EXTERNAL_STORAGE) && permission.granted) {
                    isAllow[0] = true;
                }
                if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && permission.granted) {
                    isAllow[1] = true;
                }

                if (isAllow[0] && isAllow[1]) {
                    if (new File(apkFile).exists()) {
                        //对比之前获取未安装APK的包的信息（包名，版本，应用名等）
                        PackageManager pm = context.getPackageManager();
                        PackageInfo pkgInfo = pm.getPackageArchiveInfo(apkFile, PackageManager.GET_ACTIVITIES | PackageManager.SIGNATURE_MATCH);
                        String unInstallPackageName = null;
                        int unInstallPackageCode = 0;
                        if (pkgInfo != null) {
                            ApplicationInfo appInfo = pkgInfo.applicationInfo;
                            //必须加这两句，不然下面icon获取是default icon而不是应用包的icon
                            unInstallPackageName = appInfo.packageName; // 得到包名
                            unInstallPackageCode = pkgInfo.versionCode; // 得到版本code
                            DebugLog.e("sang", "未安装APK的包名：" + unInstallPackageName);
	        			/*下面代码备用
		        			String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名?
		        			String version = pkgInfo.versionName; // 得到版本信息?
		        			//icon1和icon2其实是一样的
		        			appInfo.sourceDir = apkFile;
		        			Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息?
		        			Drawable icon2 = appInfo.loadIcon(pm);
		        			String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName);
		        			DebugLog.e("sang", String.format("PkgInfo: %s", pkgInfoStr));
	        			*/
                        }
                        //包名也一致，则进行安装操作
                        if (unInstallPackageName.equals(AppStaus.mContext.getPackageName()) && unInstallPackageCode > AppStaus.APP_VERCODE) {
                            //先获取已安装apk的签名
                            byte[] signature = getSign(context);
                            String installPublickey = getPublicKey(signature);
//			        DebugLog.e("sang", "已安装的APK的签名key =》"+installPublickey);
                            //文件存在就获取未安装APK的签名
                            byte[] signature2 = getPackageArchiveInfo(apkFile, PackageManager.GET_ACTIVITIES | PackageManager.GET_SIGNATURES);
                            String unInstallPublickey = getPublicKey(signature2);
//		        	DebugLog.e("sang", "文件存在，获取未安装APK的签名key："+unInstallPublickey);
                            //对比签名
                            if (installPublickey.equals(unInstallPublickey)) {
                                //APK的签名一致，进行安装
//		        			DebugLog.e("sang", "签名一致, 可以进行安装");
                                inistallApk(context, new File(apkFile));
                            } else {
                                //APK的签名不一致，请下载完整最新版APK(调用统一的下载方法)
//		        			DebugLog.e("sang", "APK的签名不一致，请下载完整最新版APK");
                                download(context, info, newApkName);
                            }
                        } else {
                            //包名不一致，请下载完整最新版APK，进行APK更新操作
                            download(context, info, newApkName);
                        }
                    } else {
                        //文件不存在，重新下载(调用统一的下载方法)
                        download(context, info, newApkName);
                    }
                    //dialog.dismiss();
                    if (!TextUtils.isEmpty(info.getUpdate_log())) {
                        String[] split = info.getUpdate_log().split(";");
                        StringBuffer sb = new StringBuffer();
                        if (split != null && split.length > 0) {
                            for (String string : split) {
                                sb.append(string + "\n");
                            }
                        }
                    }
                }
            }
        });


    }
	/*	});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(ViewInterface v) {
				dialog.dismiss();
			}
		});*/

    //	dialog.show();
    //}

    /**
     * 执行apk后台下载,下载结果为byte数组
     */
    private static void download(final Activity context, final AutoUpdateInfo info, final String newApkName) {
        if (TextUtils.isEmpty(StringUtil.checkUrl(info.getApk_url()))) {
         /*   DebugLog.i("wang","===启动coogles ");
            Intent intent=new Intent();
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setAction(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.android.vending", "com.google.and侔roid.finsky.activities.LaunchUrlHandlerActivity"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(context!=null){
                context.startActivity(intent);
            }
            return;*/
            final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
            return;
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            final String sdPath = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(sdPath);
            if (!file.exists()) {
                file.mkdir();
            }
            Toast.makeText(context, context.getString(R.string.back_downding), Toast.LENGTH_SHORT).show();
            OkHttpClient okHttclient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    return response.newBuilder().body(new ProgeressResponseBody(response.body(), new ProgressListener() {
                        @Override
                        public void onStart() {
                            DebugLog.i("wang", "下载开始");
                        }

                        @Override
                        public void onProgess(long progress) {
                            DebugLog.i("wang", "下载进度:" + progress);
                        }

                        @Override
                        public void onFinish() {
                            DebugLog.i("wang", "下载完成");
                        }

                        @Override
                        public void onFail() {
                            DebugLog.i("wang", "下载失败");
                        }
                    })).build();
                }
            }).build();
            Request request = new Request.Builder().url(info.getApk_url()).build();
            okhttp3.Call call = okHttclient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {

                    if (response != null) {
                        downLoadApk(context, sdPath, response.body().bytes(), newApkName);
                    }

                }
            });
          /*  okHttclient.newCall(okHttclient);
            Retrofit retrofit = new Retrofit.Builder().client(okHttclient).baseUrl(info.getApk_url())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
            retrofit.create(UserApi.class).getApk().subscribeOn(Schedulers.io()).map(new Function<ResponseBody, byte[]>() {
                @Override
                public byte[] apply(@NonNull ResponseBody responseBody) throws Exception {
                    return responseBody.bytes();
                }
            }).subscribe(new Consumer<byte[]>() {
                @Override
                public void accept(byte[] bytes) throws Exception {
                    downLoadApk(context, sdPath, bytes, newApkName);
                }
            });*/
/*			HttpUtil.get(info.getApk_url(), null, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					downLoadApk(context,sdPath,arg2,newApkName);
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					Toast.makeText(context, "下载失败,请检查网络状态", Toast.LENGTH_SHORT).show();
				}
			});*/
        }
    }

    /**
     * APK byte数据转换为.APK可安装文件
     *
     * @param sdPath     APK下载的文件夹路径,不包含APK文件名
     * @param arg2       APK byte数组数据
     * @param newApkName 新下载的APK文件名
     */
    private static void downLoadApk(final Activity context, final String sdPath, final byte[] arg2, final String newApkName) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RxPermissions rxPermissions = new RxPermissions(context);
                rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE).observeOn(Schedulers.io()).subscribe(new CusumeObserver<Permission>() {
                    @Override
                    public void onNext(@NonNull Permission permission) {
                        super.onNext(permission);
                        if (permission.granted) {
                            final File apkFile = new File(sdPath, newApkName);
                            ByteArrayInputStream bis = null;
                            FileOutputStream fos = null;
                            try {
                                bis = new ByteArrayInputStream(arg2);
                                fos = new FileOutputStream(apkFile);
                                byte[] buff = new byte[1024];
                                int count = 0;
                                while ((count = bis.read(buff)) != -1) {
                                    fos.write(buff, 0, count);
                                    fos.flush();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (bis != null) {
                                        bis.close();
                                    }
                                    if (fos != null) {
                                        fos.close();
                                    }
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //存储本次下载APK的文件名
                                    SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHAREPAREN_XML_NAME, Activity.MODE_PRIVATE);
                                    sharedPreferences.edit().putString(AppStaus.APP_VERSIONNAME, newApkName).commit();
                                    inistallApk(context, apkFile);
                                }
                            });
                        }
                    }
                });

            }
        });


    }

    /**
     * 安装APK文件
     *
     * @param context
     * @param apkFile APK全路径,包含APK文件名
     */
    private static void inistallApk(Context context, File apkFile) {
        DebugLog.i("wang", "==path==" + apkFile.getAbsolutePath() + "==size==" + apkFile.length());
        Intent intent = new Intent(Intent.ACTION_VIEW);
   /*     intent.setDataAndType(Uri.parse("file://" + apkFile),
                "application/vnd.android.package-archive");*/

//判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, /*"application/vnd.android.package-archive"*/context.getContentResolver().getType(contentUri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private static byte[] getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            //按包名读取签名
            if (packageName.equals("com.panshi.hujin2.iloan")) {
                return info.signatures[0].toByteArray();
            }
        }
        return null;
    }

    public static String getPublicKey(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
            return cert.getPublicKey().toString();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static byte[] getPackageArchiveInfo(String apkFile, int flags) {

        //这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
        Object pkgParserPkg = null;
        Class[] typeArgs = null;
        Object[] valueArgs = null;
        try {
            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            Constructor<?> packageParserConstructor = null;
            Object packageParser = null;
            //由于SDK版本问题，这里需做适配，来生成不同的构造函数
            if (Build.VERSION.SDK_INT > 20) {
                //无参数 constructor
                packageParserConstructor = packageParserClass.getDeclaredConstructor();
                packageParser = packageParserConstructor.newInstance();
                packageParserConstructor.setAccessible(true);//允许访问
//				DebugLog.e("sang", "高版本SDK = " + Build.VERSION.SDK_INT);
//				DebugLog.e("sang", "无参数 constructor: " + packageParserConstructor.toGenericString());

                typeArgs = new Class[2];
                typeArgs[0] = File.class;
                typeArgs[1] = int.class;
                Method pkgParser_parsePackageMtd = packageParserClass.getDeclaredMethod("parsePackage", typeArgs);
                pkgParser_parsePackageMtd.setAccessible(true);

                valueArgs = new Object[2];
                valueArgs[0] = new File(apkFile);
                valueArgs[1] = PackageManager.GET_SIGNATURES;
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(packageParser, valueArgs);
            } else {
                //低版本有参数 constructor
                packageParserConstructor = packageParserClass.getDeclaredConstructor(String.class);
                Object[] fileArgs = {apkFile};
                packageParser = packageParserConstructor.newInstance(fileArgs);
                packageParserConstructor.setAccessible(true);//允许访问
//				DebugLog.e("sang", "低版本SDK = " + Build.VERSION.SDK_INT);
//				DebugLog.e("sang", "低版本有参数 constructor: " + packageParserConstructor.toGenericString());

                typeArgs = new Class[4];
                typeArgs[0] = File.class;
                typeArgs[1] = String.class;
                typeArgs[2] = DisplayMetrics.class;
                typeArgs[3] = int.class;

                Method pkgParser_parsePackageMtd = packageParserClass.getDeclaredMethod("parsePackage", typeArgs);
                pkgParser_parsePackageMtd.setAccessible(true);

                valueArgs = new Object[4];
                valueArgs[0] = new File(apkFile);
                valueArgs[1] = apkFile;
                valueArgs[2] = metrics;
                valueArgs[3] = PackageManager.GET_SIGNATURES;
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(packageParser, valueArgs);
            }

            typeArgs = new Class[2];
            typeArgs[0] = pkgParserPkg.getClass();
            typeArgs[1] = int.class;
            Method pkgParser_collectCertificatesMtd = packageParserClass.getDeclaredMethod("collectCertificates", typeArgs);
            valueArgs = new Object[2];
            valueArgs[0] = pkgParserPkg;
            valueArgs[1] = PackageManager.GET_SIGNATURES;
            pkgParser_collectCertificatesMtd.invoke(packageParser, valueArgs);
            // 应用程序信息包, 这个公开的, 不过有些函数变量没公开
            Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");
            Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
//            DebugLog.e("sang", "未安装的APK的签名 ："+info[0].toCharsString());
            return info[0].toByteArray();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
