package com.example.bloadbank.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApi {
    public static  String BASE_URL = "http://ipda3-tech.com/blood-bank/api/v1/";
    private static Retrofit retrofit = null;
    public static Retrofit GetClient()
    {
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
