package com.yarik.photogallery.gallery;

import rx.Observable;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 16/02/17.
 */

public interface PagingListener<T> {

    Observable<T> getNextPage(int offset);
}
