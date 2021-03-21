package com.example.unsplashphotoapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.unsplashphotoapp.data.entities.UnsplashPhoto;
import com.example.unsplashphotoapp.util.Converters;

@Database(entities = UnsplashPhoto.class, version = 1)
@TypeConverters(Converters.class)
public abstract class UnsplashPhotoDatabase extends RoomDatabase {

    public abstract UnsplashPhotoDao getUnsplashDao();
}
