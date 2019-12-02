package com.lyloou.test.common;


public interface Constant {


    enum Url implements Str {
        Kingsoftware("http://open.iciba.com/"),
        Gank("http://gank.io/api/"),
        Douban("https://api.douban.com/v2/movie/"),
        Laifudao("http://api.laifudao.com/"),
        Meiriyiwen("https://interface.meiriyiwen.com/article/"),
        Weather("http://t.weather.sojson.com/"),
        YouDao("http://fanyi.youdao.com/openapi.do?keyfrom=Yanzhikai&key=2032414398&type=data&doctype=json&version=1.1&q=car/"),
        Mxnzp("https://www.mxnzp.com/"),
        GithubTrendingApi("https://github-trending-api.now.sh/"),
        ;
        private String url;

        Url(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String str() {
            return getSimpleStr(getClass(), name());
        }

    }

    enum Action implements Str {
        ;

        @Override
        public String str() {
            return getSimpleStr(getClass(), name());
        }
    }

    enum Key implements Str {
        BACKGROUND_SERVER;

        @Override
        public String str() {
            return getSimpleStr(getClass(), name());
        }
    }

    static String getSimpleStr(Class clazz, String name) {
        return clazz.getSimpleName().toUpperCase().concat("_").concat(name);
    }

    interface Str {
        String str();
    }
}
