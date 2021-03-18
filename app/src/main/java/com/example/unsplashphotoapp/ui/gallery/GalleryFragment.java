package com.example.unsplashphotoapp.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unsplashphotoapp.BaseApplication;
import com.example.unsplashphotoapp.MainActivity;
import com.example.unsplashphotoapp.R;
import com.example.unsplashphotoapp.api.unsplash.UnsplashResponse;
import com.example.unsplashphotoapp.util.UnsplashPhotoAdapter;
import com.example.unsplashphotoapp.viewmodels.ViewModelProviderFactory;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;


public class GalleryFragment extends Fragment implements UnsplashPhotoAdapter.OnPhotoClickListener {
    private static final String TAG = "HomeFragment";

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private UnsplashPhotoAdapter unsplashPhotoAdapter;
    private GalleryViewModel galleryViewModel;

    private RecyclerView recyclerView;

    private ProgressBar progress_loadingPhotos;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);

        galleryViewModel.searchPhotos().observe(getViewLifecycleOwner(), unsplashResponses -> {
            List<UnsplashResponse> results =  unsplashResponses.getResults();

            if(results.size() == 0) {
                Toast.makeText(requireContext(), "No results found.", Toast.LENGTH_SHORT).show();
            }
            else {
                unsplashPhotoAdapter.submitList(results);
            }


            progress_loadingPhotos.setVisibility(View.GONE);
        });

        //Log.d(TAG, "onViewCreated: AUTHORIZATION URL: " + AUTH_URL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        buildRecyclerView(rootView);

        ((BaseApplication) getActivity().getApplication()).getAppComponent().inject(this);

        galleryViewModel = new ViewModelProvider(requireActivity(), viewModelProviderFactory)
                .get(GalleryViewModel.class);

        progress_loadingPhotos = rootView.findViewById(R.id.progress_loading_photos);

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
            galleryViewModel.refresh();
            unsplashPhotoAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);

            return true;
        }
        else if (item.getItemId() == R.id.options_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    galleryViewModel.setNewQuery(query);

                    progress_loadingPhotos.setVisibility(View.VISIBLE);
                    recyclerView.scrollToPosition(0);
                    searchView.clearFocus();

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.galleryFragment,
                        R.id.savesFragment,
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

        NavDirections action = GalleryFragmentDirections
                .actionHomeFragmentToDetailsFragment(unsplashResponse);

        Navigation
                .findNavController(view)
                .navigate(action);
    }

    public static class TimeOutAsyncTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<TextView> tv_timeOut;
        private final WeakReference<Button> button_refresh;
        private final WeakReference<ProgressBar> progress_loadingImages;

        public TimeOutAsyncTask(TextView timeOut, Button refresh, ProgressBar loadingImages) {
            tv_timeOut = new WeakReference<>(timeOut);
            button_refresh = new WeakReference<>(refresh);
            progress_loadingImages = new WeakReference<>(loadingImages);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "doInBackground: Error on thread sleep.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tv_timeOut.get().setVisibility(View.VISIBLE);
            button_refresh.get().setVisibility(View.VISIBLE);

            progress_loadingImages.get().setVisibility(View.GONE);
        }
    }
}
