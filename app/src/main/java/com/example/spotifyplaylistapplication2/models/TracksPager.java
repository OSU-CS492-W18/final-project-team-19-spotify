package com.example.spotifyplaylistapplication2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TracksPager implements Parcelable {
    public Pager<Track> tracks;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.tracks, 0);
    }

    public TracksPager() {
    }

    protected TracksPager(Parcel in) {
        this.tracks = in.readParcelable(Pager.class.getClassLoader());
    }

    public static final Creator<TracksPager> CREATOR = new Creator<TracksPager>() {
        public TracksPager createFromParcel(Parcel source) {
            return new TracksPager(source);
        }

        public TracksPager[] newArray(int size) {
            return new TracksPager[size];
        }
    };
}
