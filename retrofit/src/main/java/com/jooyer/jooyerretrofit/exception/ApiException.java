package com.jooyer.jooyerretrofit.exception;

/** 回调统一请求异常
 * Created by Jooyer on 2017/2/14
 */
public class ApiException extends Exception {

    /**
     * 错误码
     */
    private int mErrorCode;

    /**
     *  错误提示信息
     */
    private String mDisplayMessage;

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ApiException(String displayMessage, Throwable throwable, int errorCode) {
        super(displayMessage, throwable);
        mErrorCode = errorCode;
        mDisplayMessage = displayMessage;
    }


    @ExceptionCode.ExceptCode
    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(@ExceptionCode.ExceptCode int errorCode) {
        mErrorCode = errorCode;
    }

    public String getDisplayMessage() {
        return mDisplayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        mDisplayMessage = displayMessage;
    }
}
