package com.yarik.photogallery.api;

import com.yarik.photogallery.api.model.PhotosServerResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public interface PhotosApi {

    @GET("photos")
    Observable<PhotosServerResponse> getPhotos(@QueryMap Map<String, String> map);
}
