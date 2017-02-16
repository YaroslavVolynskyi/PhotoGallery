package com.yarik.photogallery.gallery;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 16/02/17.
 */

public class PaginationTool<T> {

    public PaginationTool() {

    }

//    public Observable<T> getPagingObservable(@NonNull final RecyclerView recyclerView, final int limit, final int retryCount) {
//        int startNumberOfRetryAttempt = 0;
//        return getScrollObservable(recyclerView, limit)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .distinctUntilChanged()
//                .observeOn(Schedulers.newThread())
//                .switchMap(offset -> getPagingObservable(pagingListener, pagingListener.onNextPage(offset), startNumberOfRetryAttempt, offset, retryCount));
//    }
//
//    public Observable<Integer> getScrollObservable(@NonNull final RecyclerView recyclerView, final int limit) {
//        return Observable.create(subscriber -> {
//            final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    if (!subscriber.isUnsubscribed()) {
//                        int position = getLastVisibleItemPosition(recyclerView);
//                        int updatePosition = recyclerView.getAdapter().getItemCount() - 1 - (limit / 2);
//                        if (position >= updatePosition) {
//                            int offset = recyclerView.getAdapter().getItemCount();
//                            subscriber.onNext(offset);
//                        }
//                    }
//                }
//            };
//
//            recyclerView.addOnScrollListener(onScrollListener);
//            subscriber.add(Subscriptions.create(() -> recyclerView.removeOnScrollListener(onScrollListener)));
//        });
//    }
//
//    private int getLastVisibleItemPosition(RecyclerView recyclerView) {
//        Class recyclerViewLMClass = recyclerView.getLayoutManager().getClass();
//        if (recyclerViewLMClass == LinearLayoutManager.class || LinearLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
//            return linearLayoutManager.findLastVisibleItemPosition();
//        } else if (recyclerViewLMClass == StaggeredGridLayoutManager.class || StaggeredGridLayoutManager.class.isAssignableFrom(recyclerViewLMClass)) {
//            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager)recyclerView.getLayoutManager();
//            int[] into = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
//            List<Integer> intoList = new ArrayList<>();
//            for (int i : into) {
//                intoList.add(i);
//            }
//            return Collections.max(intoList);
//        }
//        return 0;
//    }
}
