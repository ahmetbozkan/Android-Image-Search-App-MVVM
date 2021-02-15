package com.example.unsplashphotoapp.data.submodels;

import java.io.Serializable;

public class UnsplashUser implements Serializable {

    private String id;
    private String username;
    private String name;
    private int total_likes;
    private UserProfileImage profile_image;

    public UnsplashUser(String id, String username, String name, int total_likes, UserProfileImage profile_image) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.total_likes = total_likes;
        this.profile_image = profile_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(int total_likes) {
        this.total_likes = total_likes;
    }

    public UserProfileImage getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(UserProfileImage profile_image) {
        this.profile_image = profile_image;
    }
}
