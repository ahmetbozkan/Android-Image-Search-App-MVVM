package com.example.unsplashphotoapp.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unsplashphotoapp.api.oauth.AccessToken;
import com.example.unsplashphotoapp.api.oauth.OAuthApi;
import com.example.unsplashphotoapp.api.unsplash.UnsplashApi;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class OAuthRepository {

    private static final String TAG = "OAuthRepository";

    private final OAuthApi oAuthApi;
    private final UnsplashApi unsplashApi;

    private final Application application;

    private final MutableLiveData<UnsplashUser> currentUser = new MutableLiveData<>();

    @Inject
    public OAuthRepository(Application application, OAuthApi oAuthApi, UnsplashApi unsplashApi) {
        this.oAuthApi = oAuthApi;
        this.application = application;
        this.unsplashApi = unsplashApi;
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
                Log.e(TAG, "onFailure: failed to get" + t.getMessage());
            }
        });
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

    private void loadCurrentUser(String header) {
        Call<UnsplashUser> currentUserCall = unsplashApi.getCurrentUser(header);
        currentUserCall.enqueue(new Callback<UnsplashUser>() {
            @Override
            public void onResponse(@NonNull Call<UnsplashUser> call, @NonNull Response<UnsplashUser> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                UnsplashUser user = response.body();
                currentUser.setValue(user);
                Log.d(TAG, "onResponse: Current User: " + user.getUsername());
            }

            @Override
            public void onFailure(@NonNull Call<UnsplashUser> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: Error on loading current user: " + t.getLocalizedMessage());
            }
        });
    }

    public LiveData<UnsplashUser> getCurrentUser(String header) {
        if (currentUser.getValue() == null) {
            loadCurrentUser(header);
        }
        return currentUser;
    }

    public void updateProfile(String header, String username) {
        Call<UnsplashUser> updateCall = unsplashApi.updateProfile(header, username);
        updateCall.enqueue(new Callback<UnsplashUser>() {
            @Override
            public void onResponse(@NonNull Call<UnsplashUser> call, @NonNull Response<UnsplashUser> response) {
                if (!response.isSuccessful() && response.body() == null) {
                    Log.d(TAG, "onResponse: " + response.code());
                    return;
                }

                UnsplashUser user = response.body();
                currentUser.setValue(user);
                Log.d(TAG, "onResponse: Updated username: " + user.getUsername());
            }

            @Override
            public void onFailure(@NonNull Call<UnsplashUser> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Error on updating profile: " + t.getMessage());
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
}
