package com.lyloou.lou.util.http;

import org.json.JSONObject;

public interface ICallBack {
    String getUrl();

    void doResponse(JSONObject responseInfo);

    void doError(String errorMsg);
}