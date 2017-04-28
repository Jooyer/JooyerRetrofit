package com.jooyer.jooyerretrofit;

import android.content.Context;

import com.jooyer.jooyerretrofit.exception.ExceptionFactory;
import com.jooyer.jooyerretrofit.exception.RetryWhenNetWorkException;
import com.jooyer.jooyerretrofit.http.TokenInterceptor;
import com.jooyer.jooyerretrofit.listener.OnHttpCallBackListener;
import com.jooyer.jooyerretrofit.rxlife.ActivityLifeCycleEvent;
import com.jooyer.jooyerretrofit.subscribers.ProgressSubscriber;

import java.lang.ref.SoftReference;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Http 交互处理类
 * Created by Jooyer on 2017/2/14
 */
public class HttpManager {
    private SoftReference<OnHttpCallBackListener> mCallBackListener;
    private SoftReference<Context> mContext;
    private Retrofit mWithTokenRetrofit;
    private Retrofit mWithOutTokenRetrofit;
    private boolean isHasToken;


    /**
     * 默认是没有 Token 的请求
     */
    public HttpManager(OnHttpCallBackListener callBackListener, Context context) {
        mCallBackListener = new SoftReference<OnHttpCallBackListener>(callBackListener);
        mContext = new SoftReference<Context>(context);
        initRetrofit();
    }

    /**
     * 带 Token
     */
    public HttpManager(OnHttpCallBackListener callBackListener, Context context, boolean hanToken) {
        mCallBackListener = new SoftReference<OnHttpCallBackListener>(callBackListener);
        mContext = new SoftReference<Context>(context);
        isHasToken = hanToken;
        initRetrofit();
    }


    /**
     * 自动登录过期标示
     */
    private void initRetrofit() {

        if (isHasToken) {
            RxRetrofit.getInstance().getOkHttpClientBuilder().addInterceptor(new TokenInterceptor());
            if (null == mWithTokenRetrofit) {
                mWithTokenRetrofit = new Retrofit.Builder()
                        .client(RxRetrofit.getInstance().getOkHttpClientBuilder().build())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .baseUrl(RxRetrofit.getInstance().getBaseUrl())
                        .build();
            }
            isHasToken = false;
        } else {
            if (null == mWithOutTokenRetrofit) {
                mWithOutTokenRetrofit = new Retrofit.Builder()
                        .client(RxRetrofit.getInstance().getOkHttpClientBuilder().build())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .baseUrl(RxRetrofit.getInstance().getBaseUrl())
                        .build();
            }
        }

    }

    private  <T> T createApi(Class<T> service) {
       return isHasToken ? mWithTokenRetrofit.create(service) : mWithOutTokenRetrofit.create(service);
    }


    /**
     * 有 RxLife 的 Activity
     */
    public <T> void doHttpWithRxActivity(Class<T> service,BaseApi api) {
        ProgressSubscriber subscriber = new ProgressSubscriber(api, mContext, mCallBackListener);
        Observable<String> observable = api.getObservable(createApi(service))
                .retryWhen(new RetryWhenNetWorkException())
                .onErrorResumeNext(funcException)
                .compose(api.getRxAppCompatActivity().bindUntilEvent(ActivityLifeCycleEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

        observable.subscribe(subscriber);
    }

    /**
     * 没有 RxLife 的 Activity
     */
    public <T> void doHttpWithOutRxActivity(Class<T> service,BaseApi api) {
        ProgressSubscriber subscriber = new ProgressSubscriber(api, mContext, mCallBackListener);
        Observable observable = api.getObservable(createApi(service))
                .retryWhen(new RetryWhenNetWorkException())
                .onErrorResumeNext(funcException)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

        observable.subscribe(subscriber);
    }

    /**
     * 有 RxLife 的 Fragment
     */
    public <T> void doHttpWithRxFragment(BaseApi api, Class<T> service) {
        ProgressSubscriber subscriber = new ProgressSubscriber(api, mContext, mCallBackListener);
        Observable observable = api.getObservable(createApi(service))
                .retryWhen(new RetryWhenNetWorkException())
                .onErrorResumeNext(funcException)
                .compose(api.getRxAppCompatActivity().bindUntilEvent(ActivityLifeCycleEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

        observable.subscribe(subscriber);
    }


    /**
     * 没有 RxLife 的 Fragment
     */
    public <T> void doHttpWithOutRxFragment(BaseApi api, Class<T> service) {
        ProgressSubscriber subscriber = new ProgressSubscriber(api, mContext, mCallBackListener);
        Observable observable = api.getObservable(createApi(service))
                .retryWhen(new RetryWhenNetWorkException())
                .onErrorResumeNext(funcException)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

        observable.subscribe(subscriber);
    }

    /**
     * 异常处理类
     */

    Func1 funcException = new Func1<Throwable, Observable>() {
        @Override
        public Observable call(Throwable throwable) {
            return Observable.error(ExceptionFactory.analysisException(throwable));
        }
    };


}
