package com.example.unsplashphotoapp.data.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.loader.content.AsyncTaskLoader;

import com.example.unsplashphotoapp.api.oauth.AccessToken;
import com.example.unsplashphotoapp.api.unsplash.SearchResponse;
import com.example.unsplashphotoapp.api.unsplash.UnsplashApi;
import com.example.unsplashphotoapp.api.unsplash.UnsplashResponse;
import com.example.unsplashphotoapp.data.entities.UnsplashPhoto;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;
import com.example.unsplashphotoapp.db.UnsplashPhotoDao;
import com.example.unsplashphotoapp.db.UnsplashPhotoDatabase;
import com.google.gson.Gson;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class UnsplashRepository {
    private static final String TAG = "UnsplashRepository";

    private final UnsplashApi unsplashApi;
    private final MutableLiveData<SearchResponse> searchList = new MutableLiveData<>();

    private LiveData<List<UnsplashPhoto>> savesList;

    private final UnsplashPhotoDao dao;

    private final Application application;

    @Inject
    public UnsplashRepository(Application application, UnsplashApi unsplashApi, UnsplashPhotoDatabase database) {
        this.application = application;
        this.unsplashApi = unsplashApi;

        dao = database.getUnsplashDao();
    }

    public void refresh(String query) {
        searchPhotos(query);
    }

    public LiveData<SearchResponse> searchPhotos(String query) {
        Call<SearchResponse> getSearchResults = unsplashApi.searchPhotos(query, 25);
        getSearchResults.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                if (response.body() == null) {
                    searchList.setValue(null);
                    return;
                }

                SearchResponse photos = response.body();
                searchList.setValue(photos);
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Error when searching photos: " + t.getMessage());
            }
        });

        return searchList;
    }

    public void likePhoto(String id, String header) {
        Call<UnsplashResponse> likeCall = unsplashApi.likePhoto(id, header);
        likeCall.enqueue(new Callback<UnsplashResponse>() {
            @Override
            public void onResponse(@NonNull Call<UnsplashResponse> call, @NonNull Response<UnsplashResponse> response) {
                if (!response.isSuccessful() && response.body() == null) {
                    Log.d(TAG, "onResponse: Response is not successfull. Code: " + response.code());
                    return;
                }

                UnsplashResponse photo = response.body();

                Toast.makeText(application.getApplicationContext(), "You liked the photo.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: Liked photo: " + photo.getId());
            }

            @Override
            public void onFailure(@NonNull Call<UnsplashResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Error on liking the photo: " + t.getMessage());
            }
        });
    }

    public AccessToken getAccessToken() {
        SharedPreferences sharedPreferences = application.getSharedPreferences("ACCESS_TOKEN_PREFS", Context.MODE_PRIVATE);
        String tokenJson = sharedPreferences.getString("access_token_object", "null");

        if (tokenJson == null) {
            return null;
        } else {
            return new Gson().fromJson(
                    tokenJson,
                    AccessToken.class
            );
        }
    }

    public void insertSavedPhoto(UnsplashPhoto unsplashPhoto) {
        new InsertPhotoAsyncTask(dao).execute(unsplashPhoto);
    }

    public void deleteSavedPhoto(UnsplashPhoto unsplashPhoto) {
        new DeletePhotoAsyncTask(dao).execute(unsplashPhoto);
    }

    public LiveData<List<UnsplashPhoto>> getAllSaves() {
        if (savesList == null) {
            savesList = dao.getSavedPhotos();
        }

        return savesList;
    }

    private static class InsertPhotoAsyncTask extends AsyncTask<UnsplashPhoto, Void, Void> {

        private final UnsplashPhotoDao dao;

        public InsertPhotoAsyncTask(UnsplashPhotoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UnsplashPhoto... unsplashPhotos) {
            dao.insertSavedPhoto(unsplashPhotos[0]);
            return null;
        }
    }

    private static class DeletePhotoAsyncTask extends AsyncTask<UnsplashPhoto, Void, Void> {

        private final UnsplashPhotoDao dao;

        public DeletePhotoAsyncTask(UnsplashPhotoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UnsplashPhoto... unsplashPhotos) {
            dao.deletePhoto(unsplashPhotos[0]);
            return null;
        }
    }


}
