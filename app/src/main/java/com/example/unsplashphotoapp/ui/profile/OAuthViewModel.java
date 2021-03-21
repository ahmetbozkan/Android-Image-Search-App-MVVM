package com.example.unsplashphotoapp.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.unsplashphotoapp.api.oauth.AccessToken;
import com.example.unsplashphotoapp.data.repositories.OAuthRepository;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OAuthViewModel extends ViewModel {

    private final OAuthRepository oAuthRepository;

    @Inject
    public OAuthViewModel(OAuthRepository oAuthRepository) {
        this.oAuthRepository = oAuthRepository;
    }

    public void postAccessToken(String clientId, String clientSecret, String redirectUri, String code, String grantType) {
        oAuthRepository.postAccessToken(clientId, clientSecret, redirectUri, code, grantType);
    }

    public AccessToken getAccessToken() {
        return oAuthRepository.getAccessToken();
    }

    public LiveData<UnsplashUser> getCurrentUser(String header) {
        return oAuthRepository.getCurrentUser(header);
    }

    public void updateProfile(String header, String username) {
        oAuthRepository.updateProfile(header, username);
    }



}
