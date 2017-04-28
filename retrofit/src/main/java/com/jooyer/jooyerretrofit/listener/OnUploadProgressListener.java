package com.jooyer.jooyerretrofit.listener;

/** 上传进度回调
 * Created by Jooyer on 2017/2/14
 */
public interface OnUploadProgressListener {

    void onUploadProgress(long currentBytesCount, long totalBytesCount);

}
