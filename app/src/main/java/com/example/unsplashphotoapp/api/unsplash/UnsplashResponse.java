package com.example.unsplashphotoapp.api.unsplash;

import com.example.unsplashphotoapp.data.submodels.UnsplashPhotoUrls;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;

import java.io.Serializable;

public class UnsplashResponse implements Serializable {

    private String id;
    private String created_at;
    private String description;
    private int likes;

    private UnsplashUser user;
    private UnsplashPhotoUrls urls;

    public UnsplashResponse(String id, String created_at, String description, int likes, UnsplashUser user, UnsplashPhotoUrls urls) {
        this.id = id;
        this.created_at = created_at;
        this.description = description;
        this.likes = likes;
        this.user = user;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
