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

import com.lyloou.test.douban.DouBanApi;
import com.lyloou.test.kingsoftware.KingsoftwareAPI;
import com.lyloou.test.laifudao.LaiFuDaoApi;
import com.lyloou.test.onearticle.OneArticleApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.26 19:15
 * <p>
 * Description:
 */
public class NetWork {
    private static KingsoftwareAPI sKingsoftwareAPI;
    private static LaiFuDaoApi sLaiFuDaoApi;
    private static DouBanApi sDouBanApi;
    private static OneArticleApi sOneArticleApi;

    public static KingsoftwareAPI getKingsoftwareApi() {
        if (sKingsoftwareAPI == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://open.iciba.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            sKingsoftwareAPI = retrofit.create(KingsoftwareAPI.class);
        }
        return sKingsoftwareAPI;
    }

    public static LaiFuDaoApi getLaiFuDaoApi() {
        if (sLaiFuDaoApi == null) {
            OkHttpClient client = new OkHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.laifudao.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            sLaiFuDaoApi = retrofit.create(LaiFuDaoApi.class);
        }
        return sLaiFuDaoApi;
    }

    public static DouBanApi getDouBanApi() {
        if (sDouBanApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.douban.com/v2/movie/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            sDouBanApi = retrofit.create(DouBanApi.class);
        }
        return sDouBanApi;
    }

    public static OneArticleApi getOneArticleApi() {
        if (sOneArticleApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://interface.meiriyiwen.com/article/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            sOneArticleApi = retrofit.create(OneArticleApi.class);
        }
        return sOneArticleApi;
    }
}
