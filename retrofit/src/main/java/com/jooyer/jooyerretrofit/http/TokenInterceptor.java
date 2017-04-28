package com.jooyer.jooyerretrofit.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * http://www.mamicode.com/info-detail-1375498.html
 * Token
 * Created by WZG on 2016/10/26.
 */

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request()
                .newBuilder()
                .addHeader("X-Access-Token", "token")//TODO 将服务器返回的token填入
                .build();

        Log.i("TokenInterceptor", "X-Access-Token: " + "token");
        return chain.proceed(newRequest);
    }

}
