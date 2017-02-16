package com.yarik.photogallery.photo;

import android.support.annotation.NonNull;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.mvp.Presenter;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 16/02/17.
 */

public class PhotoDetailPresenter extends Presenter<IPhotoDetailView> {

    public PhotoDetailPresenter(@NonNull final GalleryContext context) {
        super(context);
    }

    @Override
    protected void onViewAttached() {

    }

    @Override
    protected void onViewDetached() {

    }
}
