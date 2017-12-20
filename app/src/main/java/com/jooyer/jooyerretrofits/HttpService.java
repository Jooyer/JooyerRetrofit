package com.jooyer.jooyerretrofits;


import com.jooyer.jooyerretrofit.retrofit.fileconverter.FileConverter;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 使用 Retrofit 必须的:
 * 一个统一的接口
 * <p>
 * Created by Jooyer on 2017/2/13
 */
public interface HttpService {

    // 使用自定义 FileConverter ,注意返回的是 File
    @GET
    Observable<File> downLoad(@Url String url, @Header(FileConverter.SAVE_PATH) String path);


    /**
     * 断点续传下载
     */
    @GET
    Observable<ResponseBody> downloadTest(@Header("range") String start, @Url String url);


    /**
     * 下面 仅仅说明需要使用到 @Streaming 这个注解,对于具体接口方法,需要根据项目而来
     */
    /*断点续传下载接口
    * 在方法上加上@Streaming注解,加上这个注解以后就不会讲文件内容加载到内存中,
    * 而是在通过ResponseBody 去读取文件的时候才从网络文件去下载文件,且必须异步下载
    * */
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);

    /**
     * 以二进制流的形式上传
     * 如果有参数使用 @QueryMap Map<String, Object> param
     *
     */
    @Multipart
    @POST("upload")
    Observable<String> upload(//   @QueryMap Map<String, Object> param,
                              @Part("image\"; filename=\"test.jpg") RequestBody body // 单个文件
//                               @Part() MultipartBody.Part[] parts 上传多个文件
    );

    /**
     * 直接丢单个文件上传
     */
    @Multipart
    @POST("upload")
    Observable<String> uploadII(@Part MultipartBody.Part file);


    /**
     * 获取一个集合
     */
    @GET("data/Android/2/10")
    Observable<String> getAndroidList();

}
