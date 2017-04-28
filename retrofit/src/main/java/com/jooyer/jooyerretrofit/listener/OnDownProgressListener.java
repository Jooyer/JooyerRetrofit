package com.jooyer.jooyerretrofit.listener;

/** 下载进度的回调
 * Created by Jooyer on 2017/2/15
 */
public interface OnDownProgressListener {

    /**
     * @param totalRead --> 已经读取(保存)的总字节数
     * @param total --> 文件总大小
     * @param done --> 是否读取完成
     */
    void onDownProgress(long totalRead, long total, boolean done);

}
