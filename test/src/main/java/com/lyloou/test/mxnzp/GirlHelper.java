package com.lyloou.test.mxnzp;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GirlHelper {

    public static Type getType() {
        return new TypeToken<List<GirlResult.Data.Girl>>() {
        }.getType();
    }
}
