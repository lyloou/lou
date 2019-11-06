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

package com.lyloou.test;

import com.lyloou.test.common.Constant;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.douban.DouBanApi;
import com.lyloou.test.kingsoftware.KingsoftwareAPI;
import com.lyloou.test.onearticle.OneArticleApi;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.26 19:04
 * <p>
 * Description:
 */
public class NetWorkTest {

    @Test
    public void kingsoftwareDaily() {
        CountDownLatch latch = new CountDownLatch(1);
        NetWork.get(Constant.Url.Kingsoftware.getUrl(), KingsoftwareAPI.class)
                .getDaily("")
                .subscribe(daily -> {
                    System.out.println(daily.getFenxiang_img());
                    latch.countDown();
                }, throwable -> {
                    throwable.printStackTrace();
                    latch.countDown();
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void doubanMovieTop20() {
        CountDownLatch latch = new CountDownLatch(1);
        NetWork.get(Constant.Url.Douban.getUrl(), DouBanApi.class)
                .getTopMovie(0, 20)
                .map(listHttpResult -> listHttpResult.getSubjects())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(subjects -> {
                    subjects.forEach(System.out::println);
                    latch.countDown();
                }, throwable -> {
                    throwable.printStackTrace();
                    latch.countDown();
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void doubanMovieDetail() {
        CountDownLatch latch = new CountDownLatch(1);
        NetWork.get(Constant.Url.Douban.getUrl(), DouBanApi.class)
                .getMovieDetail("1295644")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(movie -> {
                            System.out.println(movie.getTitle());
                            latch.countDown();
                        }
                        , throwable -> {
                            throwable.printStackTrace();
                            latch.countDown();
                        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void oneArticle() {
        CountDownLatch latch = new CountDownLatch(1);
        NetWork.get(Constant.Url.Meiriyiwen.getUrl(), OneArticleApi.class)
                .getOneArticle(1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(article -> {
                            System.out.println(article.getData().getTitle());
                            latch.countDown();
                        }
                        , throwable -> {
                            throwable.printStackTrace();
                            latch.countDown();
                        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void specialDayArticle() {
        CountDownLatch latch = new CountDownLatch(1);
        NetWork.get(Constant.Url.Meiriyiwen.getUrl(), OneArticleApi.class)
                .getSpecialArticle(1, "20170917")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(article -> {
                            System.out.println(article.getData().getTitle());
                            latch.countDown();
                        }
                        , throwable -> {
                            throwable.printStackTrace();
                            latch.countDown();
                        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void bus() {
        CountDownLatch latch = new CountDownLatch(1);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url("https://api.chelaile.net.cn/bus/line!busesDetail.action?filter=1&modelVersion=0.0.8&last_src=app_meizhu_store&s=android&stats_referer=nearby&push_open=1&stats_act=auto_refresh&userId=unknown&geo_lt=4&lorder=1&geo_lat=22.544675&vc=83&sv=5.1&v=3.34.2&targetOrder=35&gpstype=gcj&imei=866808025006643&lineId=0755-M3553-0&screenHeight=1854&udid=441cf931-6752-4a7c-bd7e-fbd5e8cf6a3f&cshow=linedetail&cityId=014&sign=Bv52Ecvs79wuuZVTTRbzNQ%3D%3D&geo_type=gcj&wifi_open=1&mac=38%3Abc%3A1a%3Ad2%3A97%3A52&deviceType=m1+note&lchsrc=icon&stats_order=1-1&nw=WIFI&AndroidID=9794624a5f2faa00&lng=113.947299&geo_lac=35.0&o1=eda29c1b972004dde4dc96ec491d35ac75e8cb75&language=1&first_src=app_qq_sj&userAgent=Mozilla%2F5.0+%28Linux%3B+Android+5.1%3B+m1+note+Build%2FLMY47D%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F53.0.2785.49+Mobile+MQQBrowser%2F6.2+TBS%2F043305+Safari%2F537.36&lat=22.544675&beforAds=&geo_lng=113.947299")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println(response.body().string());
                }
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
