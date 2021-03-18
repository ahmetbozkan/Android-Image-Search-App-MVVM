package com.example.unsplashphotoapp.di.modules;

import androidx.lifecycle.ViewModelProvider;

import com.example.unsplashphotoapp.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory factory);

}
