package com.yarik.photogallery.gallery;

import android.support.annotation.NonNull;

import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.mvp.IPresenterView;

import java.util.List;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public interface IGalleryView extends IPresenterView {

    void addPhotos(@NonNull List<Photo> photos, int photosAdded, int lastLoadedPage, boolean arePhotosOld);
}
