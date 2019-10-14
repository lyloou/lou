package com.lyloou.test.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Ugson {
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson gson = new GsonBuilder().setLenient().create();

    public static Gson getGson() {
        return gson;
    }

    public static <T> List<T> getList(String data) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        return gson.fromJson(data, type);
    }
}
