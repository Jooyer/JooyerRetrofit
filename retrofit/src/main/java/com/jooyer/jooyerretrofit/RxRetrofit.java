package com.jooyer.jooyerretrofit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.jooyer.jooyerretrofit.http.CacheInterceptor;
import com.jooyer.jooyerretrofit.http.LoggingInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 初始化
 * --> 获取全局上下文对象
 * Created by Jooyer on 2017/2/14
 */
public class RxRetrofit {
    private static final String TAG = RxRetrofit.class.getSimpleName();
    private static final int DEFAULT_TIMEOUT = 30;
    private Context mContext;
    private String mBaseUrl;
    @SuppressLint("StaticFieldLeak")
    private static RxRetrofit mRxRetrofit;
    private OkHttpClient.Builder mBuilder;

    // 请求时的显示进度界面
    private ProgressDialog mProgressDialog;


    private RxRetrofit() {
    }

    public static RxRetrofit getInstance() {
        if (null == mRxRetrofit) {
            synchronized (RxRetrofit.class) {
                if (null == mRxRetrofit) {
                    mRxRetrofit = new RxRetrofit();
                }
            }
        }
        return mRxRetrofit;
    }

    /**
     * @param context                 --> 用来初始化缓存目录
     * @param baseUrl                 --> Retrofit 需要的基础 URL
     * @param isAddLoggingInterceptor --> 是否添加请求日志
     * @param isAddLoggingInterceptor --> 是否添加请求日志
     * @param cacheName               --> 缓存目录,默认是 " OkHttpCache "
     */
    public void init(final Context context, final String cacheName, final String baseUrl,
                     final boolean isAddLoggingInterceptor, final boolean isNeedOkHttpCache) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                mContext = context;
                mBaseUrl = baseUrl;
                initClient(isAddLoggingInterceptor, isNeedOkHttpCache, cacheName);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /**
     * 初始化 OKHttp
     *
     * @param cacheName               --> 缓存目录,默认是 " OkHttpCache "
     * @param isAddLoggingInterceptor 是否添加日志输入   true --> 添加
     * @param isNeedOkHttpCache       是否添加 OKHttp 缓存   true --> 添加
     */
    private void initClient(boolean isAddLoggingInterceptor, boolean isNeedOkHttpCache, String cacheName) {
        mBuilder = new OkHttpClient.Builder();
        //超时设置
        mBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //设置缓存
        if (isNeedOkHttpCache) {
            File OkHttpCache = new File(mContext.getCacheDir(), TextUtils.isEmpty(cacheName) ? "OkHttpCache" : cacheName);
            mBuilder.cache(new Cache(OkHttpCache, 50 * 1024 * 1024));
            mBuilder.addInterceptor(new CacheInterceptor());
        }

        // 打印日志,根据需要设置
        if (isAddLoggingInterceptor) {
            mBuilder.addInterceptor(new LoggingInterceptor());
        }

        // 设置自动重连
        mBuilder.retryOnConnectionFailure(true);
    }

    /**
     *  传入自己配置 OkHttpClient.Builder
     *  注意: 这里是全局的.不管在哪里更改,都是全局作用
     */
    public void setBuilder(OkHttpClient.Builder builder) {
        mBuilder = builder;
    }

    OkHttpClient.Builder getOkHttpClientBuilder() {
        return mBuilder;
    }

    public Context getContext() {
        if (null == mContext) {
            throw new NullPointerException("必须设置mContext,建议在Application里设置");
        }
        return mContext;
    }


    String getBaseUrl() {
        if (TextUtils.isEmpty(mBaseUrl)) {
            throw new NullPointerException("必须设置BaseUrl,建议在Application里设置");
        }
        return mBaseUrl;
    }

    public ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        mProgressDialog = progressDialog;
    }
}
