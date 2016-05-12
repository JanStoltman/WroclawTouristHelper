package com.example.yggdralisk.wroclawtouristhelper;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yggdralisk on 12.05.16.
 */
public interface RetrofitInterface {
    @GET("opendata/transport/transport_stacje_WRM.xls")
    Call<ResponseBody> getBikePoints();
}
