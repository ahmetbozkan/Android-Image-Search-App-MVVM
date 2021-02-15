package com.example.unsplashphotoapp.ui.toplevel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unsplashphotoapp.MainActivity;
import com.example.unsplashphotoapp.R;
import com.example.unsplashphotoapp.api.unsplash.UnsplashResponse;
import com.example.unsplashphotoapp.ui.util.UnsplashPhotoAdapter;
import com.example.unsplashphotoapp.ui.photo.UnsplashViewModel;


public class HomeFragment extends Fragment implements UnsplashPhotoAdapter.OnPhotoClickListener {
    private static final String TAG = "HomeFragment";

    private static final String CLIENT_ACCESS_KEY = "pLyAztVcmazvRper9FG4QElxKReUwGS4uH9oXjAX1xQ";
    private static final String CLIENT_SECRET_KEY = "Ju4iXTZAjr-GMqbQIGZsBcaDUhe0A-sK_N8TrEX7rwE";
    private static final String REDIRECT_URI = "com.example.unsplashphotoapp://callback";

    private static final String AUTH_URL = "https://unsplash.com/oauth/authorize" +
            "?client_id=" + CLIENT_ACCESS_KEY +
            "&redirect_uri=" + REDIRECT_URI +
            "&response_type=code" + "&scope=public" + "+read_user" + "+write_user" +
            "+read_photos" + "+write_likes" + "+write_followers" + "+read_collections";

    private UnsplashPhotoAdapter unsplashPhotoAdapter;
    private UnsplashViewModel unsplashViewModel;

    private RecyclerView recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);

        unsplashViewModel = new ViewModelProvider(requireActivity())
                .get(UnsplashViewModel.class);

        unsplashViewModel.getPhotoList().observe(getViewLifecycleOwner(), unsplashResponses -> {
            unsplashPhotoAdapter.submitList(unsplashResponses);

            view.findViewById(R.id.progress_loading_photos).setVisibility(View.GONE);
        });

        Log.d(TAG, "onViewCreated: AUTHORIZATION URL: " + AUTH_URL);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        buildRecyclerView(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.option_refresh) {
            unsplashViewModel.refresh();
            unsplashPhotoAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);

            return true;

        } else if (item.getItemId() == R.id.option_login) {
            // Login with Unsplash Account and get Access Token
            if (unsplashViewModel.getAccessToken() == null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL));
                startActivity(intent);
            }
            else {
                Toast.makeText(requireContext(), "You've already logged in.", Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        Uri uri = requireActivity().getIntent().getData();

        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            Log.d(TAG, "onResume: Uri not null: " + uri.toString());

            String code = uri.getQueryParameter("code");

            unsplashViewModel.postAccessToken(CLIENT_ACCESS_KEY, CLIENT_SECRET_KEY, REDIRECT_URI, code, "authorization_code");
        }


    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.homeFragment,
                        R.id.searchFragment,
                        R.id.profileFragment
                ).build();


        Toolbar toolbar = view.findViewById(R.id.toolbar_home);
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    private void buildRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_photos);
        recyclerView.setHasFixedSize(true);

        unsplashPhotoAdapter = new UnsplashPhotoAdapter(this);
        recyclerView.setAdapter(unsplashPhotoAdapter);
    }

    @Override
    public void onPhotoClick(UnsplashResponse unsplashResponse, View view) {
        Log.d(TAG, "onPhotoClick: Clicked");

        NavDirections action = HomeFragmentDirections
                .actionHomeFragmentToDetailsFragment(unsplashResponse);

        Navigation
                .findNavController(view)
                .navigate(action);
    }
}
