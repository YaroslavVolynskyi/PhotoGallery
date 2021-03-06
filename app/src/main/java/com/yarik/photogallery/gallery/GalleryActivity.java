package com.yarik.photogallery.gallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.NetworkUtils;
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
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class GalleryActivity extends PresenterActivity<GalleryPresenter, IGalleryView> implements IGalleryView {

    private static final int                           PORTRAIT_COLUMN_COUNT  = 2;
    private static final int                           LANDSCAPE_COLUMN_COUNT = 4;

    @BindView(R.id.gallery_recycler_view) RecyclerView mGalleryRecyclerView;
    private final Map<String, String>                  mParametersMap         = new HashMap<>(3);
    private GalleryRecyclerViewAdapter                 mAdapter;
    private CompositeSubscription                      mSubscriptions;
    private int                                        mPhotosAdded;
    private int                                        mLastLoadedPage;
    private ProgressDialog                             mProgressDialog;
    private boolean                                    mIsDialogShown;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        registerReceiver(new ConnectivityChangeReceiver(this::initSubscriptions), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == ORIENTATION_LANDSCAPE) {
            mGalleryRecyclerView.setLayoutManager(new GridLayoutManager(this, LANDSCAPE_COLUMN_COUNT));
        } else if (orientation == ORIENTATION_PORTRAIT) {
            mGalleryRecyclerView.setLayoutManager(new GridLayoutManager(this, PORTRAIT_COLUMN_COUNT));
        }
        mAdapter = new GalleryRecyclerViewAdapter(this, this::onPhotoClicked);
        mGalleryRecyclerView.setAdapter(mAdapter);

        mParametersMap.put(Config.PARAM_FEATURE, Config.PhotoFeatues.POPULAR.getFeatureName());
        mParametersMap.put(Config.PARAM_COSUMER_KEY, Config.CONSUMER_KEY);
        mParametersMap.put(Config.PARAM_PAGE, String.valueOf(mLastLoadedPage));
    }

    @SuppressWarnings("unchecked")
    private void onPhotoClicked(@NonNull final List<Photo> photos, final int position) {
        final Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putParcelableArrayListExtra(PhotoDetailActivity.KEY_PHOTOS, (ArrayList) photos);
        intent.putExtra(PhotoDetailActivity.KEY_POSITION, position);
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
        if (NetworkUtils.isNetworkAvailable(this)) {
            initSubscriptions();
        } else {
            showNoInternerDialog();
        }
    }

    private void showNoInternerDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.no_internet);
        alertDialog.setMessage(getString(R.string.turn_on_internet_connection));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private void initSubscriptions() {
        //@formatter:off
        mSubscriptions = new CompositeSubscription();
        final PhotosFactory factory = new PhotosFactory(Config.BASE_URL);
        final PhotosApi photosApi = factory.getPhotosApi();
        if (mPhotosAdded == 0 && mLastLoadedPage == 0 && !mIsDialogShown) {
            mProgressDialog = ProgressDialog.show(this, "", getString(R.string.loading_dialog), true);
            mIsDialogShown = true;
        }
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
                .doOnError(throwable -> {
                    Timber.e(throwable.getMessage());
                    hideProgressDialog();
                })
                .subscribe(photos -> {
                    hideProgressDialog();
                    getPresenter().newPhotosReceived(photos, mLastLoadedPage);
                }, throwable -> {
                    Timber.e(throwable.getMessage());
                    hideProgressDialog();
                });
        mSubscriptions.add(nextPageSubscription);
        //@formatter:on
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mIsDialogShown = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSubscriptions != null && !mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
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

    private class ConnectivityChangeReceiver extends BroadcastReceiver {

        @NonNull private final Action0 mInitSubscriptionAction;

        public ConnectivityChangeReceiver(@NonNull final Action0 initSubscriptionAction) {
            mInitSubscriptionAction = initSubscriptionAction;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                mInitSubscriptionAction.call();
            }
        }
    }
}
