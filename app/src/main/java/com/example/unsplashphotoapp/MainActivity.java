package com.example.unsplashphotoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.example.unsplashphotoapp.di.AppComponent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static androidx.navigation.ui.NavigationUI.navigateUp;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    private AppComponent appComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appComponent = ((BaseApplication) getApplication()).getAppComponent();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navHostFragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(
                bottomNavigationView,
                navController
        );

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {//Do nothing when item is reselected.
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.galleryFragment &&
                    destination.getId() != R.id.savesFragment && destination.getId() != R.id.profileFragment) {

                bottomNavigationView.setVisibility(View.GONE);
            } else {
                if (bottomNavigationView.getVisibility() == View.GONE) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}