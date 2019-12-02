package com.lyloou.test.mxnzp;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JokeApi {
    @GET("api/jokes/list")
    Observable<JokeResult> getJoke(@Query("page") int page);
}
