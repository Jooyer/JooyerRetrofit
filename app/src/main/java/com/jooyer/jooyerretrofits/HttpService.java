package com.jooyer.jooyerretrofits;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 *  使用 Retrofit 必须的:
 *   一个统一的接口
 *
 * Created by Jooyer on 2017/2/13
 */
public interface HttpService {

    /**
     *  获取一条信息
     */
    @GET("data/Android/2/1")
    Observable<String> getAndroidSingle();

    /**
     *  获取一个集合
     */
    @GET("data/Android/2/10")
    Observable<String> getAndroidList();

    /**
     *  获取一个集合
     */
    @GET("data/Android/2/")
    Observable<String> getAndroidCountList(@Query("count")String count);

    /**
     *  下载
     */
    @GET
    Observable<ResponseBody> downloadTest(@Header("range") String start, @Url String url);


    /**
     * 下面 仅仅说明需要使用到 @Streaming 这个注解,对于具体接口方法,需要根据项目而来
     */
    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);


}
