package com.example.unsplashphotoapp.di.modules;

import androidx.lifecycle.ViewModel;

import com.example.unsplashphotoapp.di.ViewModelKey;
import com.example.unsplashphotoapp.ui.gallery.GalleryViewModel;
import com.example.unsplashphotoapp.ui.profile.OAuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class UnsplashViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel.class)
    public abstract ViewModel bindGalleryViewModel(GalleryViewModel galleryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OAuthViewModel.class)
    public abstract ViewModel bindOAuthViewModel(OAuthViewModel oAuthViewModel);

}
