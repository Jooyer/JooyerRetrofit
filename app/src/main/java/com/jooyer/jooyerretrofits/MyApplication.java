package com.jooyer.jooyerretrofits;

import android.app.Application;

import com.jooyer.jooyerretrofit.RxRetrofit;

/**
 * Created by Jooyer on 2017/4/28
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /**
         *  注意这个初始最好放在这里
         */
        // 提示: 如果设置了 OKHTTP 缓存,则无论你在请求的 api 中是否设置 setCache() 和 setMethod()
        // 都会正常去获取缓存.
        RxRetrofit.getInstance().init(this,"test",Constant.BASE_URL,true,false);
        // 设置网络请求时的界面展示,有默认效果
        RxRetrofit.getInstance().setProgressDialog(null);
    }
}
