package com.yarik.photogallery.photo;

import android.support.annotation.NonNull;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.mvp.Presenter;

import java.util.List;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 16/02/17.
 */

public class PhotoDetailPresenter extends Presenter<IPhotoDetailView> {

    private List<Photo> mPhotos;
    private int         mSelectedPosition;

    public PhotoDetailPresenter(@NonNull final GalleryContext context) {
        super(context);
    }

    public void initPresenter(@NonNull final List<Photo> photos, final int selectedPosition) {
        mPhotos = photos;
        mSelectedPosition = selectedPosition;
    }

    @Override
    protected void onViewAttached() {
        getView().updateView(mPhotos, mSelectedPosition);
    }

    @Override
    protected void onViewDetached() {}
}
