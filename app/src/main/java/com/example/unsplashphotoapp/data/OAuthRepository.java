package com.example.unsplashphotoapp.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unsplashphotoapp.api.oauth.AccessToken;
import com.example.unsplashphotoapp.api.oauth.OAuthApi;
import com.example.unsplashphotoapp.api.oauth.OAuthClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OAuthRepository {

    private static final String TAG = "OAuthRepository";

    private static OAuthRepository mInstance;
    private final OAuthApi oAuthApi;

    private final Application application;

    private OAuthRepository(Application application) {
        Retrofit retrofit = OAuthClient.newInstance();

        oAuthApi = retrofit.create(OAuthApi.class);

        this.application = application;
    }

    public static synchronized OAuthRepository getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new OAuthRepository(application);
        }

        return mInstance;
    }

    public void postAccessToken(String clientId, String clientSecret, String redirectUri, String code, String grantType) {
        Call<AccessToken> accessTokenCall = oAuthApi.getAccessToken(
                clientId,
                clientSecret,
                redirectUri,
                code,
                grantType
        );

        accessTokenCall.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                AccessToken accessToken = response.body();
                storeAccessObject(accessToken);
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public AccessToken getAccessToken() {
        SharedPreferences sharedPreferences = application.getSharedPreferences("ACCESS_TOKEN_PREFS", Context.MODE_PRIVATE);
        String tokenJson = sharedPreferences.getString("access_token_object", "null");

        if (tokenJson == null) {
            return null;
        } else {
            return new Gson().fromJson(
                    tokenJson,
                    AccessToken.class
            );
        }
    }

    private void storeAccessObject(AccessToken accessToken) {
        SharedPreferences sharedPreferences = application.getSharedPreferences("ACCESS_TOKEN_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String tokenJson = gson.toJson(accessToken);

        editor.putString("access_token_object", tokenJson);
        editor.apply();

        Log.d(TAG, "storeAccessObject: " + tokenJson);
    }
}
