package com.jooyer.jooyerretrofit.exception;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;


/**
 * 重试条件
 * Created by Jooyer on 2017/2/14
 */
public class RetryWhenNetWorkException implements Function<Observable<? extends Throwable>, Observable<?>> {

    /**
     * 重试次数
     */
    private int mCount = 3;

    /**
     * 延迟时长
     */
    private long mDelay = 3000;

    /**
     * 每次重试间隔时长
     */
    private long mPerIncreaseDelay = 3000;

    public RetryWhenNetWorkException() {
    }

    public RetryWhenNetWorkException(int count, long delay) {
        mCount = count;
        mDelay = delay;
    }

    public RetryWhenNetWorkException(int count, long delay, long perIncreaseDelay) {
        mCount = count;
        mDelay = delay;
        mPerIncreaseDelay = perIncreaseDelay;
    }



    @Override
    public Observable<?> apply(@NonNull Observable<? extends Throwable> observable) throws Exception {
        return observable
                // rang --> 发射从1开始,一共发射 mCount + 1 次
                .zipWith(Observable.range(1, mCount + 1), new BiFunction<Throwable, Integer, Wrapper>() {
                    @Override
                    public Wrapper apply(@NonNull Throwable throwable, @NonNull Integer integer) throws Exception {
                        return new Wrapper(integer, throwable);
                    }
                }).flatMap(new Function<Wrapper, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Wrapper wrapper) throws Exception {
                        if ((wrapper.mThrowable instanceof ConnectException ||
                                wrapper.mThrowable instanceof SocketException ||
                                wrapper.mThrowable instanceof TimeoutException) &&
                                wrapper.mCount < mCount + 1) {

                            return Observable.timer(mDelay + (wrapper.mCount - 1) * mPerIncreaseDelay, TimeUnit.MILLISECONDS);
                        }
                        return Observable.error(wrapper.mThrowable);
                    }
                });
    }

    static class Wrapper {
        private int mCount;

        private Throwable mThrowable;

        public Wrapper(int count, Throwable throwable) {
            this.mCount = count;
            mThrowable = throwable;
        }
    }

}
