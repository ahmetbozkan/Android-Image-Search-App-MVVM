package com.example.unsplashphotoapp.ui.toplevel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import com.example.unsplashphotoapp.MainActivity;
import com.example.unsplashphotoapp.R;
import com.example.unsplashphotoapp.api.unsplash.UnsplashResponse;
import com.example.unsplashphotoapp.ui.photo.UnsplashViewModel;
import com.example.unsplashphotoapp.ui.util.UnsplashPhotoAdapter;

public class SearchFragment extends Fragment implements UnsplashPhotoAdapter.OnPhotoClickListener {

    private UnsplashPhotoAdapter adapter;
    private UnsplashViewModel viewModel;

    private RecyclerView recyclerView;
    private ProgressBar progress_loading;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view);

        viewModel.searchPhotos().observe(getViewLifecycleOwner(), searchResponse -> {
            view.findViewById(R.id.text_view_search_placeholder).setVisibility(View.GONE);

            if (searchResponse.getResults().size() == 0) {
                Toast.makeText(requireContext(), "No results.", Toast.LENGTH_SHORT).show();
                progress_loading.setVisibility(View.GONE);

                adapter.submitList(searchResponse.getResults());
            }
            else {
                adapter.submitList(searchResponse.getResults());
                progress_loading.setVisibility(View.GONE);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        viewModel = new ViewModelProvider(requireActivity())
                .get(UnsplashViewModel.class);


        buildRecyclerView(root);
        progress_loading = root.findViewById(R.id.progress_loading_photos);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchView searchView = (SearchView) menu.findItem(R.id.options_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.getQUERY_STRING().setValue(query);

                progress_loading.setVisibility(View.VISIBLE);
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

    private void buildRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_search);
        recyclerView.setHasFixedSize(true);

        adapter = new UnsplashPhotoAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void setToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.homeFragment,
                        R.id.searchFragment,
                        R.id.profileFragment
                ).build();

        Toolbar toolbar = view.findViewById(R.id.toolbar_search);
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public void onPhotoClick(UnsplashResponse unsplashResponse, View view) {
        NavDirections action = SearchFragmentDirections
                .actionSearchFragmentToDetailsFragment(unsplashResponse);

        Navigation
                .findNavController(view).navigate(action);
    }
}
