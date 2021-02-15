package com.example.unsplashphotoapp.ui.photo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.unsplashphotoapp.api.oauth.AccessToken;
import com.example.unsplashphotoapp.api.unsplash.SearchResponse;
import com.example.unsplashphotoapp.api.unsplash.UnsplashResponse;
import com.example.unsplashphotoapp.data.OAuthRepository;
import com.example.unsplashphotoapp.data.UnsplashRepository;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;

import java.util.List;

public class UnsplashViewModel extends AndroidViewModel {

    private final LiveData<List<UnsplashResponse>> photoList;

    private final MutableLiveData<String> QUERY_STRING = new MutableLiveData<>();

    private final UnsplashRepository mRepository;
    private final OAuthRepository mOauthRepository;

    public UnsplashViewModel(Application application) {
        super(application);

        mRepository = UnsplashRepository.getInstance(application);
        mOauthRepository = OAuthRepository.getInstance(application);

        photoList = mRepository.getPhotos();
    }

    public LiveData<List<UnsplashResponse>> getPhotoList() {
        return photoList;
    }

    public void refresh() {
        mRepository.refresh();
    }

    public LiveData<SearchResponse> searchPhotos() {
        return Transformations.switchMap(QUERY_STRING, input -> {
            if (input == null || input.equals("") || input.equals("%%")) {
                return null;
            } else {
                return mRepository.searchPhotos(input);
            }
        });
    }

    public MutableLiveData<String> getQUERY_STRING() {
        return QUERY_STRING;
    }

    public void postAccessToken(String clientId, String clientSecret, String redirectUri, String code, String grantType) {
        mOauthRepository.postAccessToken(clientId, clientSecret, redirectUri, code, grantType);
    }

    public AccessToken getAccessToken() {
        return mOauthRepository.getAccessToken();
    }

    public LiveData<UnsplashUser> getCurrentUser(String header) {
        return mRepository.getCurrentUser(header);
    }

    public void likePhoto(String id, String header) {
        mRepository.likePhoto(id, header);
    }
}
