package com.jooyer.jooyerretrofits;

import java.util.List;

/**
 * Created by Jooyer on 2017/2/13
 */
public class TestBean {


    /**
     * error : false
     * results : [{"_id":"589ece26421aa92710db961a","createdAt":"2017-02-11T16:41:10.3Z","desc":"关于Android应用的耗电量的统计分析方法和工具","images":["http://img.gank.io/f43faa62-8c4a-4eba-9c67-2142539dc2a5","http://img.gank.io/bee4090a-26cb-4917-ad3f-e2a9b485f140"],"publishedAt":"2017-02-13T11:54:17.922Z","source":"web","type":"Android","url":"https://hujiaweibujidao.github.io/blog/2017/01/24/how-to-know-your-applications-battery-stats/","used":true,"who":"潇涧"},{"_id":"58a00b79421aa901ef405786","createdAt":"2017-02-12T15:15:05.362Z","desc":"一款优雅的遵循 Material Design 的开源音乐播放器","images":["http://img.gank.io/9af611e5-7eb0-4c03-97ed-1798fba0019e","http://img.gank.io/69dd5400-56bf-4455-b3ab-23912cdfb230"],"publishedAt":"2017-02-13T11:54:17.922Z","source":"chrome","type":"Android","url":"https://github.com/hefuyicoder/ListenerMusicPlayer","used":true,"who":"Jason"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 589ece26421aa92710db961a
         * createdAt : 2017-02-11T16:41:10.3Z
         * desc : 关于Android应用的耗电量的统计分析方法和工具
         * images : ["http://img.gank.io/f43faa62-8c4a-4eba-9c67-2142539dc2a5","http://img.gank.io/bee4090a-26cb-4917-ad3f-e2a9b485f140"]
         * publishedAt : 2017-02-13T11:54:17.922Z
         * source : web
         * type : Android
         * url : https://hujiaweibujidao.github.io/blog/2017/01/24/how-to-know-your-applications-battery-stats/
         * used : true
         * who : 潇涧
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
