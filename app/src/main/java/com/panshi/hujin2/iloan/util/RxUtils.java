package com.panshi.hujin2.iloan.util;

import io.reactivex.disposables.Disposable;

public class RxUtils {


    public static void cancelDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.isDisposed();
        }
    }
}
