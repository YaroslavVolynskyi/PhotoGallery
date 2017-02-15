package com.yarik.photogallery.api.model;

import com.yarik.photogallery.api.model.Photo;

import java.util.List;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class PhotosServerResponse {

    private int         current_page;
    private int         total_pages;
    private int         total_items;
    private List<Photo> photos;
    private Filter      filters;
    private String      feature;

    public int getCurrent_page() {
        return current_page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_items() {
        return total_items;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public Filter getFilters() {
        return filters;
    }

    public String getFeature() {
        return feature;
    }

    private static class Filter {
        private boolean categoty;
        private boolean exclude;
    }
}
