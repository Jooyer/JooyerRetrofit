package com.jooyer.jooyerretrofit.http;


import com.jooyer.jooyerretrofit.listener.OnUploadProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 自定义上传回调进度
 * Created by Jooyer on 2017/2/14
 */
public class ProgressBody extends RequestBody {

    /**
     * 实际待包装的请求体
     */
    private final RequestBody mRealRequestBody;

    private final OnUploadProgressListener mUploadProgressListener;

    /**
     * 包装完成的 BufferedSink
     */
    private BufferedSink mBufferedSink;

    public ProgressBody(RequestBody realRequestBody, OnUploadProgressListener uploadProgressListener) {
        mRealRequestBody = realRequestBody;
        mUploadProgressListener = uploadProgressListener;
    }

    /**
     *  重写 实际调用的响应体的 contentType
     */
    @Override
    public MediaType contentType() {
        return mRealRequestBody.contentType();
    }


    /**
     *  重写实际调用的响应体的 contentLength
     */
    @Override
    public long contentLength() throws IOException {
        return mRealRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (null == mBufferedSink){
            mBufferedSink = Okio.buffer(sink(sink));
        }
        mRealRequestBody.writeTo(mBufferedSink);
        // 必须调用flush,否则 最后一部分数据可能不会被写入
        mBufferedSink.flush();
    }

    /**
     * 通过此方法,回调上传的进度
     */
    private Sink sink(BufferedSink sink) {
        return new ForwardingSink(sink) {
            //当前写入的字节
            long curWriteBytesCount = 0L;
            //总字节长度
            long totalBytesCount = 0L;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                curWriteBytesCount += byteCount;

                // 获取总长度,后续不再调用
                if (0 == totalBytesCount){
                    totalBytesCount = contentLength();
                }

                Observable.just(curWriteBytesCount)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                if (null != mUploadProgressListener)
                                    mUploadProgressListener.onUploadProgress(curWriteBytesCount,totalBytesCount);
                            }
                        });
            }
        };
    }
}
