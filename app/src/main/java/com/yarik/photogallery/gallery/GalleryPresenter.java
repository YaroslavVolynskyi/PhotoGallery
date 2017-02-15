package com.yarik.photogallery.gallery;

import android.support.annotation.NonNull;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.api.Config;
import com.yarik.photogallery.api.PhotosApi;
import com.yarik.photogallery.api.PhotosFactory;
import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.api.model.PhotosServerResponse;
import com.yarik.photogallery.mvp.Presenter;

import java.util.HashMap;
import java.util.Map;

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

    @NonNull final private PhotosManager mPhotosManager;
    private CompositeSubscription        mSubscriptions;

    public GalleryPresenter(@NonNull final GalleryContext context, @NonNull final PhotosManager photosManager) {
        super(context);
        mPhotosManager = photosManager;
    }

    @Override
    protected void onViewAttached() {
        mSubscriptions = new CompositeSubscription();
        final PhotosFactory factory = new PhotosFactory(Config.BASE_URL);
        final PhotosApi photosApi = factory.getPhotosApi();

        final Map<String, String> parameters = new HashMap<>(3);
        parameters.put("feature", "popular");
        parameters.put("consumer_key", Config.CONSUMER_KEY);
        parameters.put("page", "1");
        final Subscription subscription = photosApi.getPhotos(parameters)
                .subscribeOn(Schedulers.newThread())
                .map(PhotosServerResponse::getPhotos)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Timber.e(throwable.getMessage()))
                .subscribe(photos -> {
                    for (final Photo photo : photos) {
                        Timber.e(photo.getImageUrl());
                    }
                    getView().addPhotos(photos);
                }, throwable -> {
                    Timber.e(throwable.getMessage());
                });
        mSubscriptions.add(subscription);
    }

    @Override
    protected void onViewDetached() {
        mSubscriptions.unsubscribe();
    }
}
