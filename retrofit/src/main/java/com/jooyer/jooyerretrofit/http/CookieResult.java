package com.jooyer.jooyerretrofit.http;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/** 本地缓存读取与保存数据结构
 * Created by Jooyer on 2017/2/14
 */
@Entity
public class CookieResult {

    @Id(autoincrement = true)
    public long id;

    /**
     *  保存服务器返回的数据 或者 从数据库获取数据的唯一标示(key)
     */
    private String url;

    private String result;

    private long time;

    public CookieResult() {
    }

    public CookieResult(String url, String result, long time) {
        this.url = url;
        this.result = result;
        this.time = time;
    }

    @Generated(hash = 1914207567)
    public CookieResult(long id, String url, String result, long time) {
        this.id = id;
        this.url = url;
        this.result = result;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CookieResult{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", result='" + result + '\'' +
                ", time=" + time +
                '}';
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
