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

package com.lyloou.test.douban;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.05.04 15:17
 * <p>
 * Description:
 */
public interface DouBanApi {
    // [豆瓣api报错：invalid_apikey - kfgauss的博客 - CSDN博客](https://blog.csdn.net/kfgauss/article/details/91492643)
    String API_KEY = "?apikey=0df993c66c0c636e29ecbb5344252a4a";

    @GET("top250" + API_KEY)
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("top250" + API_KEY)
    Observable<HttpResult<List<Subject>>> getNewMovie(@Query("start") int start, @Query("count") int count);

    @GET("subject/{id}" + API_KEY)
    Observable<MovieDetail> getMovieDetail(@Path("id") String id);
}
