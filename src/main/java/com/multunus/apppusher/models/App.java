package com.multunus.apppusher.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yedhukrishnan on 19/06/16.
 */
public class App implements Parcelable {
    private long id;
    private String name;
    private String url;

    public App() {
    }

    public App(Parcel source) {
        id = source.readLong();
        name = source.readString();
        url = source.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return name.replaceAll(" ", "_").concat(".apk").toLowerCase();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<App> CREATOR = new Parcelable.Creator<App>() {
        @Override
        public App createFromParcel(Parcel source) {
            return new App(source);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };
}
