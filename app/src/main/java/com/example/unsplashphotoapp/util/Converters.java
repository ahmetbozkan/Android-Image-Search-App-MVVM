package com.example.unsplashphotoapp.util;

import androidx.room.TypeConverter;

import com.example.unsplashphotoapp.data.submodels.UnsplashPhotoUrls;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;
import com.google.gson.Gson;

public class Converters {

    @TypeConverter
    public String userToJson(UnsplashUser user) {
        return new Gson()
                .toJson(user);
    }

    @TypeConverter
    public UnsplashUser userFromJson(String json) {
        return new Gson()
                .fromJson(json, UnsplashUser.class);
    }

    @TypeConverter
    public String urlsToJson(UnsplashPhotoUrls urls) {
        return new Gson()
                .toJson(urls);
    }

    @TypeConverter
    public UnsplashPhotoUrls urlsFromJson(String json) {
        return new Gson()
                .fromJson(json, UnsplashPhotoUrls.class);
    }
}
