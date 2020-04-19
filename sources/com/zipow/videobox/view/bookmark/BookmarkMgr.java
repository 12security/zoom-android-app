package com.zipow.videobox.view.bookmark;

import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.safe.SafeObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BookmarkMgr {
    public static final String BOOKMARK_ITEM_POS = "bookmark_pos";
    public static final String BOOKMARK_TITLE = "bookmark_title";
    public static final String BOOKMARK_URL = "bookmark_url";
    private static final String TAG = "BookmarkMgr";
    private final int DFT_MAX_NUM = 128;
    private final int MINIMUM_CAPACITY = 32;
    private ArrayList<BookmarkItem> mBookmarks;
    private int mMaxNum = 128;

    public BookmarkMgr() {
        init();
    }

    public BookmarkMgr(int i) {
        if (i > 0) {
            this.mMaxNum = i;
        }
        init();
    }

    private void init() {
        this.mBookmarks = new ArrayList<>();
        this.mBookmarks.ensureCapacity(32);
        loadBookmarksFromConfig();
    }

    public void clear() {
        this.mBookmarks.clear();
        saveBookmarksToConfig();
    }

    public boolean isEmpty() {
        return this.mBookmarks.isEmpty();
    }

    public boolean add(BookmarkItem bookmarkItem) {
        if (this.mBookmarks.size() > this.mMaxNum) {
            return false;
        }
        boolean add = this.mBookmarks.add(bookmarkItem);
        if (add) {
            saveBookmarksToConfig();
        }
        return add;
    }

    public int indexOf(Object obj) {
        return this.mBookmarks.indexOf(obj);
    }

    public BookmarkItem remove(int i) {
        BookmarkItem bookmarkItem = (BookmarkItem) this.mBookmarks.remove(i);
        saveBookmarksToConfig();
        return bookmarkItem;
    }

    public boolean remove(Object obj) {
        boolean remove = this.mBookmarks.remove(obj);
        if (remove) {
            saveBookmarksToConfig();
        }
        return remove;
    }

    public BookmarkItem set(int i, BookmarkItem bookmarkItem) {
        BookmarkItem bookmarkItem2 = (BookmarkItem) this.mBookmarks.set(i, bookmarkItem);
        saveBookmarksToConfig();
        return bookmarkItem2;
    }

    public BookmarkItem get(int i) {
        return (BookmarkItem) this.mBookmarks.get(i);
    }

    public ArrayList<BookmarkItem> getAll() {
        return this.mBookmarks;
    }

    public int size() {
        return this.mBookmarks.size();
    }

    @NonNull
    public List<BookmarkItem> subList(int i, int i2) {
        return this.mBookmarks.subList(i, i2);
    }

    @Nullable
    public Object[] toArray() {
        return this.mBookmarks.toArray();
    }

    @NonNull
    public <T> T[] toArray(T[] tArr) {
        return this.mBookmarks.toArray(tArr);
    }

    public void reload() {
        this.mBookmarks.clear();
        loadBookmarksFromConfig();
    }

    private boolean loadBookmarksFromConfig() {
        ByteArrayInputStream byteArrayInputStream;
        Throwable th;
        SafeObjectInputStream safeObjectInputStream;
        Throwable th2;
        Throwable th3;
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.BOOKMARKS, null);
        if (readStringValue == null || readStringValue.length() == 0) {
            return false;
        }
        try {
            try {
                byteArrayInputStream = new ByteArrayInputStream(Base64.decode(readStringValue, 8));
                try {
                    safeObjectInputStream = new SafeObjectInputStream(byteArrayInputStream);
                    try {
                        ArrayList arrayList = (ArrayList) safeObjectInputStream.readObject();
                        if (!arrayList.isEmpty()) {
                            this.mBookmarks.clear();
                            this.mBookmarks.addAll(arrayList);
                        }
                        safeObjectInputStream.close();
                        byteArrayInputStream.close();
                        return true;
                    } catch (Throwable th4) {
                        Throwable th5 = th4;
                        th2 = r4;
                        th3 = th5;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    throw th;
                }
            } catch (Exception unused) {
                return false;
            } catch (Throwable th7) {
                th.addSuppressed(th7);
            }
        } catch (Exception unused2) {
            return false;
        }
        throw th3;
        if (th2 != null) {
            safeObjectInputStream.close();
        } else {
            safeObjectInputStream.close();
        }
        throw th3;
        throw th;
    }

    private void saveBookmarksToConfig() {
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream objectOutputStream;
        Throwable th;
        Throwable th2;
        if (this.mBookmarks.isEmpty()) {
            PreferenceUtil.saveStringValue(PreferenceUtil.BOOKMARKS, null);
            return;
        }
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            try {
                objectOutputStream.writeObject(this.mBookmarks);
                PreferenceUtil.saveStringValue(PreferenceUtil.BOOKMARKS, Base64.encodeToString(byteArrayOutputStream.toByteArray(), 8));
                objectOutputStream.close();
                byteArrayOutputStream.close();
                return;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                th = r3;
                th2 = th4;
            }
        } catch (IOException unused) {
            return;
        } catch (Throwable th5) {
            r1.addSuppressed(th5);
        }
        throw th2;
        if (th != null) {
            try {
                objectOutputStream.close();
            } catch (Throwable th6) {
                th.addSuppressed(th6);
            }
        } else {
            objectOutputStream.close();
        }
        throw th2;
        throw th;
    }
}
