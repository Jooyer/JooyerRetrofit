package com.jooyer.jooyerretrofit;

import android.content.Context;

import com.jooyer.jooyerretrofit.exception.ExceptionFactory;
import com.jooyer.jooyerretrofit.exception.RetryWhenNetWorkException;
import com.jooyer.jooyerretrofit.listener.OnHttpCallBackListener;
import com.jooyer.jooyerretrofit.rxlife.ActivityLifeCycleEvent;
import com.jooyer.jooyerretrofit.rxlife.FragmentLifeCycleEvent;
import com.jooyer.jooyerretrofit.subscribers.ProgressSubscriber;

import java.lang.ref.SoftReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * http://blog.csdn.net/jdsjlzx/article/details/54845517
 * Http 交互处理类
 * Created by Jooyer on 2017/2/14
 */
public class HttpManager {
    private SoftReference<OnHttpCallBackListener> mCallBackListener;
    private SoftReference<Context> mContext;
    private Retrofit mRetrofit;


    /**
     * 默认是没有 Token 的请求
     */
    public HttpManager(Context context, OnHttpCallBackListener callBackListener) {
        initRetrofit();
        mCallBackListener = new SoftReference<OnHttpCallBackListener>(callBackListener);
        mContext = new SoftReference<Context>(context);
    }


    /**
     * 自动登录过期标示
     */
    private void initRetrofit() {
        if (null == mRetrofit) {
            synchronized (HttpManager.class){
                if (null == mRetrofit){
                    mRetrofit = new Retrofit.Builder()
                            .client(RxRetrofit.getInstance().getOkHttpClientBuilder().build())
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .baseUrl(RxRetrofit.getInstance().getBaseUrl())
                            .build();
                }
            }
        }
    }

    /**
     * 有 RxLife 的 Activity
     */
    @SuppressWarnings("unchecked")
    public <T> void doHttpWithRxActivity(BaseApi api, Class<T> service) {
        ProgressSubscriber subscriber = new ProgressSubscriber(api, mContext, mCallBackListener);
        Observable observable = api.getObservable(mRetrofit.create(service))
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
    @SuppressWarnings("unchecked")
    public <T> void doHttpWithOutRxActivity(BaseApi api, Class<T> service) {
        ProgressSubscriber subscriber = new ProgressSubscriber(api, mContext, mCallBackListener);
        Observable observable = api.getObservable(mRetrofit.create(service))
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
    @SuppressWarnings("unchecked")
    public <T> void doHttpWithRxFragment(BaseApi api, Class<T> service) {
        ProgressSubscriber subscriber = new ProgressSubscriber(api, mContext, mCallBackListener);
        Observable observable = api.getObservable(mRetrofit.create(service))
                .retryWhen(new RetryWhenNetWorkException())
                .onErrorResumeNext(funcException)
                .compose(api.getRxFragment().bindUntilEvent(FragmentLifeCycleEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

        observable.subscribe(subscriber);
    }


    /**
     * 没有 RxLife 的 Fragment
     */
    @SuppressWarnings("unchecked")
    public <T> void doHttpWithOutRxFragment(BaseApi api, Class<T> service) {
        ProgressSubscriber subscriber = new ProgressSubscriber(api, mContext, mCallBackListener);
        Observable observable = api.getObservable(mRetrofit.create(service))
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

    private Function funcException = new Function<Throwable, ObservableSource>() {
        @Override
        public ObservableSource apply(@NonNull Throwable throwable) throws Exception {
            return Observable.error(ExceptionFactory.analysisException(throwable));
        }
    };


}
