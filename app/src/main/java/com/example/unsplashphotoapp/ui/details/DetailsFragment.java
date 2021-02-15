package com.example.unsplashphotoapp.ui.details;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.unsplashphotoapp.R;
import com.example.unsplashphotoapp.api.unsplash.UnsplashResponse;
import com.example.unsplashphotoapp.data.submodels.UnsplashUser;
import com.example.unsplashphotoapp.ui.photo.UnsplashViewModel;
import com.google.android.material.imageview.ShapeableImageView;

public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";

    private ImageView image_photo;
    private ShapeableImageView image_userImage;
    private TextView text_view_username;
    private TextView text_view_photo_description, text_view_created_at, text_view_likes;

    private Button button_like;

    private UnsplashResponse photo;
    private UnsplashUser author;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view);
        setCurrentPhoto(view);

        UnsplashViewModel unsplashViewModel = new ViewModelProvider(requireActivity())
                .get(UnsplashViewModel.class);

        view.findViewById(R.id.layout_user_info).setOnClickListener(v -> {
            NavDirections action = DetailsFragmentDirections
                    .actionDetailsFragmentToUserProfileFragment(author);

            Navigation
                    .findNavController(view)
                    .navigate(action);
        });

        button_like.setOnClickListener(v -> {
            if (unsplashViewModel.getAccessToken() == null) {
                Toast.makeText(requireContext(), "You have to login first.", Toast.LENGTH_SHORT).show();
                return;
            }

            String access_token = unsplashViewModel.getAccessToken().getAccess_token();
            unsplashViewModel.likePhoto(photo.getId(), "Bearer " + access_token);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        init(rootView);

        return rootView;
    }

    private void setToolbar(View view) {
        NavController navController = Navigation.findNavController(view);

        Toolbar toolbar = view.findViewById(R.id.toolbar_details);
        NavigationUI.setupWithNavController(toolbar, navController);

    }

    @SuppressLint("SetTextI18n")
    private void setCurrentPhoto(View view) {
        photo = DetailsFragmentArgs.fromBundle(getArguments())
                .getUnsplashPhoto();

        Log.d(TAG, "setCurrentPhoto: Photo ID: " + photo.getId());

        author = photo.getUser();

        Glide.with(this)
                .load(photo.getUrls().getFull())
                .centerCrop()
                .error(R.drawable.ic_error)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        view.findViewById(R.id.progress_loading_photos).setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        view.findViewById(R.id.progress_loading_photos).setVisibility(View.GONE);

                        text_view_username.setVisibility(View.VISIBLE);
                        text_view_created_at.setVisibility(View.VISIBLE);
                        text_view_likes.setVisibility(View.VISIBLE);
                        image_userImage.setVisibility(View.VISIBLE);
                        button_like.setVisibility(View.VISIBLE);

                        if (photo.getDescription() != null) {
                            text_view_photo_description.setVisibility(View.VISIBLE);
                        }

                        return false;
                    }
                })
                .into(image_photo);

        Glide.with(this)
                .load(author.getProfile_image().getSmall())
                .centerCrop()
                .into(image_userImage);

        text_view_username.setText(author.getName());
        text_view_photo_description.setText(photo.getDescription());
        text_view_created_at.setText("Created at: " + photo.getCreated_at());
        text_view_likes.setText("Likes: " + photo.getLikes());
    }

    private void init(View view) {
        image_photo = view.findViewById(R.id.image_view_photo);
        image_userImage = view.findViewById(R.id.image_view_user_photo);

        text_view_username = view.findViewById(R.id.text_view_username);
        text_view_photo_description = view.findViewById(R.id.text_view_description);
        text_view_created_at = view.findViewById(R.id.text_view_created_at);
        text_view_likes = view.findViewById(R.id.text_view_likes);

        button_like = view.findViewById(R.id.button_like);
    }
}
