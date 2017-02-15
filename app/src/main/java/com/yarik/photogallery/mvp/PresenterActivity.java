package com.yarik.photogallery.mvp;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.yarik.photogallery.GalleryContext;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public abstract class PresenterActivity<P extends Presenter<V>, V extends IPresenterView> extends AppCompatActivity {

    private final static String UUID_SAVED_BUNDLE_KEY = "uuid_saved_bundle_key";
    private P                   mPresenter;
    private String              uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            uuid = PresentersHolder.getInstance().getNextId();
        } else {
            uuid = savedInstanceState.getString(UUID_SAVED_BUNDLE_KEY);
        }
        initPresenter(uuid);
        super.onCreate(savedInstanceState);
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(UUID_SAVED_BUNDLE_KEY, uuid);
    }

    @NonNull
    protected abstract P onCreatePresenter(@NonNull final GalleryContext context);

    protected P getPresenter() {
        return mPresenter;
    }

    protected abstract V getPresenterView();

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attach(getPresenterView());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detach();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            mPresenter.destroy();
            PresentersHolder.getInstance().removePresenter(uuid);
        }
    }

    private void initPresenter(String uuid) {
        mPresenter = PresentersHolder.getInstance().getOrCreate(uuid, this::createPresenter);
    }

    private P createPresenter() {
        final GalleryContext galleryContext = (GalleryContext) getApplicationContext();
        return onCreatePresenter(galleryContext);
    }
}
