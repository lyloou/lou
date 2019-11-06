/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.test.common;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:    Lou
 * Version:   V2.0
 * Date:      2017.06.26 19:15
 * Update:    2019-11-06 10:42
 * <p>
 * Description:
 */
public class NetWork {

    private static Map<String, Object> map = new HashMap<>();

    public static <T> T get(@NonNull String baseUrl, @NonNull Class<T> clazz) {
        String key = clazz.getSimpleName().concat(baseUrl);
        if (map.containsKey(key)) {
            //noinspection unchecked
            return (T) map.get(key);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        T t = retrofit.create(clazz);
        map.put(key, t);
        return t;
    }
}
