package com.example.unsplashphotoapp.ui.saves;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.unsplashphotoapp.data.entities.UnsplashPhoto;
import com.example.unsplashphotoapp.data.repositories.UnsplashRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SavesViewModel extends ViewModel {

    private final UnsplashRepository mRepository;

    @Inject
    public SavesViewModel(UnsplashRepository unsplashRepository) {
        mRepository = unsplashRepository;
    }

    public void deletePhoto(UnsplashPhoto unsplashPhoto) {
        mRepository.deleteSavedPhoto(unsplashPhoto);
    }

    public LiveData<List<UnsplashPhoto>> getAllSaves() {
        return mRepository.getAllSaves();
    }

}

