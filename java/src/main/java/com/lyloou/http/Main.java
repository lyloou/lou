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

package com.lyloou.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main {

    public static void main(String[] args) {
        sample1();
    }

    // OkHttp测试
    private static void sample1() {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder()
                .tag("1")
                .url("http://www.baidu.com");

        Request request = builder.build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
                //call.cancel();
            }
        });

        // 判断call是否调用
        System.out.println(call.isCanceled());

        // 取消打指定标签的、等待执行的请求
        for (Call c : client.dispatcher().queuedCalls()) {
            if (c.request().tag().equals("2")) {
                c.cancel();
            }
        }

        // 取消打指定标签的、运行中的请求
        for (Call c : client.dispatcher().runningCalls()) {
            if (c.request().tag().equals("2")) {
                c.cancel();
            }
        }


    }
}
