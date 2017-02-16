package com.yarik.photogallery.photo;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;

import com.squareup.picasso.Picasso;
import com.yarik.photogallery.GalleryContext;
import com.yarik.photogallery.R;
import com.yarik.photogallery.mvp.PresenterActivity;

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

    @BindView(R.id.photoImageView) ImageView    mPhotoImageView;
    @BindView(R.id.authorNameTextView) TextView mAuthorNameTextView;
    @BindView(R.id.photoNameTextView) TextView  mPhotoNameTextView;
    @BindView(R.id.cameraTextView) TextView     mCameraTextView;
    @BindView(R.id.fab) FloatingActionButton    mFab;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        final Intent intent = getIntent();
        final String url = intent.getExtras().getString("url");
        final String photoName = intent.getExtras().getString("photoname");
        Picasso.with(this).load(url).into(mPhotoImageView);
        mPhotoNameTextView.setVisibility(photoName != null ? View.VISIBLE : View.GONE);
        mPhotoNameTextView.setText(photoName);
        mFab.setOnClickListener(view -> {
            if (url != null) {
                sharePhotoUrl(url);
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
