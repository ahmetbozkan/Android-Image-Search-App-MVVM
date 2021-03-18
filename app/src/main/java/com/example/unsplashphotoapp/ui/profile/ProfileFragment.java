package com.example.unsplashphotoapp.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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
import com.example.unsplashphotoapp.BaseApplication;
import com.example.unsplashphotoapp.MainActivity;
import com.example.unsplashphotoapp.R;
import com.example.unsplashphotoapp.ui.gallery.GalleryViewModel;
import com.example.unsplashphotoapp.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private static final String CLIENT_ACCESS_KEY = "pLyAztVcmazvRper9FG4QElxKReUwGS4uH9oXjAX1xQ";
    private static final String CLIENT_SECRET_KEY = "Ju4iXTZAjr-GMqbQIGZsBcaDUhe0A-sK_N8TrEX7rwE";
    private static final String REDIRECT_URI = "com.example.unsplashphotoapp://callback";

    private static final String AUTH_URL = "https://unsplash.com/oauth/authorize" +
            "?client_id=" + CLIENT_ACCESS_KEY +
            "&redirect_uri=" + REDIRECT_URI +
            "&response_type=code" + "&scope=public" + "+read_user" + "+write_user" +
            "+read_photos" + "+write_likes" + "+write_followers" + "+read_collections";

    private String currentUsername = "";

    private ImageView image_userImage;

    private EditText tv_username;
    private TextView tv_name;
    private TextView tv_totalLikes;

    private Button button_update;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    private OAuthViewModel oAuthViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view);

        Log.d(TAG, "onViewCreated: View Model HashCode: " + oAuthViewModel);

        loadCurrentUser(view);

        button_update.setOnClickListener(v -> {
            if (tv_username.getText().toString().equals(currentUsername)) {
                Toast.makeText(requireContext(), "You cannot use the same username.", Toast.LENGTH_SHORT).show();
            } else {
                oAuthViewModel.updateProfile("Bearer " + oAuthViewModel.getAccessToken()
                        .getAccess_token(), tv_username.getText().toString());
                tv_username.clearFocus();
            }

        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        init(root);

        ((BaseApplication) getActivity().getApplication()).getAppComponent().inject(this);

        oAuthViewModel = new ViewModelProvider(requireActivity(), viewModelProviderFactory)
                .get(OAuthViewModel.class);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.login_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.option_login) {
            // Login with Unsplash Account and get Access Token
            if (oAuthViewModel.getAccessToken() == null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL));
                startActivity(intent);
            } else {
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

            oAuthViewModel.postAccessToken(CLIENT_ACCESS_KEY, CLIENT_SECRET_KEY, REDIRECT_URI, code, "authorization_code");
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadCurrentUser(View view) {
        if (oAuthViewModel.getAccessToken() != null) {
            String access_token = oAuthViewModel.getAccessToken()
                    .getAccess_token();

            Log.d(TAG, "loadCurrentUser: ACCESS TOKEN: " + access_token);

            oAuthViewModel.getCurrentUser("Bearer " + access_token).observe(getViewLifecycleOwner(), unsplashUser -> {

                if (unsplashUser != null) {
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
                                    button_update.setVisibility(View.VISIBLE);
                                    view.findViewById(R.id.text_view_username_placeholder).setVisibility(View.VISIBLE);
                                    return false;
                                }
                            })
                            .into(image_userImage);

                    tv_username.setText("" + unsplashUser.getUsername());
                    tv_name.setText("" + unsplashUser.getName());
                    tv_totalLikes.setText("Total Likes: " + unsplashUser.getTotal_likes());
                    currentUsername = unsplashUser.getUsername();
                } else {
                    Log.d(TAG, "loadCurrentUser: User is null");
                }

            });
        } else {
            view.findViewById(R.id.progress_loading_profile).setVisibility(View.GONE);
            Log.d(TAG, "loadCurrentUser: ACCESS TOKEN NULL: ");
            Toast.makeText(requireContext(), "You have to login to display your profile.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.galleryFragment,
                        R.id.savesFragment,
                        R.id.profileFragment
                ).build();

        Toolbar toolbar = view.findViewById(R.id.toolbar_profile);
        ((MainActivity) requireActivity()).setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    private void init(View view) {
        image_userImage = view.findViewById(R.id.image_view_user_photo);

        tv_username = view.findViewById(R.id.text_view_username);
        tv_name = view.findViewById(R.id.text_view_name);
        tv_totalLikes = view.findViewById(R.id.text_view_total_likes);

        button_update = view.findViewById(R.id.button_update);
    }
}
