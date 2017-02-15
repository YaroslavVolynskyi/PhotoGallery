package com.yarik.photogallery.gallery;

import android.support.annotation.NonNull;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.mvp.Presenter;
import com.yarik.photogallery.mvp.PresenterActivity;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class GalleryPresenter extends Presenter<IGalleryView> {

    public GalleryPresenter(@NonNull final GalleryContext context) {
        super(context);
    }

    @Override
    protected void onViewAttached() {
    }

    @Override
    protected void onViewDetached() {

    }
}
