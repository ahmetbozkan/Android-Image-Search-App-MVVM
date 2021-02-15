package com.example.unsplashphotoapp.api.oauth;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OAuthClient {

    private static final String BASE_AUTH_URL = "https://unsplash.com/";

    private static Retrofit mInstance;

    public static synchronized Retrofit newInstance() {
        if (mInstance == null) {
            mInstance = new Retrofit.Builder()
                    .baseUrl(BASE_AUTH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mInstance;
    }
}
