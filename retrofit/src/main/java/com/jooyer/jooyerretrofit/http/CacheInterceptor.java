package com.jooyer.jooyerretrofit.http;


import com.jooyer.jooyerretrofit.RxRetrofit;
import com.jooyer.jooyerretrofit.utils.CommUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * GET 缓存方式拦截器
 * <p>
 * Created by Jooyer on 2017/2/14
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!CommUtil.isNetWorkAvailable(RxRetrofit.getInstance().getContext())) {
            // 没有网络,强制从缓存读取
            // 否则,在断网情况下退出APP 或者 等待一分钟(有网缓存时间) 后,就获取不到缓存了
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);
        Response responseLatest = null;
        if (CommUtil.isNetWorkAvailable(RxRetrofit.getInstance().getContext())) {
            //有网,设置缓存失效时间 一分钟
            int maxAge = 60;
            responseLatest = response.newBuilder()
                    .removeHeader("Pragma") // TODO 没明白
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();

        } else {
            // 没网 ,设置失效时间 6小时
            int maxStale = 6 * 60 * 60;
            responseLatest = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control","public,only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return responseLatest;
    }
}
