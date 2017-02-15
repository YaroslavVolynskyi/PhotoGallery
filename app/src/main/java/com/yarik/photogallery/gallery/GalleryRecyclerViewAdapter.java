package com.yarik.photogallery.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yarik.photogallery.R;
import com.yarik.photogallery.api.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    @NonNull private final Context     mContext;
    @NonNull private final List<Photo> mPhotos = new ArrayList<>();

    public GalleryRecyclerViewAdapter(@NonNull final Context context) {
        mContext = context;
    }

    public void addPhotos(@NonNull final List<Photo> photos) {
        mPhotos.addAll(photos);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {
        if (!mPhotos.isEmpty()) {
            final Photo photo = mPhotos.get(position);
            if (photo != null) {
                Picasso.with(mContext).load(photo.getImageUrl()).into(holder.mImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }
}