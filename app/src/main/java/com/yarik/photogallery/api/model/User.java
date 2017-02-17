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

public class User implements Parcelable {
    private long   id;
    private String firstname;
    private String lastname;

    protected User(Parcel in) {
        id = in.readLong();
        firstname = in.readString();
        lastname = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeLong(id);
        parcel.writeString(firstname);
        parcel.writeString(lastname);
    }
}
