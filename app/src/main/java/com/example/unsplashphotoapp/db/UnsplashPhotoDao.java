package com.example.unsplashphotoapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.unsplashphotoapp.data.entities.UnsplashPhoto;

import java.util.List;

@Dao
public interface UnsplashPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSavedPhoto(UnsplashPhoto unsplashPhoto);

    @Delete
    void deletePhoto(UnsplashPhoto unsplashPhoto);

    @Query("SELECT * FROM saves_table WHERE is_saved = 1")
    LiveData<List<UnsplashPhoto>> getSavedPhotos();

}
