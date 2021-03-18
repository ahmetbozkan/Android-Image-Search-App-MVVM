package com.example.unsplashphotoapp;

import android.app.Application;

import com.example.unsplashphotoapp.di.AppComponent;
import com.example.unsplashphotoapp.di.DaggerAppComponent;

public class BaseApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
