package com.lyloou.test.mxnzp;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MxnzpApi {
    @GET("api/jokes/list")
    Observable<JokeResult> getJoke(@Query("page") int page);

    @GET("api/image/girl/list")
    Observable<GirlResult> getGirl(@Query("page") int page);
}
