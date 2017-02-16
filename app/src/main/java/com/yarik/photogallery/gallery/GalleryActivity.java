package com.yarik.photogallery.gallery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.R;
import com.yarik.photogallery.api.Config;
import com.yarik.photogallery.api.PhotosFactory;
import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.mvp.PresenterActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
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

    private GalleryRecyclerViewAdapter                 mAdapter;

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
    }

    private void onPhotoClicked(@NonNull final Photo photo) {
        getPresenter().onPhotoClicked(photo);
    }

    private Observable<Integer> getNextPageObservable() {
        return Observable.create(subscriber -> {
            subscriber.onNext(1);
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

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @NonNull
    @Override
    protected GalleryPresenter onCreatePresenter(@NonNull final GalleryContext context) {
        return new GalleryPresenter(context, getNextPageObservable());
    }

    @Override
    protected IGalleryView getPresenterView() {
        return this;
    }

    @Override
    public void addPhotos(@NonNull final List<Photo> photos) {
        mAdapter.addPhotos(photos);
        mAdapter.notifyDataSetChanged();
    }
}
