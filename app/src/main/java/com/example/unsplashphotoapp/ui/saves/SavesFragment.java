package com.example.unsplashphotoapp.ui.saves;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unsplashphotoapp.BaseApplication;
import com.example.unsplashphotoapp.MainActivity;
import com.example.unsplashphotoapp.R;
import com.example.unsplashphotoapp.data.entities.UnsplashPhoto;
import com.example.unsplashphotoapp.ui.gallery.GalleryViewModel;
import com.example.unsplashphotoapp.util.SavesAdapter;
import com.example.unsplashphotoapp.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

public class SavesFragment extends Fragment implements SavesAdapter.OnItemClickListener {
    private static final String TAG = "SearchFragment";

    private SavesAdapter savesAdapter;

    @Inject
    ViewModelProviderFactory factory;
    private SavesViewModel savesViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view);

        savesViewModel = new ViewModelProvider(requireActivity(), factory).get(SavesViewModel.class);
        savesViewModel.getAllSaves().observe(requireActivity(), unsplashPhotos -> {
            if (unsplashPhotos != null) {
                savesAdapter.submitList(unsplashPhotos);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_saves, container, false);

        buildRecyclerView(root);

        ((BaseApplication) requireActivity().getApplication())
                .getAppComponent()
                .inject(this);

        return root;
    }

    private void setToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.galleryFragment,
                        R.id.savesFragment,
                        R.id.profileFragment
                ).build();

        Toolbar toolbar = view.findViewById(R.id.toolbar_search);
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    private void buildRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_saves);
        recyclerView.setHasFixedSize(true);

        savesAdapter = new SavesAdapter(this);
        recyclerView.setAdapter(savesAdapter);
    }

    @Override
    public void onSaveClick(UnsplashPhoto unsplashPhoto) {
        savesViewModel.deletePhoto(unsplashPhoto);
    }
}
