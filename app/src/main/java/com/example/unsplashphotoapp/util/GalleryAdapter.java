package com.example.unsplashphotoapp.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.unsplashphotoapp.R;
import com.example.unsplashphotoapp.api.unsplash.UnsplashResponse;

public class GalleryAdapter extends ListAdapter<UnsplashResponse, GalleryAdapter.UnsplashViewHolder> {

    private final OnPhotoClickListener onPhotoClickListener;

    private static final DiffUtil.ItemCallback<UnsplashResponse> DIFF_CALLBACK = new DiffUtil.ItemCallback<UnsplashResponse>() {
        @Override
        public boolean areItemsTheSame(@NonNull UnsplashResponse oldItem, @NonNull UnsplashResponse newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UnsplashResponse oldItem, @NonNull UnsplashResponse newItem) {
            return oldItem.getId().equals(newItem.getId()) &&
                    oldItem.getCreated_at().equals(newItem.getCreated_at()) &&
                    oldItem.getLikes() == newItem.getLikes() &&
                    oldItem.getUrls().getFull().equals(newItem.getUrls().getFull()) &&
                    oldItem.getUser().getId().equals(newItem.getUser().getId());
        }
    };

    public GalleryAdapter(OnPhotoClickListener onPhotoClickListener) {
        super(DIFF_CALLBACK);

        this.onPhotoClickListener = onPhotoClickListener;
    }

    @NonNull
    @Override
    public UnsplashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_unsplash_photo, parent, false);

        return new UnsplashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnsplashViewHolder holder, int position) {
        UnsplashResponse currentPhoto = getItem(position);

        Glide.with(holder.itemView.getContext())
                .load(currentPhoto.getUrls().getRegular())
                .centerCrop()
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image_view);


        Glide.with(holder.itemView.getContext())
                .load(R.drawable.ic_saves_unsaved)
                .centerCrop()
                .error(R.drawable.ic_error)
                .into(holder.image_view_save);


        holder.tv_username.setText(currentPhoto.getUser().getName());
    }

    public class UnsplashViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView image_view;
        private final TextView tv_username;
        private final ImageView image_view_save;

        public UnsplashViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view = itemView.findViewById(R.id.image_view_photo);
            tv_username = itemView.findViewById(R.id.text_view_username);
            image_view_save = itemView.findViewById(R.id.image_view_save_photo);

            itemView.setOnClickListener(this);
            image_view_save.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == itemView.getId()) {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    UnsplashResponse unsplashPhoto = getItem(position);

                    if (unsplashPhoto != null) {
                        onPhotoClickListener.onPhotoClick(unsplashPhoto, v);
                    }
                }
            } else {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    UnsplashResponse unsplashPhoto = getItem(position);

                    if (unsplashPhoto != null) {
                        onPhotoClickListener.onSaveClick(unsplashPhoto);
                    }
                }
            }

        }
    }

    public interface OnPhotoClickListener {
        void onPhotoClick(UnsplashResponse unsplashResponse, View view);

        void onSaveClick(UnsplashResponse unsplashResponse);
    }
}
