package com.lyloou.test.man;

import java.io.Serializable;

class Data implements Serializable {
    private String title;
    private String url;
    private String lastUrl;
    private int position;

    public String getTitle() {
        return title;
    }

    public Data setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Data setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getLastUrl() {
        return lastUrl;
    }

    public Data setLastUrl(String lastUrl) {
        this.lastUrl = lastUrl;
        return this;
    }

    public int getPosition() {
        return position;
    }

    public Data setPosition(int position) {
        this.position = position;
        return this;
    }
}
