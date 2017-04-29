package com.jooyer.jooyerretrofit.subscribers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.jooyer.jooyerretrofit.BaseApi;
import com.jooyer.jooyerretrofit.CacheManager;
import com.jooyer.jooyerretrofit.RxRetrofit;
import com.jooyer.jooyerretrofit.exception.ApiException;
import com.jooyer.jooyerretrofit.exception.ExceptionCode;
import com.jooyer.jooyerretrofit.exception.HttpTimeException;
import com.jooyer.jooyerretrofit.http.CookieResult;
import com.jooyer.jooyerretrofit.listener.OnHttpCallBackListener;
import com.jooyer.jooyerretrofit.utils.CommUtil;

import java.lang.ref.SoftReference;

import rx.Observable;
import rx.Subscriber;

/**
 * 用于 Http 请求开始时,显示一个 progress
 * 当请求结束时, 关闭 progress
 * Created by Jooyer on 2017/2/14
 */
public class ProgressSubscriber<T> extends Subscriber<T> {

    private static final String TAG = "ProgressSubscriber";
    private SoftReference<OnHttpCallBackListener> mCallBackListener;

    private SoftReference<Context> mContext;

    private ProgressDialog mDialog;

    private BaseApi mApi;

    public ProgressSubscriber(BaseApi api, SoftReference<Context> context, SoftReference<OnHttpCallBackListener> callBackListener) {
        mApi = api;
        mContext = context;
        mCallBackListener = callBackListener;

        if (mApi.isShowProgress()) {
            initProgressDialog(mApi.isCancel());
        }

    }

    public ProgressDialog getDialog() {
        return mDialog;
    }

    public void setDialog(ProgressDialog dialog) {
        mDialog = dialog;
    }

    private void initProgressDialog(boolean isCancel) {
        Context context = mContext.get();
        if (null == mDialog && null != context) {
            if (null != RxRetrofit.getInstance().getProgressDialog()) {
                mDialog = RxRetrofit.getInstance().getProgressDialog();
            } else {
                mDialog = new ProgressDialog(context);
                mDialog.setMessage("正在加载...");
            }
            mDialog.setCancelable(isCancel);
        }

        if (null != mDialog && isCancel) { // 可以取消 dialog
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    onCancelProgress();
                }
            });
        }

    }

    /**
     * 取消 Progress 时,也取消了对 Observable 的订阅,同时也取消了 Http 请求
     */
    private void onCancelProgress() {
        if (!isUnsubscribed()) {
            unsubscribe();
        }
    }


    /**
     * 展示 加载框
     */
    private void showProgressDialog() {
        if (mApi.isShowProgress()) {
            Context context = mContext.get();
            if (null == context || null == mDialog)
                return;

            if (!mDialog.isShowing())
                mDialog.show();
        }
    }

    /**
     * 取消 加载框
     */
    private void dismissProgressDialog() {
        if (mApi.isShowProgress()) {
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    /**
     * 开始订阅
     */
    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog();
        Log.i(TAG, "==========onStart==0=== :  " + mApi.isCache());
        // 有网 并且有缓存
        if (mApi.isCache() && CommUtil.isNetWorkAvailable(RxRetrofit.getInstance().getContext())) {
            Log.i(TAG, "=========onStart==1===" + mApi.getUrl());
            CookieResult result = CacheManager.getInstance().queryEntry(mApi.getUrl());
            if (null != result) {
                long time = (System.currentTimeMillis() - result.getTime()) / 1000;
                Log.i(TAG, "============onStart==2===" + time + "=====" + mApi.getCacheTimeInConnect());
                if (time < mApi.getCacheTimeInConnect()) {
                    if (null != mCallBackListener.get()) {
                        mCallBackListener.get().onNext(result.getResult(), mApi.getMethod());
                    }
                    Log.i(TAG, "============onStart==3===");
                    onCompleted();
                    unsubscribe();

                }
            }
        }
        Log.i(TAG, "==========onStart==4===");
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "===onCompleted==1===");
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        Log.i(TAG, "=========onError==1=== : " + mApi.isCache());
        dismissProgressDialog();
        // 需要缓存并且本地有缓存则返回缓存数据
        if (mApi.isCache()) {
            Log.i(TAG, "=========onError==2===");
            getCache();
        } else {
            Log.i(TAG, "=========onError==3===");
            errorDo(e);
        }
    }

    private void getCache() {
        Observable.just(mApi.getUrl())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        errorDo(e);
                    }

                    @Override
                    public void onNext(String s) {
                        //获取缓存数据
                        CookieResult result = CacheManager.getInstance().queryEntry(s);
                        if (null == result) {
                            throw new HttpTimeException(HttpTimeException.NO_CACHE_ERROR);
                        }

                        long time = (System.currentTimeMillis() - result.getTime()) / 1000;
                        if (time < mApi.getCacheTimeInConnect()) {
                            if (null != mCallBackListener.get()) {
                                mCallBackListener.get().onNext(result.getResult(), mApi.getMethod());
                            }
                        } else {
                            CacheManager.getInstance().deleteEntry(result);
                            throw new HttpTimeException(HttpTimeException.CACHE_LOSE_EFFICACY_ERROR);
                        }

                    }
                });

    }

    @Override
    public void onNext(T t) {
        Log.i(TAG, "===onNext==1===");
        if (mApi.isCache()) {
            CookieResult result = CacheManager.getInstance().queryEntry(mApi.getUrl());
            long time = System.currentTimeMillis();
            if (null == result) {
                //保存
                result = new CookieResult(mApi.getUrl(), t.toString(), time);
                CacheManager.getInstance().savedEntry(result);
                Log.i(TAG, "===onNext==2===");
            } else {
                // 更新
                result.setResult(t.toString());
                result.setTime(time);
                CacheManager.getInstance().updateEntry(result);
                Log.i(TAG, "===onNext==3===");
            }
        }
        if (null != mCallBackListener.get()) {
            mCallBackListener.get().onNext(t.toString(), mApi.getMethod());
        }

    }

    private void errorDo(Throwable e) {
        Context context = mContext.get();
        if (null == context) return;

        OnHttpCallBackListener onHttpCallBackListener = mCallBackListener.get();
        if (null == onHttpCallBackListener) return;

        if (e instanceof ApiException) {
            onHttpCallBackListener.onError(((ApiException) e));
        } else if (e instanceof HttpTimeException) {
            HttpTimeException exception = (HttpTimeException) e;
            onHttpCallBackListener.onError(new ApiException(exception.getMessage(), exception, ExceptionCode.RUNTIME_ERROR_CODE));
        } else {
            onHttpCallBackListener.onError(new ApiException(e.getMessage(), e, ExceptionCode.UNKNOWN_ERROR_CODE));
        }
    }
}
