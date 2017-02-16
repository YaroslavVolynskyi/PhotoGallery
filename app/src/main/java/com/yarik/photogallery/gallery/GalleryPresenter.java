package com.yarik.photogallery.gallery;

import android.support.annotation.NonNull;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.api.Config;
import com.yarik.photogallery.api.PhotosApi;
import com.yarik.photogallery.api.PhotosFactory;
import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.mvp.Presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class GalleryPresenter extends Presenter<IGalleryView> {

    private int                                mPhotosAded;
    private List<Photo>                        mPhotos         = new ArrayList<>();

    public GalleryPresenter(@NonNull final GalleryContext context) {
        super(context);
    }

    @Override
    protected void onViewAttached() {
        if (!mPhotos.isEmpty()) {
            getView().addPhotos(mPhotos, mPhotosAded);
        }
    }

    @Override
    protected void onViewDetached() {
    }

    public void newPhotosReceived(@NonNull final List<Photo> photos) {
        mPhotosAded += photos.size();
        mPhotos.addAll(photos);
        getView().addPhotos(photos, mPhotosAded);
    }

    public void onPhotoClicked(@NonNull final Photo photo) {
        Timber.d("photo name" + photo.getName());
        Timber.d("author name" + photo.getUser().getFirstname() + " " + photo.getUser().getLastname());
        Timber.d("camera model" + photo.getCamera());
    }
}
