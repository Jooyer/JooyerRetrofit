package com.jooyer.jooyerretrofit.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/19
 */
public class LoggingInterceptor implements Interceptor {

    private String TAG = "logs";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.i(TAG,"=======1=======" +  String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.i(TAG,"=======2=======" +  String.format("Received response for %s in %.1fms%n%sconnection=%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers(), chain.connection()));
        return response;
    }

}
