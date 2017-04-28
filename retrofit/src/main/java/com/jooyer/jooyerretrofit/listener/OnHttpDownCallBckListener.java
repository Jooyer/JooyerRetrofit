package com.jooyer.jooyerretrofit.listener;

/** 下载过程的回调
 * Created by Jooyer on 2017/2/14
 */
public abstract class OnHttpDownCallBckListener<T> {

    public abstract void onNext(T t);

    public abstract void onStart();

    public abstract void onComplete();

    /**
     *  更新下载进入
     * @param downLength --> 已经下载长度
     * @param totalLength --> 文件总长度
     */
    public abstract void onUpdateProgress(long downLength, long totalLength);

    /**
     *  下载失败/方法错误等
     *     主动调用,更加灵活
     * @param e
     */
    public void onError(Throwable e){

    }

    /**
     *  暂停
     */
    public void onPause(){

    }

    /**
     * 停止下载,销毁已下载内容
     */
    public void onStop(){

    }

}
