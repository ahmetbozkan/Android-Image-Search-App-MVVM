package com.example.unsplashphotoapp.api.unsplash;

import com.example.unsplashphotoapp.data.submodels.UnsplashUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UnsplashApi {

    @Headers("Authorization: Client-ID pLyAztVcmazvRper9FG4QElxKReUwGS4uH9oXjAX1xQ")
    @GET("photos")
    Call<List<UnsplashResponse>> getPhotos(
            @Query("per_page") Integer per_page
    );

    @Headers("Authorization: Client-ID pLyAztVcmazvRper9FG4QElxKReUwGS4uH9oXjAX1xQ")
    @GET("search/photos")
    Call<SearchResponse> searchPhotos(
            @Query("query") String query,
            @Query("per_page") Integer per_page
    );

    //Later, save the liked photos into sqlite database with room.
    @POST("photos/{id}/like")
    Call<UnsplashResponse> likePhoto(
            @Path("id") String id,
            @Header("Authorization") String authHeader
    );

    @GET("me")
    Call<UnsplashUser> getCurrentUser(
            @Header("Authorization") String authHeader
    );

    @PUT("me")
    Call<UnsplashUser> updateProfile(
            @Header("Authorization") String authHeader,
            @Query("username") String username
    );

}
