package com.jooyer.jooyerretrofits;


import com.jooyer.jooyerretrofit.BaseApi;

/**
 * Created by Jooyer on 2017/4/18
 */

// 注意这里的泛型一定要写的,否则使用时还的强转,具体看DemoActivity
public class TestApi extends BaseApi<HttpService> {

    // 温馨提示: 如果你的请求有参数传递可以写在此处
    // 例如我们一次请求 5 条数据或者动态改变请求数量

    private int count = 0 ;

    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public io.reactivex.Observable<String> getObservable(HttpService service) {
        // 在 BaseApi 中有一个 mApiFlag 属性
        // 其主要作用是: 一个 TestApi 可以包含多个请求数据
        if (1 == mApiFlag){
            return service.getAndroidSingle();
        }else if (2 == mApiFlag){
            return service.getAndroidCountList(String.valueOf(count));
        }else { // mApiFlag 默认是 0,则可以使用这个默认值去做一个某个页面的默认请求
            return service.getAndroidList();
        }
    }
}
