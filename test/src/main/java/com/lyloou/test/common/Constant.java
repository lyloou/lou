package com.lyloou.test.common;


public interface Constant {
    int TRUE = 1;
    int FALSE = 0;

    enum Url implements Str {
        Kingsoftware("http://open.iciba.com/"),
        Gank("http://gank.io/api/"),
        Douban("https://api.douban.com/v2/movie/"),
        Meiriyiwen("https://interface.meiriyiwen.com/article/"),
        Weather("http://t.weather.sojson.com/"),
        YouDao("http://fanyi.youdao.com/openapi.do?keyfrom=Yanzhikai&key=2032414398&type=data&doctype=json&version=1.1&q=car/"),
        Mxnzp("https://www.mxnzp.com/"),
        GithubTrendingApi("https://github-trending-api.now.sh/"),
        FlowApi("http://114.67.95.131:8888/api/v1/flow/"),
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
