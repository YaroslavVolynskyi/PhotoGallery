package com.yarik.photogallery.api;

import android.support.annotation.NonNull;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public final class Config {

    private Config() {}

    public static final String BASE_URL          = "https://api.500px.com/v1/";
    public static final String CONSUMER_KEY      = "wB4ozJxTijCwNuggJvPGtBGCRqaZVcF6jsrzUadF";
    public static final String PARAM_FEATURE     = "feature";
    public static final String PARAM_PAGE        = "page";
    public static final String PARAM_COSUMER_KEY = "consumer_key";

    public enum PhotoFeatues {

        POPULAR("popular");

        private String mFeature;

        PhotoFeatues(@NonNull final String feature) {
            mFeature = feature;
        }

        public String getFeatureName() {
            return mFeature;
        }
    }
}
