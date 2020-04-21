package com.metrocem.mis.Retrofit;

import com.metrocem.mis.Home.MainActivity;
import com.metrocem.mis.Utilities.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitInstance {

    private static Retrofit retrofit;
    //private static final String BASE_URL = "http://mis.nurtech.xyz/api/v1/";
    //private static final String BASE_URL = "http://misstage.nurtech.xyz/api/v1/";
    private static final String accept = "application/json";


    private static final String token = MainActivity.authToken;

    public static Retrofit getRetrofitInstance() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", accept)
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(request);
            }
        });

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
