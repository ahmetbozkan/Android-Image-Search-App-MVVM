package com.example.unsplashphotoapp.ui.details;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.unsplashphotoapp.R;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;
import com.example.unsplashphotoapp.data.submodels.UserProfileImage;
import com.example.unsplashphotoapp.ui.details.UserProfileFragmentArgs;

public class UserProfileFragment extends Fragment {
    private static final String TAG = "UserProfileFragment";

    private ImageView image_userImage;

    private TextView tv_username;
    private TextView tv_name;
    private TextView tv_totalLikes;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolbar(view);
        setAuthor(view);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        init(rootView);

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void setAuthor(View view) {
        UnsplashUser author = UserProfileFragmentArgs.fromBundle(getArguments())
                .getUnsplashUser();

        UserProfileImage userImage = author.getProfile_image();

        Log.d(TAG, "setAuthor: " + author.getUsername() + " \n" +
                author.getName());

        tv_username.setText("Username: " + author.getUsername());
        tv_name.setText("Name: " + author.getName());
        tv_totalLikes.setText("Total Likes: " + author.getTotal_likes());

        Glide.with(requireContext())
                .load(userImage.getMedium())
                .centerCrop()
                .error(R.drawable.ic_error)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        view.findViewById(R.id.progress_loading_author).setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Error on loading user.", Toast.LENGTH_SHORT).show();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        view.findViewById(R.id.progress_loading_author).setVisibility(View.GONE);

                        tv_username.setVisibility(View.VISIBLE);
                        tv_name.setVisibility(View.VISIBLE);
                        tv_totalLikes.setVisibility(View.VISIBLE);

                        return false;
                    }
                })
                .into(image_userImage);


    }

    private void setToolbar(View view) {
        NavController navController = Navigation.findNavController(view);

        Toolbar toolbar = view.findViewById(R.id.toolbar_user_profile);
        NavigationUI.setupWithNavController(toolbar, navController);
    }

    private void init(View view) {
        image_userImage = view.findViewById(R.id.image_view_user_photo);

        tv_username = view.findViewById(R.id.text_view_username);
        tv_name = view.findViewById(R.id.text_view_name);
        tv_totalLikes = view.findViewById(R.id.text_view_total_likes);
    }
}
