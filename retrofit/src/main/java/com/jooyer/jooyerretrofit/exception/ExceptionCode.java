package com.jooyer.jooyerretrofit.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** 异常错误码
 * Created by Jooyer on 2017/2/14
 */
public class ExceptionCode {

    /**
     *  网络错误
     */
    public static final int NETWORK_ERROR_CODE = 0x1;

    /**
     *  HTTP 错误
     */
    public static final int HTTP_ERROR_CODE = 0x2;

    /**
     * FastJson 错误
     */
    public static final int JSON_ERROR_CODE = 0x3;

    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR_CODE = 0x4;

    /**
     * 运行时异常 -> 包含自定义错误
     */
    public static final int RUNTIME_ERROR_CODE = 0x5;

    /**
     * 无法解析该域名
     */
    public static final int UNKNOWN_HOST_ERROR_CODE = 0x6;

    @IntDef({NETWORK_ERROR_CODE,HTTP_ERROR_CODE,JSON_ERROR_CODE,UNKNOWN_ERROR_CODE,RUNTIME_ERROR_CODE,UNKNOWN_HOST_ERROR_CODE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptCode{

    }
}
