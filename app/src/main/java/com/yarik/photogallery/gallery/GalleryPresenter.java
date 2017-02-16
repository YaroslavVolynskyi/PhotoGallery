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

    private CompositeSubscription              mSubscriptions;
    private final Map<String, String>          mParametersMap  = new HashMap<>(3);
    @NonNull private final Observable<Integer> mNextPageObservable;
    private int                                mLastPage       = 1;
    private int                                mLastLoadedPage = 0;
    private int                                mPhotosAded;
    private List<Photo>                        mPhotos         = new ArrayList<>();

    public GalleryPresenter(@NonNull final GalleryContext context, @NonNull final Observable<Integer> nextPageObservable) {
        super(context);
        mNextPageObservable = nextPageObservable;
        mParametersMap.put(Config.PARAM_FEATURE, Config.PhotoFeatues.POPULAR.getFeatureName());
        mParametersMap.put(Config.PARAM_COSUMER_KEY, Config.CONSUMER_KEY);
        mParametersMap.put(Config.PARAM_PAGE, String.valueOf(mLastPage));
    }

    //@formatter:off
    @Override
    protected void onViewAttached() {
        mSubscriptions = new CompositeSubscription();
        final PhotosFactory factory = new PhotosFactory(Config.BASE_URL);
        final PhotosApi photosApi = factory.getPhotosApi();
        if (!mPhotos.isEmpty()) {
            getView().addPhotos(mPhotos);
        }
        final Subscription nextPageSubscription = mNextPageObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .observeOn(Schedulers.newThread())
                .switchMap(lastItem -> {
                    if (lastItem > mPhotosAded - 2) {
                        mParametersMap.put(Config.PARAM_PAGE, String.valueOf(mLastLoadedPage + 1));
                        return photosApi.getPhotos(mParametersMap);
                    }
                    return Observable.empty();
                })
                .subscribeOn(Schedulers.newThread())
                .distinctUntilChanged()
                .map(photosServerResponse -> {
                    mLastLoadedPage = photosServerResponse.getCurrentPage();
                    return photosServerResponse.getPhotos();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Timber.e(throwable.getMessage()))
                .subscribe(this::newPhotosReceived, throwable -> Timber.e(throwable.getMessage()));

        mSubscriptions.add(nextPageSubscription);
    }
    //@formatter:on

    private void newPhotosReceived(@NonNull final List<Photo> photos) {
        mPhotosAded += photos.size();
        mPhotos.addAll(photos);
        getView().addPhotos(photos);
    }

    @Override
    protected void onViewDetached() {
        mSubscriptions.unsubscribe();
    }

    public void onPhotoClicked(@NonNull final Photo photo) {
        Timber.d("photo name" + photo.getName());
        Timber.d("author name" + photo.getUser().getFirstname() + " " + photo.getUser().getLastname());
        Timber.d("camera model" + photo.getCamera());
    }
}
