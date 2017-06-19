package com.jooyer.jooyerretrofit.exception;

import android.util.Log;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;


/**
 * 异常处理工厂
 * 主要是解析异常, 输出自定义 ApiException
 * <p>
 * Created by Jooyer on 2017/2/14
 */
public class ExceptionFactory {

    private static final String NETWORK_EXCEPTION_MSG = "网络异常";
    private static final String CONNECT_EXCEPTION_MSG = "连接异常";
    private static final String JSON_EXCEPTION_MSG = "FastJson解析异常";
    private static final String UNKNOWN_HOST_EXCEPTION_MSG = "无法解析该域名";


    public static ApiException analysisException(Throwable e) {
        ApiException apiException = new ApiException(e);
        if (e instanceof HttpTimeException) {
            Log.i("Exception", "==========HttpTimeException==== " + e.getMessage());
            // 自定义运行时异常
            HttpTimeException exception = (HttpTimeException) e;
            apiException.setErrorCode(ExceptionCode.RUNTIME_ERROR_CODE);
            apiException.setDisplayMessage(exception.getMessage());
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
            Log.i("Exception", "==========ConnectException//SocketTimeoutException==== " + e.getMessage());
            // 连接异常
            apiException.setErrorCode(ExceptionCode.HTTP_ERROR_CODE);
            apiException.setDisplayMessage(CONNECT_EXCEPTION_MSG);
        } else if (e instanceof JSONException || e instanceof ParseException) {
            Log.i("Exception", "==========JSONException//ParseException==== " + e.getMessage());
            //解析异常
            apiException.setErrorCode(ExceptionCode.JSON_ERROR_CODE);
            apiException.setDisplayMessage(JSON_EXCEPTION_MSG);
        } else if (e instanceof UnknownHostException) {
            Log.i("Exception", "==========UnknownHostException==== " + e.getMessage());
            //无法解析该域名
            apiException.setErrorCode(ExceptionCode.UNKNOWN_HOST_ERROR_CODE);
            apiException.setDisplayMessage(UNKNOWN_HOST_EXCEPTION_MSG);

        } else if (e instanceof HttpException) {
            Log.i("Exception", "==========HttpException==== " + e.getMessage());
            // 网络异常
            apiException.setErrorCode(ExceptionCode.NETWORK_ERROR_CODE);
            apiException.setDisplayMessage(NETWORK_EXCEPTION_MSG);
        } else {
            Log.i("Exception", "==========Other==== " + e.getMessage());
            // 未知异常
            apiException.setErrorCode(ExceptionCode.UNKNOWN_ERROR_CODE);
            apiException.setDisplayMessage(e.getMessage());
        }
        return apiException;
    }


}
