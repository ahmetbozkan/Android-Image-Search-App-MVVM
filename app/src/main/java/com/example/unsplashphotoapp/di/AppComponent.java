package com.example.unsplashphotoapp.di;

import android.app.Application;

import com.example.unsplashphotoapp.di.modules.RetrofitModule;
import com.example.unsplashphotoapp.di.modules.RoomModule;
import com.example.unsplashphotoapp.di.modules.UnsplashViewModelsModule;
import com.example.unsplashphotoapp.di.modules.ViewModelFactoryModule;
import com.example.unsplashphotoapp.ui.gallery.GalleryFragment;
import com.example.unsplashphotoapp.ui.profile.ProfileFragment;
import com.example.unsplashphotoapp.ui.saves.SavesFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        RetrofitModule.class,
        ViewModelFactoryModule.class,
        UnsplashViewModelsModule.class,
        RoomModule.class
})
public interface AppComponent {

    void inject(GalleryFragment galleryFragment);

    void inject(ProfileFragment profileFragment);

    void inject(SavesFragment savesFragment);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();

    }

}
