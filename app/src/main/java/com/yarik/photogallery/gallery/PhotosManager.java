package com.yarik.photogallery.gallery;

import android.support.annotation.NonNull;

import com.yarik.photogallery.api.Config;
import com.yarik.photogallery.api.PhotosApi;
import com.yarik.photogallery.api.model.Photo;
import com.yarik.photogallery.api.model.PhotosServerResponse;

import java.util.List;

import rx.Observable;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class PhotosManager {

    @NonNull private final PhotosApi mPhotosApi;

    public PhotosManager(@NonNull final PhotosApi photosApi) {
        mPhotosApi = photosApi;
    }

//    public Observable<List<Photo>> getPhotos(final int pageIndex) {
//        return mPhotosApi.getPhotos("popular", Config.CONSUMER_KEY, pageIndex)
//                .map(PhotosServerResponse::getPhotos);
//    }
}
