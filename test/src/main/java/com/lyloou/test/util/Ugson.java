package com.lyloou.test.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Ugson {
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson gson = new GsonBuilder().setLenient().create();

    public static String toPrettyJson(Object o) {
        return prettyGson.toJson(o);
    }
}
