package com.jooyer.jooyerretrofits;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jooyer.jooyerretrofit.HttpManager;
import com.jooyer.jooyerretrofit.exception.ApiException;
import com.jooyer.jooyerretrofit.listener.OnHttpCallBackListener;
import com.jooyer.jooyerretrofit.rxlife.RxAppCompatActivity;


/**
 * 使用封装的 Retrofit
 */

public class DemoActivity extends RxAppCompatActivity {

    private TextView tv_test;
    private HttpManager manager;
    private OnHttpCallBackListener mOnHttpCallBackListener = new OnHttpCallBackListener() {
        @Override
        public void onNext(String result, String method) {
            TestBean bean = JSONObject.parseObject(result, TestBean.class);
            tv_test.setText(bean.getResults().get(0).getDesc());
        }

        @Override
        public void onError(ApiException e) {
            // 注意:获取具体错误显示信息,使用 e.getDisplayMessage();
            Toast.makeText(DemoActivity.this, "错误信息: " + e.getDisplayMessage(), Toast.LENGTH_SHORT).show();
            Log.i("Demo", "============== : " + e.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        tv_test = (TextView) findViewById(R.id.tv_test);
        manager = new HttpManager(this, mOnHttpCallBackListener);
    }

    /**
     * 测试不使用 RxLife
     *  PS: 就是继承自 Rx...,如果不调用 HttpManager.doHttpWithRx...,则就是不使用 Rx..
     */
    public void onRetrofitWithOutRxLife(View view) {

        // 这里直接实现BaseApi,则由于泛型的关系,需要强转
//        manager.doHttpWithOutRxActivity(HttpService.class, new BaseApi() {
//            @Override
//            public Observable<String> getObservable(Object service) {
//                return ((HttpService) service).getAndroidList();
//            }
//        });

        // 相比之下,在 BaseApi 后面加入泛型,更方便使用
//        manager.doHttpWithOutRxActivity(HttpService.class,new BaseApi<HttpService>(){
//            @Override
//            public Observable<String> getObservable(HttpService service) {
//                return service.getAndroidList();
//            }
//        });

        // 使用实现了 BaseApi 的 TestApi
        // 测试不使用 RxLife管理其下载
        manager.doHttpWithOutRxActivity(new TestApi(), HttpService.class);

    }


    /**
     * 使用 RxLife管理其下载
     */
    public void onRetrofitWithRxLife(View view) {
        TestApi api = new TestApi();
        // 使用 RxLife 管理下载生命周期必须设置
        api.setRxAppCompatActivity(DemoActivity.this);
        // 默认有网时缓存时间为60s
        api.setCacheTimeInConnect(10);

        // 设置是否需要缓存
        api.setCache(true);
        // 重要事情说三遍: 如果设置了需要缓存,则必须设置下面方法,
        // 重要事情说三遍: 如果设置了需要缓存,则必须设置下面方法,
        // 重要事情说三遍: 如果设置了需要缓存,则必须设置下面方法,
        // 推荐写法 ,将 HttpService 中的剩余部分链接放在这里,也不会造成缓存的 KEY 发生重复冲突
        api.setMethod("data/Android/2/1");

        manager.doHttpWithRxActivity(api, HttpService.class);
    }

}
