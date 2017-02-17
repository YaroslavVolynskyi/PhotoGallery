package com.yarik.photogallery.photo;

import android.support.annotation.NonNull;

import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.mvp.IPresenterView;

import java.util.List;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 16/02/17.
 */

public interface IPhotoDetailView extends IPresenterView {

    void updateView(@NonNull final List<Photo> photos, final int position);
}
