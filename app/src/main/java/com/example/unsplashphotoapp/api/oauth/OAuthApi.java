package com.example.unsplashphotoapp.api.oauth;

import com.example.unsplashphotoapp.data.submodels.UnsplashUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OAuthApi {

    @POST("oauth/token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("redirect_uri") String redirect_uri,
            @Field("code") String code,
            @Field("grant_type") String grant_type
    );
}
