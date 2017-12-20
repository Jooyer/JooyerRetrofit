package com.jooyer.jooyerretrofits;

import android.app.Application;

import com.jooyer.jooyerretrofit.retrofit.RxRetrofit;


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
        RxRetrofit.getInstance().init(this,"test",Constants.BASE_URL,true);

    }
}
