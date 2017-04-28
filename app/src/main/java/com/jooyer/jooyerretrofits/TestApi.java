package com.jooyer.jooyerretrofits;


import com.jooyer.jooyerretrofit.BaseApi;

import rx.Observable;

/**
 * Created by Jooyer on 2017/4/18
 */

public class TestApi extends BaseApi<HttpService> { // 注意这里的泛型一定要写的,否则使用时还的强转,具体看DemoActivity

    @Override
    public Observable<String> getObservable(HttpService service) {
        return service.getAndroidList();
    }
}
