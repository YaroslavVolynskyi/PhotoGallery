package com.yarik.photogallery.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 15/02/17.
 */

public class Photo implements Parcelable {

    private long   id;
    private String name;
    private String camera;
    private String image_url;
    private User user;

    protected Photo(Parcel in) {
        id = in.readLong();
        name = in.readString();
        camera = in.readString();
        image_url = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(camera);
        parcel.writeString(image_url);
        parcel.writeParcelable(user, i);
    }
}
