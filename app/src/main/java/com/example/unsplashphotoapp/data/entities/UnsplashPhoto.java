package com.example.unsplashphotoapp.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.unsplashphotoapp.data.submodels.UnsplashPhotoUrls;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;

@Entity(tableName = "saves_table")
public class UnsplashPhoto {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "created_at")
    private String created_at;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "likes")
    private int likes;

    @ColumnInfo(name = "is_saved")
    private boolean isSaved;

    @ColumnInfo(name = "user")
    private UnsplashUser user;

    @ColumnInfo(name = "urls")
    private UnsplashPhotoUrls urls;

    public UnsplashPhoto(@NonNull String id, String created_at, String description, int likes, boolean isSaved, UnsplashUser user, UnsplashPhotoUrls urls) {
        this.created_at = created_at;
        this.description = description;
        this.likes = likes;
        this.isSaved = isSaved;
        this.id = id;
        this.user = user;
        this.urls = urls;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public UnsplashUser getUser() {
        return user;
    }

    public void setUser(UnsplashUser user) {
        this.user = user;
    }

    public UnsplashPhotoUrls getUrls() {
        return urls;
    }

    public void setUrls(UnsplashPhotoUrls urls) {
        this.urls = urls;
    }
}
