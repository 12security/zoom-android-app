package com.zipow.videobox.photopicker.entity;

import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.FileUtils;

public class PhotoDirectory {
    private String coverPath;
    private Uri coverUri;
    private long dateAdded;

    /* renamed from: id */
    private String f315id;
    private String name;
    @NonNull
    private List<Photo> photos = new ArrayList();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PhotoDirectory)) {
            return false;
        }
        PhotoDirectory photoDirectory = (PhotoDirectory) obj;
        boolean isEmpty = true ^ TextUtils.isEmpty(photoDirectory.f315id);
        if (!(!TextUtils.isEmpty(this.f315id)) || !isEmpty || !TextUtils.equals(this.f315id, photoDirectory.f315id)) {
            return false;
        }
        return TextUtils.equals(this.name, photoDirectory.name);
    }

    public int hashCode() {
        if (!TextUtils.isEmpty(this.f315id)) {
            int hashCode = this.f315id.hashCode();
            if (TextUtils.isEmpty(this.name)) {
                return hashCode;
            }
            return (hashCode * 31) + this.name.hashCode();
        } else if (TextUtils.isEmpty(this.name)) {
            return 0;
        } else {
            return this.name.hashCode();
        }
    }

    public String getId() {
        return this.f315id;
    }

    public void setId(String str) {
        this.f315id = str;
    }

    public String getCoverPath() {
        return this.coverPath;
    }

    public void setCoverPath(String str) {
        this.coverPath = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public long getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(long j) {
        this.dateAdded = j;
    }

    public Uri getCoverUri() {
        return this.coverUri;
    }

    public void setCoverUri(Uri uri) {
        this.coverUri = uri;
    }

    @NonNull
    public List<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(@Nullable List<Photo> list) {
        if (list != null) {
            int size = list.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                Photo photo = (Photo) list.get(i);
                if (photo == null || !FileUtils.fileIsExists(photo.getPath())) {
                    list.remove(i);
                } else {
                    i++;
                }
            }
            this.photos = list;
        }
    }

    @NonNull
    public List<String> getPhotoPaths() {
        ArrayList arrayList = new ArrayList(this.photos.size());
        for (Photo path : this.photos) {
            arrayList.add(path.getPath());
        }
        return arrayList;
    }

    public void addPhoto(int i, String str) {
        if (FileUtils.fileIsExists(str)) {
            this.photos.add(new Photo(i, str));
        }
    }

    public void addPhoto(int i, String str, Uri uri) {
        if (FileUtils.fileIsExists(str)) {
            this.photos.add(new Photo(i, str, uri));
        }
    }
}
