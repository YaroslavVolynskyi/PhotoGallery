package com.yarik.photogallery.photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yarik.photogallery.R;
import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.api.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 17/02/17.
 */

public class PhotoDetailRecyclerViewAdapter extends RecyclerView.Adapter<PhotoDetailViewHolder> {

    @NonNull final private List<Photo> mPhotos = new ArrayList<>();
    @NonNull final Context             mContext;

    public PhotoDetailRecyclerViewAdapter(@NonNull final Context context) {
        mContext = context;
    }

    public void setPhotos(@NonNull final List<Photo> photos) {
        mPhotos.clear();
        mPhotos.addAll(photos);
    }

    @Override
    public PhotoDetailViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_detail, parent, false);
        return new PhotoDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoDetailViewHolder holder, final int position) {
        if (!mPhotos.isEmpty()) {
            final Photo photo = mPhotos.get(position);
            if (photo != null) {
                if (holder.mImageView != null) {
                    Picasso.with(mContext).load(photo.getImageUrl()).into(holder.mImageView);
                }
                final User user = photo.getUser();
                final String firstName = user.getFirstname();
                final String lastName = user.getLastname();
                final String fullName = (firstName != null ? firstName + " " : "") + (lastName != null ? lastName : "");
                holder.mAuthorNameTextView.setText(mContext.getString(R.string.author_xs, fullName));
                holder.mAuthorNameTextView.setVisibility(!fullName.isEmpty() ? View.VISIBLE : View.GONE);

                holder.mPhotoNameTextView.setText(mContext.getString(R.string.photo_xs, photo.getName()));
                holder.mPhotoNameTextView.setVisibility(photo.getName() != null ? View.VISIBLE : View.GONE);

                holder.mCameraTextView.setText(mContext.getString(R.string.camera_xs, photo.getCamera()));
                holder.mCameraTextView.setVisibility(photo.getCamera() != null ? View.VISIBLE : View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }
}
