package com.jooyer.jooyerretrofit.exception;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 *  重试条件
 * Created by Jooyer on 2017/2/14
 */
public class RetryWhenNetWorkException implements Func1<Observable<? extends Throwable>,Observable<?>> {

    /**
     *  重试次数
     */
    private int mCount = 3;

    /**
     *  延迟时长
     */
    private long mDelay = 3000;

    /**
     *  每次重试间隔时长
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
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable
                // rang --> 发射从1开始,一共发射 mCount + 1 次
                .zipWith(Observable.range(1, mCount + 1), new Func2<Throwable, Integer, Wrapper>() {
                    @Override
                    public Wrapper call(Throwable throwable, Integer integer) {
                        return new Wrapper(integer,throwable);
                    }
                }).flatMap(new Func1<Wrapper, Observable<?>>() {
                    @Override
                    public Observable<?> call(Wrapper wrapper) {
                        if ( (wrapper.mThrowable instanceof ConnectException ||
                                wrapper.mThrowable instanceof SocketTimeoutException ||
                                wrapper.mThrowable instanceof TimeoutException) &&
                                wrapper.mCount < mCount + 1
                                ){
                            return Observable.timer(mDelay + (wrapper.mCount - 1) * mPerIncreaseDelay, TimeUnit.MILLISECONDS);
                        }
                        return Observable.error(wrapper.mThrowable);
                    }
                });
    }

    static class Wrapper{
        private int mCount;

        private Throwable mThrowable;

        public Wrapper(int count, Throwable throwable) {
            this.mCount = count;
            mThrowable = throwable;
        }
    }

}
