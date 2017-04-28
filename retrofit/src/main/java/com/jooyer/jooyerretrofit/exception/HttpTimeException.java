package com.jooyer.jooyerretrofit.exception;

/**
 * 运行时 自定义异常
 * 根据需要自己添加错误
 * <p>
 * Created by Jooyer on 2017/2/14
 */
public class HttpTimeException extends RuntimeException {

    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR = 0x1001;

    /**
     * 本地无缓存错误
     */
    public static final int NO_CACHE_ERROR = 0x1002;

    /**
     * 缓存过期错误
     */
    public static final int CACHE_LOSE_EFFICACY_ERROR = 0x1003;

    public HttpTimeException(int resultCode) {
        super(getApiExceptionMessage(resultCode));
    }

    public HttpTimeException(String detailMessage) {
        super(detailMessage);
    }

    private static String getApiExceptionMessage(int code) {
        switch (code) {
            case UNKNOWN_ERROR:
                return "错误: 网络错误";
            case NO_CACHE_ERROR:
                return "错误: 无缓存数据";
            case CACHE_LOSE_EFFICACY_ERROR:
                return "错误: 缓存数据过期";
            default:
                return "错误: 未知错误";

        }
    }


}
