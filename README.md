# JooyerRetrofit
这是一个使用 RxJava + Retrofit + FastJson 封装的库,内部使用了 RxLife 来管理请求状态与 Activivty/Fragment 的生命周期同步, 同时默认使用了GreenDao来处理缓存!

## 联系我:
如果在使用中,有任何疑问,可以加 我微信(QQ) 712952521,我乐意与你探讨!<br/>
请关注 [我的Github]("https://github.com/Jooyer")

## 版本(Version)
&emsp;	1.0.0	
&emsp;&emsp;1.将 Retrofit 版本从 2.0.0 升级到 2.3.0,将 RxJava 从 1.2.1 升级到 2.0.1<br/>
&emsp;&emsp;2.修复在上传文件时,Retroift报 没有 value 的错误而导致无法上传!<br/>
&emsp;&emsp;3.精简了一部分方法,减少方法数!

&emsp;	0.0.3	
&emsp;&emsp;修改 0.0.2 版本中的的不正确部分!更新了参数格式,见参知意!

&emsp;	0.0.2	
&emsp;&emsp;首次提交,出现部分参数格式不整齐,ProgressDialog 文本提示位置不正确!


# Add dependency

## Gradle
	compile 'com.jooyer.jooyerretrofit:retrofit:0.0.3'

## Maven

	<dependency>
	  <groupId>com.jooyer.jooyerretrofit</groupId>
	  <artifactId>retrofit</artifactId>
	  <version>1.0.0</version>
	  <type>pom</type>
	</dependency>

# 使用方法
## RxLife 管理请求
&emsp;PS:使用 RxLife 管理请求,就是希望在 Activity 界面 onPause()
或者 onStop() 时,我们停止请求,这样做有两个目的:<br/>
&emsp;&emsp;1.用户离开了当前界面,停止请求节省部分流量<br/>
&emsp;&emsp;2.用户在新界面不会因为之前界面还在请求网络,造成当前界面网络请求缓慢.

## Retrofit 请求接口定义
我们需要自己定义一个请求接口,一般我们使用 HttpService 命名,其内部如下:

	public interface HttpService {
	
	    /**
	     *  获取一个集合
	     */
	    @GET("data/Android/2/10")
	    Observable<String> getAndroidList();
	
	}

PS:在这里,大家可能注意到了,我们返回值用的 String,之所以这样,其实其主要目的为了更自由的回调处理.我们可以根据服务器对应接口返回的 String 解析 成相应的 Bean,同时可以统一处理服务器返回的错误信息,如成功 200, 错误603...

## Retrofit 初始化
定义一个 Application ,在里面初始化 Retrofit 必要信息,我将其放在异步中处理,所以大家尽可放心!

	public class MyApplication extends Application {
	    @Override
	    public void onCreate() {
	        super.onCreate();
	
	        /**
	         *  注意这个初始最好放在这里,具体参数含义,点击进入方法内,有详细描述
	         */
	        // 提示: 如果设置了 OKHTTP 缓存,则无论你在请求的 api 中是否设置 setCache() 和 setMethod()
	        // 都会正常去获取缓存.
	        RxRetrofit.getInstance().init(this,"test",Constant.BASE_URL,true,false);
			// 设置网络请求时的界面展示,有默认效果
        	RxRetrofit.getInstance().setProgressDialog(null);
	    }
	}

	PS: 注意,需要在 Manifest.xml 中给三个权限和设置 android:name=".MyApplication"
	 	<!-- 网络必备 -->
	    <uses-permission android:name="android.permission.INTERNET"/>
	    <!-- 读写必备 -->
	    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
	
	虽然,我在Library里面申请了权限,但是,读写权限还得在 SDK >= 23 时动态请求权限 


