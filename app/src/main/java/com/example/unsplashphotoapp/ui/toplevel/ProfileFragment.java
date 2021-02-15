package com.example.unsplashphotoapp.ui.toplevel;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.unsplashphotoapp.R;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;
import com.example.unsplashphotoapp.ui.photo.UnsplashViewModel;

public class ProfileFragment extends Fragment {

    private ImageView image_userImage;

    private TextView tv_username;
    private TextView tv_name;
    private TextView tv_totalLikes;

    private UnsplashViewModel unsplashViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view);
        init(view);

        unsplashViewModel = new ViewModelProvider(requireActivity())
                .get(UnsplashViewModel.class);

        loadCurrentUser(view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        return root;
    }

    @SuppressLint("SetTextI18n")
    private void loadCurrentUser(View view) {
        if (unsplashViewModel.getAccessToken() != null) {
            String access_token = unsplashViewModel.getAccessToken()
                    .getAccess_token();

            unsplashViewModel.getCurrentUser("Bearer " + access_token).observe(getViewLifecycleOwner(), unsplashUser -> {

                Glide.with(requireActivity())
                        .load(unsplashUser.getProfile_image().getMedium())
                        .error(R.drawable.ic_error)
                        .centerCrop()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                view.findViewById(R.id.progress_loading_profile).setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                view.findViewById(R.id.progress_loading_profile).setVisibility(View.GONE);

                                tv_username.setVisibility(View.VISIBLE);
                                tv_name.setVisibility(View.VISIBLE);
                                tv_totalLikes.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(image_userImage);

                tv_username.setText("Username: " + unsplashUser.getUsername());
                tv_name.setText("Name: " + unsplashUser.getName());
                tv_totalLikes.setText("Total Likes: " + unsplashUser.getTotal_likes());
            });
        }

        else {
            view.findViewById(R.id.progress_loading_profile).setVisibility(View.GONE);
            Toast.makeText(requireContext(), "You have to login to display your profile.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.homeFragment,
                        R.id.searchFragment,
                        R.id.profileFragment
                ).build();

        Toolbar toolbar = view.findViewById(R.id.toolbar_profile);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    private void init(View view) {
        image_userImage = view.findViewById(R.id.image_view_user_photo);

        tv_username = view.findViewById(R.id.text_view_username);
        tv_name = view.findViewById(R.id.text_view_name);
        tv_totalLikes = view.findViewById(R.id.text_view_total_likes);
    }
}
