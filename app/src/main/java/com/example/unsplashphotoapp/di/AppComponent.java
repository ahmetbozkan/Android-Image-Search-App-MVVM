package com.example.unsplashphotoapp.di;

import android.app.Application;

import com.example.unsplashphotoapp.di.modules.AppModule;
import com.example.unsplashphotoapp.di.modules.UnsplashViewModelsModule;
import com.example.unsplashphotoapp.di.modules.ViewModelFactoryModule;
import com.example.unsplashphotoapp.ui.gallery.GalleryFragment;
import com.example.unsplashphotoapp.ui.profile.ProfileFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        ViewModelFactoryModule.class,
        UnsplashViewModelsModule.class
})
public interface AppComponent {

    void inject(GalleryFragment galleryFragment);

    void inject(ProfileFragment profileFragment);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

}
