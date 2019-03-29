package com.panshi.hujin2.iloan.view.widget;

import com.panshi.hujin2.iloan.listener.ProgressListener;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class ProgeressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private ProgressListener listener;
    private BufferedSource bufferedSource;

    public ProgeressResponseBody(ResponseBody responseBody, ProgressListener listener) {
        this.listener = listener;
        this.responseBody=responseBody;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {
            long totalByte = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long nbyte = super.read(sink, byteCount);
                if (totalByte == 0) {
                    listener.onStart();
                }
                if (nbyte != -1) {
                    totalByte += nbyte;
                    listener.onProgess(totalByte * 100 / responseBody.contentLength());
                } else {
                    if (totalByte * 100 / responseBody.contentLength() == 100) {
                        listener.onFinish();
                    } else {
                        listener.onFail();
                    }

                }
                return nbyte;

            }
        };
    }
}
