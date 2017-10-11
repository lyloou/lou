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

package com.lyloou.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaobaoMain {


    public static void main(String[] args) throws Exception {

        String url = "http://ip.taobao.com/";
        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TaobaoService service = retrofit.create(TaobaoService.class);

        Call<TaobaoIp> ipMsg = service.getIpMsg("59.108.54.37");
        ipMsg.enqueue(new Callback<TaobaoIp>() {
            @Override
            public void onResponse(Call<TaobaoIp> call, Response<TaobaoIp> response) {
                TaobaoIp ip = response.body();
                if (ip != null) {
                    System.out.println(ip.getData().getCountry());
                } else {
                    System.out.println("一个错误发生了");
                }
            }

            @Override
            public void onFailure(Call<TaobaoIp> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
}
