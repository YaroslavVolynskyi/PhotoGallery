package com.yarik.photogallery.gallery;

import android.support.annotation.NonNull;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.mvp.PresenterActivity;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class GalleryActivity extends PresenterActivity<GalleryPresenter, IGalleryView> implements IGalleryView  {

    @NonNull
    @Override
    protected GalleryPresenter onCreatePresenter(@NonNull final GalleryContext context) {
        return new GalleryPresenter(context);
    }

    @Override
    protected IGalleryView getPresenterView() {
        return this;
    }
}
