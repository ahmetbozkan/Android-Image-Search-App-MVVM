package com.example.unsplashphotoapp.di.modules;

import com.example.unsplashphotoapp.api.oauth.OAuthApi;
import com.example.unsplashphotoapp.api.unsplash.UnsplashApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class AppModule {

    private static final String BASE_URL = "https://api.unsplash.com/";

    public static final String OAUTH_BASE_URL = "https://unsplash.com/";

    @Provides
    static Gson provideLenientGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Singleton
    @Provides
    @Named(value = "unsplash_client")
    static Retrofit provideRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Singleton
    @Provides
    static UnsplashApi provideUnsplashApi(@Named(value = "unsplash_client") Retrofit retrofit) {
        return retrofit.create(UnsplashApi.class);
    }

    @Singleton
    @Provides
    @Named(value = "oauth_client")
    static Retrofit provideOAuthRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(OAUTH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Singleton
    @Provides
    static OAuthApi provideOAuthApi(@Named(value = "oauth_client") Retrofit retrofit) {
        return retrofit.create(OAuthApi.class);
    }

}
