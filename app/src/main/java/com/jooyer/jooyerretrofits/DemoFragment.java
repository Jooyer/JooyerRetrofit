package com.jooyer.jooyerretrofits;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jooyer.jooyerretrofit.HttpManager;
import com.jooyer.jooyerretrofit.exception.ApiException;
import com.jooyer.jooyerretrofit.listener.OnHttpCallBackListener;
import com.jooyer.jooyerretrofit.rxlife.RxFragment;

/**
 * Created by Jooyer on 2017/4/29
 */

public class DemoFragment extends RxFragment {

    private TextView tv_test;
    private Button btn_test1, btn_test2;
    private HttpManager manager;
    private TestApi mApi;
    private OnHttpCallBackListener mOnHttpCallBackListener = new OnHttpCallBackListener() {
        @Override
        public void onNext(String result, String method) {
            TestBean bean = JSONObject.parseObject(result, TestBean.class);
            tv_test.setText(bean.getResults().size());
        }

        @Override
        public void onError(ApiException e) {
            // 注意:获取具体错误显示信息,使用 e.getDisplayMessage();
            Toast.makeText(getContext(), "错误信息: " + e.getDisplayMessage(), Toast.LENGTH_SHORT).show();
            Log.i("Demo", "============== : " + e.getMessage());
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_demo, container, false);
        tv_test = (TextView) view.findViewById(R.id.tv_test);
        btn_test1 = (Button) view.findViewById(R.id.btn_test1);
        btn_test2 = (Button) view.findViewById(R.id.btn_test2);

        manager = new HttpManager(getContext(), mOnHttpCallBackListener);
        mApi = new TestApi();

        /**
         *  不使用 RxLife, 首先Fragment不需要继承自RxFragment
         *  然后, 不用调用 BaseApi.setRxFragment()
         *  最后,调用 HttpManager 方法时 选择 withOut 方法
         *  PS: 就是继承自 Rx...,如果不调用 HttpManager.doHttpWithRx...,则就是不使用 Rx..
         */
        btn_test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 加入每次请求 2条数据
                mApi.setCount(2);

                // 注意: 上面使用了 setCount(),如果你不写 BaseApi.setApiFlag(),默认会调用 getAndroidList
                // 但是由于这里设置了,那相当于默认值就是 2 了,则其他地方需要换方法,就重新调用此方法设置相应的值就好了
                mApi.setApiFlag(2);

                // 没有 RxLife
                manager.doHttpWithOutRxFragment(mApi,HttpService.class);
            }
        });

        /**
         *  使用 RxLife, 首先 Fragment 不需要继承自 RxFragment
         *  然后, 需要调用 BaseApi.setRxFragment(Rx)
         *  最后,调用 HttpManager 方法时,选择 withRx...方法
         *  PS: 如果调用 withRx ,却没有设置 BaseApi.setRxFragment(Rx),则会报错
         */
        btn_test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApi.setRxFragment(DemoFragment.this);
                mApi.setCount(4);
                manager.doHttpWithRxFragment(mApi,HttpService.class);
            }
        });

        return view;
    }
}
