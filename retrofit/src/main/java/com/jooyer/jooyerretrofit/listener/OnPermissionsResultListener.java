package com.jooyer.jooyerretrofit.listener;

/** 权限请求的回调
 * Created by Jooyer on 2016/12/12
 */
public interface OnPermissionsResultListener {
    void onPermissionGranted(); //同意

    void onPermissionDenied();
}
