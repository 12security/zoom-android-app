package com.zipow.videobox.photopicker.entity;

import android.net.Uri;

public class Photo {

    /* renamed from: id */
    private int f314id;
    private String path;
    private Uri uri;

    public Photo(int i, String str) {
        this.f314id = i;
        this.path = str;
    }

    public Photo(int i, String str, Uri uri2) {
        this.f314id = i;
        this.path = str;
        this.uri = uri2;
    }

    public Photo() {
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Photo)) {
            return false;
        }
        if (this.f314id != ((Photo) obj).f314id) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return this.f314id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public int getId() {
        return this.f314id;
    }

    public void setId(int i) {
        this.f314id = i;
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri2) {
        this.uri = uri2;
    }
}
