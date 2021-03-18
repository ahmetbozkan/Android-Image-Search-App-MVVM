package com.example.unsplashphotoapp.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.unsplashphotoapp.api.oauth.AccessToken;
import com.example.unsplashphotoapp.api.unsplash.SearchResponse;
import com.example.unsplashphotoapp.data.OAuthRepository;
import com.example.unsplashphotoapp.data.UnsplashRepository;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<String> QUERY_STRING = new MutableLiveData<>("random");

    private final UnsplashRepository mRepository;

    @Inject
    public GalleryViewModel(UnsplashRepository unsplashRepository) {
        mRepository = unsplashRepository;
    }

    public void refresh() {
        mRepository.refresh(QUERY_STRING.getValue());
    }

    public LiveData<SearchResponse> searchPhotos() {
        return Transformations.switchMap(QUERY_STRING, mRepository::searchPhotos);
    }

    public void setNewQuery(String query) {
        QUERY_STRING.setValue(query);
    }

    public AccessToken getAccessToken() {
        return mRepository.getAccessToken();
    }

    public void likePhoto(String id, String header) {
        mRepository.likePhoto(id, header);
    }

}