## Activity 中使用
### 1.使用 RxAppCompatActivity

	. 写一个继承自RxAppCompatActivity的 Activity
	. 实例化一个 HttpManager,虽然说有许多大神建议写一个单利,这个仁者见仁.

		HttpManager manager = new HttpManager(this, OnHttpCallBackListener);

	PS: HttpManager 有2个构造方法,其中第二个构造方法,主要是多了一个boolean,主要是看请
	求时是否需要携带 token(一般都是用token来作为登录过期的一种控制,每次请求时携带token
	,如果服务器发现token过期,会返回一个code提示客户端).比如登录时就不需呀携带token,
	而个人中心资料,需要携带token了.回调就不说了,具体看下往下看

	.如果只有一次请求,则可以写匿名类.
		example:
	        manager.doHttpWithOutRxActivity(HttpService.class,
				new BaseApi<HttpService>(){
            		@Override
            		public Observable<String> getObservable(HttpService service) {
               		 return service.getAndroidList();
            	}       
				});

	.如果有多个请求,就写一个类,继承自 BaseApi
		example:
			// 注意这里的泛型一定要写的,否则使用时还的强转,具体看DemoActivity
			public class TestApi extends BaseApi<HttpService> { 
			
			    // 温馨提示: 如果你的请求有参数传递可以写在此处
			    // 例如我们一次请求 5 条数据或者动态改变请求数量
			
			    private int count = 0 ;
			
			    public void setCount(int count) {
			        this.count = count;
			    }
			
			    @Override
			    public Observable<String> getObservable(HttpService service) {
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



	.开始请求
		 TestApi api = new TestApi();
        // 使用 RxLife 管理下载生命周期必须设置
		// PS:其实,就算是这里设置了,在 HttpManager.doxxx()时不调用doRx...(),还是没使用 RxLife 了 
        api.setRxAppCompatActivity(DemoActivity.this);
        // 默认有网时缓存时间为60s
        api.setCacheTimeInConnect(10);
		
		// 如果我想使用 TestApi 中的 getAndroidSingle();
		api.setApiFlag(1);// 默认为 0
 
        // 设置是否需要缓存
        api.setCache(true);
        // 重要事情说三遍: 如果设置了需要缓存,则必须设置下面方法,
        // 重要事情说三遍: 如果设置了需要缓存,则必须设置下面方法,
        // 重要事情说三遍: 如果设置了需要缓存,则必须设置下面方法,
        // 推荐写法 ,将 HttpService 中的剩余部分链接放在这里,也不会造成缓存的 KEY 发生重复冲突
        api.setMethod("data/Android/2/1");
		
		// 调用 doRx...(),则必须是当前视图继承是Rx...视图,且 BaseApi.setRx...();
        manager.doHttpWithRxActivity(api, HttpService.class);

		
	.回调的处理
		   private OnHttpCallBackListener mOnHttpCallBackListener = new OnHttpCallBackListener() {
        @Override
        public void onNext(String result, String method) {
			// 在这里,由于每一个界面服务器返回的 json 数据结构都不一样,所以这里返回 json 字符串!
			// 我们可以根据 json 创建不同的 Bean, 温馨提示: Android Studio 
			// 可以下载 GsonFromat 处理,效果杠杠的
			// 我们解析 result 为相应的 Bean 
            TestBean bean = JSONObject.parseObject(result, TestBean.class);
            tv_test.setText(bean.getResults().get(0).getDesc());
        }

        @Override
        public void onError(ApiException e) {
            // 注意:获取具体错误显示信息,使用 e.getDisplayMessage();
            Toast.makeText(DemoActivity.this, "错误信息: " + e.getDisplayMessage(), Toast.LENGTH_SHORT).show();
			//具体错误信息  
            Log.i("Demo", "============== : " + e.getMessage());
        }
    };



### 2.不使用 RxAppCompatActivity

	.这里我就不写代码了,主要讲下区别
		1. 可以选择继承或者RxAppCompatActivity
		2. 不需要调用 BaseApi.setRx...()
		3. 不能调用 HttpManager.doWithRx...()
		4. 其他和使用 RxAppCompatActivity 一样
		
### 3.使用 RxFragment

	.和 RxAppCompatActivity 类似,有3个区别
		1. Fragment 继承自 RxFragment
		2. 需要调用 BaseApi.setRxFragment()
		3. 需要调用 HttpManager.doWithRxFragment()
		
### 4.不使用 RxFragment

	.和不使用 RxAppCompatActivity 一样的用法 

### 使用小技巧:

	. 无论写匿名 BaseApi 还是 继承自 BaseApi ,都写上泛型类型(就是你的请求接口类 
		HttpService),方便使用且不需要强转,不易出错!
	. BaseApi中,都是可以链式调用
		BaseApi.setCancle(false).setApiFlag(2).setConnOutTime(2000)等
	. 可以自定义设置 ProgressDialgo,方便定制


## 特别鸣谢: 

[小河马](http://www.jianshu.com/p/bd758f51742e),同时感谢各种开源大神,开源公司的付出,我再一次站在巨人的肩膀上了!

## License


```

&copy;Copyright 2017 Jooyer
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
