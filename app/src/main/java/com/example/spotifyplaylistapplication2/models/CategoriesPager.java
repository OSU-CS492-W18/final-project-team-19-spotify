package com.example.spotifyplaylistapplication2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoriesPager implements Parcelable {
    public Pager<Category> categories;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.categories, 0);
    }

    public CategoriesPager() {
    }

    protected CategoriesPager(Parcel in) {
        this.categories = in.readParcelable(Pager.class.getClassLoader());
    }

    public static final Creator<CategoriesPager> CREATOR = new Creator<CategoriesPager>() {
        public CategoriesPager createFromParcel(Parcel source) {
            return new CategoriesPager(source);
        }

        public CategoriesPager[] newArray(int size) {
            return new CategoriesPager[size];
        }
    };
}