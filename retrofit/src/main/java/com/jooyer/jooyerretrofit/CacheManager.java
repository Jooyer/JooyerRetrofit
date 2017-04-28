package com.jooyer.jooyerretrofit;

import android.database.sqlite.SQLiteDatabase;

import com.jooyer.jooyerretrofit.dao.CookieResultDao;
import com.jooyer.jooyerretrofit.dao.DaoMaster;
import com.jooyer.jooyerretrofit.dao.DaoSession;
import com.jooyer.jooyerretrofit.http.CookieResult;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 数据库管理类(管理缓存)
 * Created by Jooyer on 2017/2/14
 */
public class CacheManager {

    private static CacheManager sManager;

    private final static String CACHE_DB_NAME = "cache_db";

    private DaoMaster.DevOpenHelper mHelper;

    private CacheManager() {
        mHelper = new DaoMaster.DevOpenHelper(RxRetrofit.getInstance().getContext(),CACHE_DB_NAME);
    }

    public static CacheManager getInstance(){
        if (null == sManager){
            synchronized (CacheManager.class){
                if (null == sManager){
                    sManager = new CacheManager();
                }
            }
        }
        return sManager;
    }

    private SQLiteDatabase getWritableDatabase(){
        if (null == mHelper){
            mHelper = new DaoMaster.DevOpenHelper(RxRetrofit.getInstance().getContext(),CACHE_DB_NAME);
        }
        return mHelper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase(){
        if (null == mHelper){
            mHelper = new DaoMaster.DevOpenHelper(RxRetrofit.getInstance().getContext(),CACHE_DB_NAME);
        }

        return mHelper.getReadableDatabase();
    }


    public void saveEntry(CookieResult result){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResultDao dao = daoSession.getCookieResultDao();


        QueryBuilder<CookieResult> builder = dao.queryBuilder();
        builder.where(CookieResultDao.Properties.Url.eq(result.getUrl()));

        List<CookieResult> list = builder.list();

        if (null == list || list.isEmpty()){
            // 没有缓存过数据
            dao.insert(result);
        }else {
            CookieResult cookieResult = list.get(0);
            result.setId(cookieResult.getId());
            updateEntry(result);
        }
    }


    public void savedEntry(CookieResult result){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResultDao dao = daoSession.getCookieResultDao();
        //当主键存在的时候会替换掉,所以能够很好的执行插入操作
        dao.insertOrReplace(result);
    }

    public void deleteEntry(CookieResult result){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResultDao dao = daoSession.getCookieResultDao();

        dao.delete(result);
    }

    public void deleteEntry(String url){
        CookieResult result = queryEntry(url);
        if (null != result)
            deleteEntry(result);
    }

    public void updateEntry(CookieResult result){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResultDao dao = daoSession.getCookieResultDao();
        dao.update(result);
    }

    public CookieResult queryEntry(String url){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResultDao dao = daoSession.getCookieResultDao();

        QueryBuilder<CookieResult> builder = dao.queryBuilder();
        builder.where(CookieResultDao.Properties.Url.eq(url));
        List<CookieResult> list = builder.list();
        if (list.isEmpty()){
            return null;
        }else {
            return list.get(0);
        }
    }

    public List<CookieResult> queryAll(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResultDao dao = daoSession.getCookieResultDao();
        QueryBuilder<CookieResult> builder = dao.queryBuilder();
        return builder.list();
    }


}
