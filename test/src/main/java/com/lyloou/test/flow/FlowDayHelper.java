package com.lyloou.test.flow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class FlowDayHelper {
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private static Type type = new TypeToken<List<FlowItem>>() {
    }.getType();

    public static String toJson(List<FlowItem> items) {
        return gson.toJson(items, type);
    }


    public static List<FlowItem> fromJson(String day) {
        return gson.fromJson(day, type);
    }
}
