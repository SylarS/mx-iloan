package com.panshi.hujin2.iloan.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadSampleListener
import com.liulishuo.filedownloader.FileDownloader
import com.panshi.hujin2.iloan.BuildConfig
import com.panshi.hujin2.iloan.R
import com.panshi.hujin2.iloan.application.AppStaus
import com.panshi.hujin2.iloan.bean.AutoUpdateInfo
import com.panshi.hujin2.iloan.view.widget.ProgressBeautifulView
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

object UpdateKotlin {

    //data: VersionBean.DataBean
    @JvmStatic
    fun popUpUpdate(data: AutoUpdateInfo, context: Activity) {
        var popup: View = LayoutInflater.from(context).inflate(R.layout.layout_popup_update, null)

        var tv_popup_tittle: TextView = popup.findViewById(R.id.tv_popup_tittle)
        var tv_popup_content: TextView = popup.findViewById(R.id.tv_popup_content)
        var tv_left: TextView = popup.findViewById(R.id.tv_left)
        var tv_right: TextView = popup.findViewById(R.id.tv_right)
        var progressBeautifulView: ProgressBeautifulView = popup.findViewById(R.id.pv_one)

        tv_popup_content.text = data.update_log
        (context.window.decorView as FrameLayout).addView(popup)
        tv_popup_tittle.text = context.resources.getText(R.string.new_version).toString() + data.new_versionname
        if (data.isStrong) {
            tv_left.setOnClickListener {
                context.finish()
            }
            tv_right.setOnClickListener {
                tv_left.visibility = View.GONE
                tv_right.visibility = View.GONE
                progressBeautifulView.visibility = View.VISIBLE
                checkPermission(data, progressBeautifulView, context)
            }
            popup.setOnClickListener {

            }
        } else {
            tv_left.setOnClickListener {
                (context.window.decorView as FrameLayout).removeView(popup)
            }
            tv_right.setOnClickListener {
                checkPermission(data, progressBeautifulView, context)
                (context.window.decorView as FrameLayout).removeView(popup)
            }
            popup.setOnClickListener {
                (context.window.decorView as FrameLayout).removeView(popup)
            }
        }
    }

    fun checkPermission(data: AutoUpdateInfo, progressBeautifulView: ProgressBeautifulView, context: Activity) {
        val rxPermissions = RxPermissions(context)
        val perD = rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { permission ->
                    if (permission.granted) {
                        startDownloadTask(data.apk_url!!, data.new_version!!, progressBeautifulView, context)
                    } else {
                        if (permission.shouldShowRequestPermissionRationale) {
                            if (data.isStrong) {
                                checkPermission(data, progressBeautifulView, context)
                            }
                        } else {
                            if (data.isStrong) {
                                context.finish()
                            }
                        }
                    }
                }
    }

    fun startDownloadTask(url: String, newVersion: String, progressBeautifulView: ProgressBeautifulView, context: Activity) {
        FileDownloader.setup(context)
        var address = context.externalCacheDir.absolutePath + "/" + newVersion + ".apk"
        var dis: Disposable = Flowable
                .create(FlowableOnSubscribe<Int> { emitter ->
                    FileDownloader
                            .getImpl()
                            .create(url)
                            .setPath(address)
                            .setListener(object : FileDownloadSampleListener() {
                                override fun error(baseDownloadTask: BaseDownloadTask?, th: Throwable) {
                                    emitter.onError(th)
                                }

                                override fun completed(baseDownloadTask: BaseDownloadTask?) {
                                    emitter.onComplete()
                                }

                                override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                                    val percent = soFarBytes.toFloat() / totalBytes.toFloat() * 100
                                    emitter.onNext(percent.toInt())
                                    super.progress(task, soFarBytes, totalBytes)
                                }
                            }).start()
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it > 10) {
                        progressBeautifulView.nowPercent = it / 100f
                        progressBeautifulView.nowNumber = it.toInt().toString() + "%"
                        progressBeautifulView.invalidate()
                    }
                }, {

                }, {
                    progressBeautifulView.nowPercent = 100 / 100f
                    progressBeautifulView.nowNumber = "100%"
                    progressBeautifulView.invalidate()
                    installApk(address, context)
                })
    }

    fun installApk(address: String, context: Activity) {
        var install = Intent(Intent.ACTION_VIEW)
        install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        var apkFile = File(address)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            var contentUri: Uri = FileProvider.getUriForFile(AppStaus.mContext, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile)
            install.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }
        context.startActivity(install)
    }

}