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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.04.12 14:32
 * <p>
 * Description:
 */
public interface PostService {
    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") int id);

    @POST("posts")
    Call<Post> postPost(@Body  Post post);

    @GET
    Call<ResponseBody> getByUrl(@Url() String url);
}
