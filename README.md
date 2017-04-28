# JooyerRetrofit
这是一个使用 RxJava + Retrofit + FastJson 封装的库,内部使用了 RxLife 来管理请求状态与 Activivty/Fragment 的生命周期同步! 

## Add dependency

### Gradle
`compile 'com.jooyer.jooyerretrofit:JooyerRetrofit:0.0.2'`

### Maven
```
<dependency>
  <groupId>com.jooyer.jooyerretrofit</groupId>
  <artifactId>retrofit</artifactId>
  <version>0.0.2</version>
  <type>pom</type>
</dependency>
```

这里先解释下 RxLife 的作用:
在此处,我们主要是根据 Activity/Fragment 生命周期中的 onPause/onStop 来处理请求取消操作!
在本库中,使用了 onStop() 时取消网络请求操作

## 使用步骤如下:

### 如果是Fragment

>如果不使用RxLife
  
    
   
