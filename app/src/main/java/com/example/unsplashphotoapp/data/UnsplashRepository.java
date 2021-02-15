package com.example.unsplashphotoapp.data;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unsplashphotoapp.api.unsplash.RetrofitClient;
import com.example.unsplashphotoapp.api.unsplash.SearchResponse;
import com.example.unsplashphotoapp.api.unsplash.UnsplashApi;
import com.example.unsplashphotoapp.api.unsplash.UnsplashResponse;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UnsplashRepository {
    private static final String TAG = "UnsplashRepository";

    private static UnsplashRepository mInstance;
    private final UnsplashApi unsplashApi;

    private final MutableLiveData<List<UnsplashResponse>> photoList = new MutableLiveData<>();

    private final MutableLiveData<SearchResponse> searchList = new MutableLiveData<>();

    private final MutableLiveData<UnsplashUser> currentUser = new MutableLiveData<>();

    private final Application application;

    private UnsplashRepository(Application application) {
        Retrofit retrofit = RetrofitClient.newInstance();
        unsplashApi = retrofit.create(UnsplashApi.class);

        this.application = application;

        loadPhotos();
    }

    public static synchronized UnsplashRepository getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new UnsplashRepository(application);
        }

        return mInstance;
    }

    public LiveData<List<UnsplashResponse>> getPhotos() {
        return photoList;
    }

    private void loadPhotos() {
        Call<List<UnsplashResponse>> unsplashCall = unsplashApi.getPhotos(25);
        unsplashCall.enqueue(new Callback<List<UnsplashResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<UnsplashResponse>> call, @NonNull Response<List<UnsplashResponse>> response) {
                if (response.body() == null) return;

                List<UnsplashResponse> photos = response.body();
                photoList.setValue(photos);
            }

            @Override
            public void onFailure(@NonNull Call<List<UnsplashResponse>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Fail to fetch:" + t.getMessage());
            }
        });
    }

    public void refresh() {
        loadPhotos();
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

                SearchResponse photoList = response.body();
                searchList.setValue(photoList);
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Error when searching photos: " + t.getMessage());
            }
        });

        return searchList;
    }

    private void loadCurrentUser(String header) {
        Call<UnsplashUser> currentUserCall = unsplashApi.getCurrentUser(header);
        currentUserCall.enqueue(new Callback<UnsplashUser>() {
            @Override
            public void onResponse(@NonNull Call<UnsplashUser> call, @NonNull Response<UnsplashUser> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                UnsplashUser user = response.body();
                currentUser.setValue(user);
                Log.d(TAG, "onResponse: Current User: " + user.getName());
            }

            @Override
            public void onFailure(@NonNull Call<UnsplashUser> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: Error on loading current user: " + t.getMessage());
            }
        });
    }

    public LiveData<UnsplashUser> getCurrentUser(String header) {
        if (currentUser != null) {
            loadCurrentUser(header);
        }

        return currentUser;
    }

    public void likePhoto(String id, String header) {
        Call<UnsplashResponse> likeCall = unsplashApi.likePhoto(id, header);
        likeCall.enqueue(new Callback<UnsplashResponse>() {
            @Override
            public void onResponse(@NonNull Call<UnsplashResponse> call, @NonNull Response<UnsplashResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Response is not successfull. Code: " + response.code());
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
}
