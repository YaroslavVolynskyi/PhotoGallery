package com.yarik.photogallery.gallery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
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

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class GalleryActivity extends PresenterActivity<GalleryPresenter, IGalleryView> implements IGalleryView {

    @BindView(R.id.gallery_recycler_view)
    RecyclerView mGalleryRecyclerView;

    private GalleryRecyclerViewAdapter             mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final int columnCount = 2;
        mGalleryRecyclerView.setLayoutManager(new GridLayoutManager(this, columnCount));
        mAdapter = new GalleryRecyclerViewAdapter(this);
        mGalleryRecyclerView.setAdapter(mAdapter);
    }

    @NonNull
    @Override
    protected GalleryPresenter onCreatePresenter(@NonNull final GalleryContext context) {
        final PhotosFactory photosFactory = new PhotosFactory(Config.BASE_URL);
        final PhotosManager photosManager = new PhotosManager(photosFactory.getPhotosApi());
        return new GalleryPresenter(context, photosManager);
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
