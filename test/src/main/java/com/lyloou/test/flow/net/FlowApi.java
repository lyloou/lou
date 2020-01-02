package com.lyloou.test.flow.net;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FlowApi {

    @POST("sync")
    Observable<CommonResult> sync(@Body FlowReq flowReq);
}
