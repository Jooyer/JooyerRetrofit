package com.jooyer.jooyerretrofit.listener;


import com.jooyer.jooyerretrofit.exception.ApiException;

/** 网络请求结果的回调
 * Created by Jooyer on 2017/2/14
 */
public interface OnHttpCallBackListener{

    void onNext(String result, String method);

    void onError(ApiException e);

}
