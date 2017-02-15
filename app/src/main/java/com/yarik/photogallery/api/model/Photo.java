package com.yarik.photogallery.api.model;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class Photo {

    private long   id;
    private String name;
    private String camera;
    private String image_url;
    private User user;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCamera() {
        return camera;
    }

    public String getImageUrl() {
        return image_url;
    }

    public User getUser() {
        return user;
    }
}
