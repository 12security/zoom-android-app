package com.zipow.videobox.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.InputStream;
import java.util.WeakHashMap;
import p021us.zoom.androidlib.util.ZMLog;

public class ContactsAvatarCache {
    private static final String TAG = "ContactsAvatarCache";
    @Nullable
    private static ContactsAvatarCache instance;
    @NonNull
    private WeakHashMap<Integer, Object> mCacheMap = new WeakHashMap<>();

    @NonNull
    public static synchronized ContactsAvatarCache getInstance() {
        ContactsAvatarCache contactsAvatarCache;
        synchronized (ContactsAvatarCache.class) {
            if (instance == null) {
                instance = new ContactsAvatarCache();
            }
            contactsAvatarCache = instance;
        }
        return contactsAvatarCache;
    }

    private ContactsAvatarCache() {
    }

    @Nullable
    public synchronized Bitmap getContactAvatar(Context context, int i) {
        return getContactAvatar(context, i, false);
    }

    @Nullable
    public synchronized Bitmap getContactAvatar(@Nullable Context context, int i, boolean z) {
        if (context == null) {
            return null;
        }
        Object cachedAvatar = getCachedAvatar(i);
        if (cachedAvatar instanceof Bitmap) {
            return (Bitmap) cachedAvatar;
        } else if (cachedAvatar != null) {
            return null;
        } else {
            if (z) {
                return null;
            }
            return syncGetContactAvatar(context, i);
        }
    }

    @Nullable
    private static Bitmap syncGetContactAvatar(@Nullable Context context, int i) {
        if (context == null) {
            return null;
        }
        InputStream avatarStream = getAvatarStream(context, i);
        if (avatarStream == null) {
            getInstance().cacheAvatar(i, null);
            return null;
        }
        Bitmap decodeStream = BitmapFactory.decodeStream(avatarStream);
        try {
            avatarStream.close();
        } catch (Exception e) {
            ZMLog.m289w(TAG, e, "close InputStream exception", new Object[0]);
        }
        getInstance().cacheAvatar(i, decodeStream);
        return decodeStream;
    }

    private synchronized void cacheAvatar(int i, @Nullable Bitmap bitmap) {
        if (bitmap != null) {
            this.mCacheMap.put(Integer.valueOf(i), bitmap);
        } else {
            this.mCacheMap.put(Integer.valueOf(i), Integer.valueOf(0));
        }
    }

    @Nullable
    private synchronized Object getCachedAvatar(int i) {
        return this.mCacheMap.get(Integer.valueOf(i));
    }

    private static InputStream getAvatarStream(@NonNull Context context, int i) {
        if (i < 0) {
            return null;
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            return null;
        }
        try {
            return Contacts.openContactPhotoInputStream(contentResolver, ContentUris.withAppendedId(Contacts.CONTENT_URI, (long) i));
        } catch (Exception unused) {
            return null;
        }
    }

    public String getContactAvatarPath(int i) {
        if (i < 0) {
            return null;
        }
        return Uri.withAppendedPath(ContentUris.withAppendedId(Contacts.CONTENT_URI, (long) i), "photo").toString();
    }
}
