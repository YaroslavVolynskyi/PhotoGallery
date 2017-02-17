package com.yarik.photogallery.photo;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.R;
import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.mvp.PresenterActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 16/02/17.
 */

public class PhotoDetailActivity extends PresenterActivity<PhotoDetailPresenter, IPhotoDetailView> implements IPhotoDetailView {

    public static final String                         KEY_PHOTOS   = "photos";
    public static final String                         KEY_POSITION = "position";
    @BindView(R.id.fab) FloatingActionButton           mFab;
    @BindView(R.id.galleryViewPager) RecyclerViewPager mRecyclerViewPager;
    private PhotoDetailRecyclerViewAdapter             mPagerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ButterKnife.bind(this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewPager.setLayoutManager(mLayoutManager);
        mPagerAdapter = new PhotoDetailRecyclerViewAdapter(this);
        mRecyclerViewPager.setAdapter(mPagerAdapter);

        initPresenter();
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initPresenter() {
        final Intent intent = getIntent();
        final ArrayList<Photo> photos = intent.getParcelableArrayListExtra(KEY_PHOTOS);
        final int position = intent.getExtras().getInt(KEY_POSITION);
        final Photo photo = photos.get(position);
        getPresenter().initPresenter(photos, position);

        mFab.setOnClickListener(view -> {
            if (photo.getImageUrl() != null) {
                sharePhotoUrl(photo.getImageUrl());
            }
        });
    }

    private void sharePhotoUrl(@NonNull final String photoUrl) {
        final Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.check_out_photo_xs, photoUrl));
        sharingIntent.setType("text/plain");
        final List<ResolveInfo> activities = getPackageManager().queryIntentActivities(sharingIntent, 0);
        if (!activities.isEmpty()) {
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
        }
    }

    public void updateView(@NonNull final List<Photo> photos, final int position) {
        mPagerAdapter.setPhotos(photos);
        mPagerAdapter.notifyDataSetChanged();
        mLayoutManager.scrollToPosition(position);
    }

    @NonNull
    @Override
    protected PhotoDetailPresenter onCreatePresenter(@NonNull final GalleryContext context) {
        return new PhotoDetailPresenter(context);
    }

    @Override
    protected IPhotoDetailView getPresenterView() {
        return this;
    }
}
