package com.panshi.hujin2.iloan.model;


import android.os.Build;

import com.panshi.hujin2.iloan.Constant;
import com.panshi.hujin2.iloan.api.UserApi;
import com.panshi.hujin2.iloan.application.AppStaus;
import com.panshi.hujin2.iloan.bean.Response;
import com.panshi.hujin2.iloan.bean.userDeviceDetailVO;
import com.panshi.hujin2.iloan.factory.RetrofitFactory;
import com.panshi.hujin2.iloan.util.DebugLog;
import com.panshi.hujin2.iloan.util.DeviceUtil;
import com.panshi.hujin2.iloan.util.GonsonUtil;
import com.panshi.hujin2.iloan.util.MediaTypeUtil;
import com.panshi.hujin2.iloan.view.widget.CusumeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

import static com.panshi.hujin2.iloan.util.DeviceUtil.getWIFIMAC;

public class DevicesModel implements BaseModel {
    @Override
    public void failture(int code, String message) {

    }

    public void sendDevice(final SendDeviceInfoInterface sendDeviceInfoInterface) {

        userDeviceDetailVO userDeviceDetailVOBean = new userDeviceDetailVO();
        userDeviceDetailVOBean.setBoard(Build.BOARD);
        userDeviceDetailVOBean.setBrand(Build.BRAND);
        userDeviceDetailVOBean.setBuildId(Build.ID);
        userDeviceDetailVOBean.setCpuAbi(Build.CPU_ABI);
        userDeviceDetailVOBean.setDevice(Build.DEVICE);
        userDeviceDetailVOBean.setDisplay(Build.DISPLAY);
        userDeviceDetailVOBean.setHost(Build.HOST);
        userDeviceDetailVOBean.setImei(DeviceUtil.getIMEI(AppStaus.mContext));
        userDeviceDetailVOBean.setManufacturer(Build.MANUFACTURER);
        userDeviceDetailVOBean.setModel(Build.MODEL);
        userDeviceDetailVOBean.setMwlanmac(getWIFIMAC());
        userDeviceDetailVOBean.setProduct(Build.PRODUCT);
        userDeviceDetailVOBean.setTags(Build.TAGS);
        userDeviceDetailVOBean.setType(Build.TYPE);
        userDeviceDetailVOBean.setUser(Build.USER);
//        "id": "string",
//        "modifyTime": "string",
//        "userId": "string"
//        "createTime": "string"
        String userDeviceDetailVO = GonsonUtil.buildGosn().toJson(userDeviceDetailVOBean);

        DebugLog.d("userDeviceDetailVO", userDeviceDetailVO);
        // builder.addFormDataPart("userDeviceDetailVO",userDeviceDetailVO);

        RetrofitFactory.
                createFactoty().
                create(UserApi.class).
                getDeviceDetail(MediaTypeUtil.createJsonMediaType(userDeviceDetailVO)).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new CusumeObserver<Response>() {
                    @Override
                    public void onNext(Response o) {
                        DebugLog.d("ddddeee", o.getCode() + "");
                        if (o.getCode() == Constant.SUCESS_CODE) {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    public interface SendDeviceInfoInterface {
        public void sendDeviceInfo(Response response);
    }
}
