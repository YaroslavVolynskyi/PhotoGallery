package com.yarik.photogallery.photo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarik.photogallery.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 17/02/17.
 */

public class PhotoDetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photoImageView) ImageView    mImageView;
    @BindView(R.id.photoNameTextView) TextView  mPhotoNameTextView;
    @BindView(R.id.authorNameTextView) TextView mAuthorNameTextView;
    @BindView(R.id.cameraTextView) TextView     mCameraTextView;

    public PhotoDetailViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
