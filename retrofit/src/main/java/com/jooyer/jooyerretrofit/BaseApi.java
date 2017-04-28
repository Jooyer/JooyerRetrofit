package com.jooyer.jooyerretrofit;

import android.text.TextUtils;

import com.jooyer.jooyerretrofit.rxlife.RxAppCompatActivity;
import com.jooyer.jooyerretrofit.rxlife.RxFragment;

import java.lang.ref.SoftReference;

import rx.Observable;

/**
 * 请求数据和参数的封装类
 * <p>
 * Created by Jooyer on 2017/2/14
 */
public abstract class BaseApi<T> {

    /**
     * 是否可以取消 ProgressBar
     */
    private boolean isCancel = false;

    /**
     * 是否显示 ProgressBar
     */
    private boolean isShowProgress = true;

    /**
     * 是否缓存数据
     */
    private boolean isCache = false;

    /**
     * 基础URL ,如果使用缓存,则需要这个值
     */
    private String mBaseUrl;

    /**
     * 方法(eg:     @GET("data/Android/2/1")
        Observable<String> getAndroidList();
     ,  我们的 mMethod 为 data/Android/2/1 )
     * 这个参数是缓存必须的,如果不要缓存则可以不管
     */
    private String mMethod;

    /**
     * 超时时间 默认6秒
     */
    private int mTimeOut = 6;

    /**
     * 有网本地缓存时间 默认60秒
     */
    private int cacheTimeInConnect = 60;

    /**
     * 无网本地缓存时间 默认7天
     */
    private int cacheTimeOutConnect = 7 * 24 * 60 * 60;

    /**
     * 当一个 BaseApi 对应有多个请求时,使用此参数加以区别
     */
    private int mDistinction;

    /**
     *  用来区别对待不同请求方法,具体看 TestApi
     */
    public int mApiFlag = 0;


    //rx生命周期管理
    private SoftReference<RxAppCompatActivity> rxAppCompatActivity;
    private SoftReference<RxFragment> fragment;

    public boolean isCancel() {
        return isCancel;
    }

    public BaseApi setCancel(boolean cancel) {
        isCancel = cancel;
        return this;
    }

    public boolean isShowProgress() {
        return isShowProgress;
    }

    public BaseApi setShowProgress(boolean showProgress) {
        isShowProgress = showProgress;
        return this;
    }

    public boolean isCache() {
        return isCache;
    }

    public BaseApi setCache(boolean cache) {
        isCache = cache;
        return this;
    }

//    public String getBaseUrl() {
//        return mBaseUrl;
//    }
//
//    public BaseApi setBaseUrl(String baseUrl) {
//        mBaseUrl = baseUrl;
//        return this;
//    }

    public String getMethod() {
        return mMethod;
    }

    public BaseApi setMethod(String method) {
        mMethod = method;
        return this;
    }


    public String getUrl(){
        if (isCache()){
            if (TextUtils.isEmpty(mMethod)){
                throw new NullPointerException("如果需要缓存数据,则必须设置setMethod(String method)");
            }
        }
        return mBaseUrl + mMethod;
    }

    public int getTimeOut() {
        return mTimeOut;
    }

    public BaseApi setTimeOut(int timeOut) {
        mTimeOut = timeOut;
        return this;
    }

    public int getCacheTimeInConnect() {
        return cacheTimeInConnect;
    }

    public BaseApi setCacheTimeInConnect(int cacheTimeInConnect) {
        this.cacheTimeInConnect = cacheTimeInConnect;
        return this;
    }

    public int getCacheTimeOutConnect() {
        return cacheTimeOutConnect;
    }

    public BaseApi setCacheTimeOutConnect(int cacheTimeOutConnect) {
        this.cacheTimeOutConnect = cacheTimeOutConnect;
        return this;
    }

    public int getDistinction() {
        return mDistinction;
    }

    public BaseApi setDistinction(int distinction) {
        mDistinction = distinction;
        return this;
    }

    public BaseApi() {
        mBaseUrl = RxRetrofit.getInstance().getBaseUrl();
    }

    /*
    * 获取当前rx生命周期
    */
    public RxAppCompatActivity getRxAppCompatActivity() {
        return rxAppCompatActivity.get();
    }

    public BaseApi setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = new SoftReference<>(rxAppCompatActivity);
        return this;
    }

    public RxFragment getRxFragment() {
        return fragment.get();
    }

    public BaseApi setRxFragment(RxFragment fragment) {
        this.fragment = new SoftReference<>(fragment);
        return this;
    }

    public int getApiFlag() {
        return mApiFlag;
    }

    public BaseApi setApiFlag(int apiFlag) {
        mApiFlag = apiFlag;
        return this;
    }

    /**
     *
     * @param service --> 统一的请求接口
     * @return --> 具体的请求方法
     */
    public abstract  Observable<String> getObservable(T service);
}
