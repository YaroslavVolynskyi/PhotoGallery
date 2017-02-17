package com.yarik.photogallery.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.R;
import com.yarik.photogallery.api.Config;
import com.yarik.photogallery.api.PhotosApi;
import com.yarik.photogallery.api.PhotosFactory;
import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.mvp.PresenterActivity;
import com.yarik.photogallery.photo.PhotoDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class GalleryActivity extends PresenterActivity<GalleryPresenter, IGalleryView> implements IGalleryView {

    @BindView(R.id.gallery_recycler_view) RecyclerView mGalleryRecyclerView;
    private final Map<String, String>                  mParametersMap = new HashMap<>(3);
    private GalleryRecyclerViewAdapter                 mAdapter;
    private CompositeSubscription                      mSubscriptions;
    private int                                        mPhotosAdded;
    private int                                        mLastLoadedPage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final int columnCount = 2;
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columnCount);
        mGalleryRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new GalleryRecyclerViewAdapter(this, this::onPhotoClicked);
        mGalleryRecyclerView.setAdapter(mAdapter);
        mParametersMap.put(Config.PARAM_FEATURE, Config.PhotoFeatues.POPULAR.getFeatureName());
        mParametersMap.put(Config.PARAM_COSUMER_KEY, Config.CONSUMER_KEY);
        mParametersMap.put(Config.PARAM_PAGE, String.valueOf(mLastLoadedPage));
    }

    @SuppressWarnings("unchecked")
    private void onPhotoClicked(@NonNull final List<Photo> photos, final int position) {
        final Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putParcelableArrayListExtra("photos", (ArrayList) photos);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private Observable<Integer> getNextPageObservable() {
        return Observable.create(subscriber -> {
            subscriber.onNext(mLastLoadedPage + 1);
            mGalleryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    subscriber.onNext(gridLayoutManager.findLastCompletelyVisibleItemPosition());
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //@formatter:off
        mSubscriptions = new CompositeSubscription();
        final PhotosFactory factory = new PhotosFactory(Config.BASE_URL);
        final PhotosApi photosApi = factory.getPhotosApi();
        final Subscription nextPageSubscription = getNextPageObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .observeOn(Schedulers.newThread())
                .switchMap(lastItem -> {
                    if (lastItem > mPhotosAdded - 2) {
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
                .subscribe(photos -> {
                    getPresenter().newPhotosReceived(photos, mLastLoadedPage);
                }, throwable -> Timber.e(throwable.getMessage()));
        mSubscriptions.add(nextPageSubscription);
        //@formatter:on
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSubscriptions.unsubscribe();
    }

    @NonNull
    @Override
    protected GalleryPresenter onCreatePresenter(@NonNull final GalleryContext context) {
        return new GalleryPresenter(context);
    }

    @Override
    protected IGalleryView getPresenterView() {
        return this;
    }

    @Override
    public void addPhotos(@NonNull final List<Photo> photos, final int photosAdded, final int lastLoadedPage, final boolean arePhotosOld) {
        if (arePhotosOld) {
            mAdapter.setPhotos(photos);
        } else {
            mAdapter.addPhotos(photos);
        }
        mAdapter.notifyDataSetChanged();
        mLastLoadedPage = lastLoadedPage;
        mPhotosAdded = photosAdded;
    }
}
