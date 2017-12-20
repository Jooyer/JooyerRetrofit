package com.jooyer.jooyerretrofits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Retrofit 使用
 * 官网 : http://square.github.io/retrofit/
 *
 * OkHttp 使用
 * 官网 : https://github.com/square/okhttp
 *
 * Retrofit支持异步和同步，用call.enqueue(new Callback<LoginResult>）来采用异步请求，如果 调用call.execute() 则采用同步方式
 *
 *  http://www.jianshu.com/p/16994e49e2f6
 *      --> 包含添加证书的方法等...
 *  http://blog.csdn.net/column/details/13297.html
 */


/**
 *
 *  Retrofit 基本使用方法
 *
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    /**
     *  一般使用方式
     */
    public void onNormal(View view) {
        startActivity(new Intent(MainActivity.this,NormalActivity.class));
    }

    /**
     *  下载文件
     */
    public void onDown(View view) {
        startActivity(new Intent(MainActivity.this,DownActivity.class));
    }

    /**
     *  上传文件
     */
    public void onUpload(View view) {
        startActivity(new Intent(MainActivity.this,UploadActivity.class));
    }
}
