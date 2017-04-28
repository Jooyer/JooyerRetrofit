package com.jooyer.jooyerretrofit.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络判断
 * 获取BaseUrl
 * 下载内容写入文件
 * Created by Jooyer on 2017/2/14
 */
public class CommUtil {

    /**
     * 判断网络是否正常连接
     *
     * @return true --> 正常连接
     */
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != manager) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 读取 BaseUrl
     */
    public static String getBaseUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }

        index = url.lastIndexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }




}
