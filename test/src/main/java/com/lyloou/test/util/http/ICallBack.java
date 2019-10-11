package com.lyloou.test.util.http;

public interface ICallBack {
    String getUrl();

    void doResponse(String responseInfo);

    void doError(String errorMsg);
}
