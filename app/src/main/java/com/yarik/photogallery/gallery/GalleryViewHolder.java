package com.yarik.photogallery.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.yarik.photogallery.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class GalleryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_imageview)
    ImageView mImageView;

    public GalleryViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
