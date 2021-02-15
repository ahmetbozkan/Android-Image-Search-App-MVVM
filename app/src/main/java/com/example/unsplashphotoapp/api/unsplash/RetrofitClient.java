package com.example.unsplashphotoapp.api.unsplash;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.unsplash.com/";

    private static Retrofit mInstance;

    public static synchronized Retrofit newInstance() {
        if (mInstance == null) {
            mInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mInstance;
    }
}
