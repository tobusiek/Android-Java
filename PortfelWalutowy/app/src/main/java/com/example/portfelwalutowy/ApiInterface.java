package com.example.portfelwalutowy;

import retrofit2.http.GET;
import retrofit2.Call;

public interface ApiInterface {
    @GET("?format")
    Call<CurrencyInfo> getCurrencyInfo();
}
