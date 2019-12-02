package com.lyloou.test.github;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubApi {
    @GET("repositories")
    Observable<List<Repository>> getRepository(@Query("language") String language, @Query("since") String since);
}
