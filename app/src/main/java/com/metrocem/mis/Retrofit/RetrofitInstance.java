package com.metrocem.mismetrocem.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://mis.nurtech.xyz/api/v1/";
//    static Gson gson = new GsonBuilder()
//            .setLenient()
//            .create();
//
//    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

//    static OkHttpClient client = httpClient.build();

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
