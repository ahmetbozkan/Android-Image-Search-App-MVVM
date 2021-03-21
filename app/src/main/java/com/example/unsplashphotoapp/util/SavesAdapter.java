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
import com.example.unsplashphotoapp.data.entities.UnsplashPhoto;

public class SavesAdapter extends ListAdapter<UnsplashPhoto, SavesAdapter.SavesViewHolder> {

    private final OnItemClickListener onItemClickListener;

    private static final DiffUtil.ItemCallback<UnsplashPhoto> DIFF_CALLBACK = new DiffUtil.ItemCallback<UnsplashPhoto>() {
        @Override
        public boolean areItemsTheSame(@NonNull UnsplashPhoto oldItem, @NonNull UnsplashPhoto newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UnsplashPhoto oldItem, @NonNull UnsplashPhoto newItem) {
            return oldItem.getId().equals(newItem.getId()) &&
                    oldItem.getCreated_at().equals(newItem.getCreated_at()) &&
                    oldItem.getLikes() == newItem.getLikes() &&
                    oldItem.isSaved() == newItem.isSaved() &&
                    oldItem.getUser().getId().equals(newItem.getUser().getId());
        }
    };


    public SavesAdapter(OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);

        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SavesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_unsplash_photo, parent, false);

        return new SavesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SavesViewHolder holder, int position) {
        UnsplashPhoto currentPhoto = getItem(position);

        Glide.with(holder.itemView.getContext())
                .load(currentPhoto.getUrls().getRegular())
                .centerCrop()
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image_view_photo);

        if (currentPhoto.isSaved()) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_saves)
                    .centerCrop()
                    .error(R.drawable.ic_error)
                    .into(holder.image_view_save);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_saves_unsaved)
                    .centerCrop()
                    .error(R.drawable.ic_error)
                    .into(holder.image_view_save);
        }

        holder.text_view_username.setText(currentPhoto.getUser().getName());
    }

    public class SavesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView image_view_photo;
        private final TextView text_view_username;
        private final ImageView image_view_save;

        public SavesViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view_photo = itemView.findViewById(R.id.image_view_photo);
            text_view_username = itemView.findViewById(R.id.text_view_username);
            image_view_save = itemView.findViewById(R.id.image_view_save_photo);

            image_view_save.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                UnsplashPhoto unsplashPhoto = getItem(position);

                if (unsplashPhoto != null) {
                    onItemClickListener.onSaveClick(unsplashPhoto);
                }
            }

        }
    }

    public interface OnItemClickListener {
        void onSaveClick(UnsplashPhoto unsplashPhoto);
    }
}
