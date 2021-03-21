package com.example.unsplashphotoapp.di.modules;

import android.app.Application;

import androidx.room.Room;

import com.example.unsplashphotoapp.db.UnsplashPhotoDao;
import com.example.unsplashphotoapp.db.UnsplashPhotoDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class RoomModule {

    @Provides
    @Singleton
    static UnsplashPhotoDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, UnsplashPhotoDatabase.class, "saved_images_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    static UnsplashPhotoDao provideDao(UnsplashPhotoDatabase unsplashPhotoDatabase) {
        return unsplashPhotoDatabase.getUnsplashDao();
    }

}
