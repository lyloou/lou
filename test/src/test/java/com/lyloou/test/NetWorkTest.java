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

import com.lyloou.test.common.NetWork;
import com.lyloou.test.douban.HttpResult;
import com.lyloou.test.douban.MovieDetail;
import com.lyloou.test.douban.Subject;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
        NetWork.getKingsoftwareApi()
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
        NetWork.getDouBanApi()
                .getTopMovie(0, 20)
                .map(new Function<HttpResult<List<Subject>>, List<Subject>>() {
                    @Override
                    public List<Subject> apply(@NonNull HttpResult<List<Subject>> listHttpResult) throws Exception {
                        return listHttpResult.getSubjects();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new Consumer<List<Subject>>() {
                    @Override
                    public void accept(@NonNull List<Subject> subjects) throws Exception {
                        subjects.forEach(System.out::println);
                        latch.countDown();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        latch.countDown();
                    }
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
        NetWork.getDouBanApi()
                .getMovieDetail("1295644")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new Consumer<MovieDetail>() {
                               @Override
                               public void accept(@NonNull MovieDetail movie) throws Exception {
                                   System.out.println(movie.getTitle());
                                   latch.countDown();
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
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
