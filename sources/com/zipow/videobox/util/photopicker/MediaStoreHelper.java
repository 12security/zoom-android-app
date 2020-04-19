package com.zipow.videobox.util.photopicker;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;
import com.zipow.videobox.photopicker.PhotoPicker;
import com.zipow.videobox.photopicker.entity.Photo;
import com.zipow.videobox.photopicker.entity.PhotoDirectory;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.language.p018bm.Rule;
import p015io.reactivex.Single;
import p015io.reactivex.SingleEmitter;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.SingleOnSubscribe;
import p015io.reactivex.android.schedulers.AndroidSchedulers;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.schedulers.Schedulers;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.videomeetings.C4558R;

public class MediaStoreHelper {
    public static final int INDEX_ALL_PHOTOS = 0;

    private static class PhotoDirLoaderCallbacks implements LoaderCallbacks<Cursor> {
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public PhotosResultCallback resultCallback;

        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        }

        public PhotoDirLoaderCallbacks(Context context2, PhotosResultCallback photosResultCallback) {
            this.context = context2;
            this.resultCallback = photosResultCallback;
        }

        @RequiresApi(api = 29)
        @NonNull
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            Context context2 = this.context;
            boolean z = false;
            if (bundle != null) {
                z = bundle.getBoolean(PhotoPicker.EXTRA_SHOW_GIF, false);
            }
            return new PhotoDirectoryLoader(context2, z);
        }

        @RequiresApi(api = 29)
        public void onLoadFinished(@NonNull Loader<Cursor> loader, @Nullable final Cursor cursor) {
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                Single.create(new SingleOnSubscribe<List<PhotoDirectory>>() {
                    public void subscribe(SingleEmitter<List<PhotoDirectory>> singleEmitter) throws Exception {
                        ArrayList arrayList = new ArrayList();
                        PhotoDirectory photoDirectory = new PhotoDirectory();
                        photoDirectory.setName(PhotoDirLoaderCallbacks.this.context.getString(C4558R.string.zm_picker_all_image));
                        photoDirectory.setId(Rule.ALL);
                        do {
                            Cursor cursor = cursor;
                            int i = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                            Cursor cursor2 = cursor;
                            String string = cursor2.getString(cursor2.getColumnIndexOrThrow("bucket_id"));
                            Cursor cursor3 = cursor;
                            String string2 = cursor3.getString(cursor3.getColumnIndexOrThrow("bucket_display_name"));
                            Cursor cursor4 = cursor;
                            String string3 = cursor4.getString(cursor4.getColumnIndexOrThrow("_data"));
                            Cursor cursor5 = cursor;
                            long j = (long) cursor5.getInt(cursor5.getColumnIndexOrThrow("_size"));
                            Uri uri = null;
                            if (OsUtil.isAtLeastQ()) {
                                uri = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, (long) i);
                            }
                            if (j >= 1) {
                                PhotoDirectory photoDirectory2 = new PhotoDirectory();
                                photoDirectory2.setId(string);
                                photoDirectory2.setName(string2);
                                if (!arrayList.contains(photoDirectory2)) {
                                    if (OsUtil.isAtLeastQ()) {
                                        photoDirectory2.setCoverUri(uri);
                                        photoDirectory2.addPhoto(i, string3, uri);
                                    } else {
                                        photoDirectory2.setCoverPath(string3);
                                        photoDirectory2.addPhoto(i, string3);
                                    }
                                    Cursor cursor6 = cursor;
                                    photoDirectory2.setDateAdded(cursor6.getLong(cursor6.getColumnIndexOrThrow("date_added")));
                                    arrayList.add(photoDirectory2);
                                } else if (OsUtil.isAtLeastQ()) {
                                    ((PhotoDirectory) arrayList.get(arrayList.indexOf(photoDirectory2))).addPhoto(i, string3, uri);
                                } else {
                                    ((PhotoDirectory) arrayList.get(arrayList.indexOf(photoDirectory2))).addPhoto(i, string3);
                                }
                                photoDirectory.addPhoto(i, string3, uri);
                            }
                        } while (cursor.moveToNext());
                        if (photoDirectory.getPhotoPaths().size() > 0) {
                            photoDirectory.setCoverPath((String) photoDirectory.getPhotoPaths().get(0));
                            if (OsUtil.isAtLeastQ() && photoDirectory.getPhotos().size() > 0) {
                                photoDirectory.setCoverUri(((Photo) photoDirectory.getPhotos().get(0)).getUri());
                            }
                        }
                        arrayList.add(0, photoDirectory);
                        singleEmitter.onSuccess(arrayList);
                    }
                }).subscribeOn(Schedulers.m266io()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new SingleObserver<List<PhotoDirectory>>() {
                    public void onSubscribe(Disposable disposable) {
                        if (PhotoDirLoaderCallbacks.this.resultCallback != null) {
                            PhotoDirLoaderCallbacks.this.resultCallback.onLoadStart();
                        }
                    }

                    public void onSuccess(List<PhotoDirectory> list) {
                        if (PhotoDirLoaderCallbacks.this.resultCallback != null) {
                            PhotoDirLoaderCallbacks.this.resultCallback.onResultCallback(list);
                        }
                    }

                    public void onError(Throwable th) {
                        if (PhotoDirLoaderCallbacks.this.resultCallback != null) {
                            PhotoDirLoaderCallbacks.this.resultCallback.onLoadError(th.toString());
                        }
                    }
                });
            }
        }
    }

    public interface PhotosResultCallback {
        void onLoadError(String str);

        void onLoadStart();

        void onResultCallback(List<PhotoDirectory> list);
    }

    public static void getPhotoDirs(FragmentActivity fragmentActivity, Bundle bundle, PhotosResultCallback photosResultCallback) {
        fragmentActivity.getSupportLoaderManager().initLoader(0, bundle, new PhotoDirLoaderCallbacks(fragmentActivity, photosResultCallback));
    }
}
