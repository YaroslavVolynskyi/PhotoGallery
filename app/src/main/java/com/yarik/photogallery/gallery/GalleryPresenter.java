package com.yarik.photogallery.gallery;

import android.support.annotation.NonNull;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.mvp.Presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class GalleryPresenter extends Presenter<IGalleryView> {

    private int         mPhotosAded;
    private List<Photo> mPhotos = new ArrayList<>();
    private int         mLastLoadedPage;

    public GalleryPresenter(@NonNull final GalleryContext context) {
        super(context);
    }

    @Override
    protected void onViewAttached() {
        if (!mPhotos.isEmpty()) {
            getView().addPhotos(mPhotos, mPhotosAded, mLastLoadedPage, true);
        }
    }

    @Override
    protected void onViewDetached() {}

    public void newPhotosReceived(@NonNull final List<Photo> photos, final int lastLoadedPage) {
        mPhotosAded += photos.size();
        mPhotos.addAll(photos);
        mLastLoadedPage = lastLoadedPage;
        getView().addPhotos(photos, mPhotosAded, mLastLoadedPage, false);
    }
}
