package com.jooyer.jooyerretrofit.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jooyer on 2017/1/19
 */

public class AgentInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request()
                .newBuilder()
                .addHeader("User-Agent", "Android")
                .build();
        return chain.proceed(newRequest);
    }
}
