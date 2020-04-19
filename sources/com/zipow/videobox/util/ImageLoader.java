package com.zipow.videobox.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.zmurl.avatar.ZMAvatarCornerParams;
import com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl;
import com.zipow.videobox.view.ZMGifView;
import com.zipow.videobox.view.ZMGifView.OnResizeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    private static final int MSG_ADD_CACHE_FILE = 11;
    private static final int MSG_FIX_CACHE_FILE = 12;
    private static final int MSG_FIX_CACHE_FILE_COMPLETE = 13;
    private static final int MSG_INIT_CACHE_FILE_COMPLETE = 1;
    private static final int MSG_UPDATE_CACHE_FILE = 15;
    private static final String TAG = "com.zipow.videobox.util.ImageLoader";
    private static ImageLoader sImageLoader;
    /* access modifiers changed from: private */
    @Nullable
    public Handler mCacheHandler;
    @NonNull
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public HashMap<String, String> mFileCache;

    private ImageLoader() {
    }

    public void init() {
        this.mCacheHandler = new Handler() {
            public void handleMessage(@NonNull Message message) {
                super.handleMessage(message);
                int i = message.what;
                if (i == 1) {
                    if (message.obj != null) {
                        ImageLoader.this.mFileCache = (HashMap) message.obj;
                    }
                    ImageLoader.this.mCacheHandler.sendEmptyMessage(12);
                } else if (i != 15) {
                    switch (i) {
                        case 11:
                            String[] strArr = (String[]) message.obj;
                            if (strArr != null && strArr.length == 2 && !TextUtils.isEmpty(strArr[0]) && !TextUtils.isEmpty(strArr[1])) {
                                String cacheFilePath = ImageLoader.this.getCacheFilePath(strArr[0]);
                                if (TextUtils.isEmpty(cacheFilePath) || !strArr[1].equals(cacheFilePath)) {
                                    ImageLoader.this.addCacheFile(strArr[0], strArr[1]);
                                    ImageLoader.this.mCacheHandler.removeMessages(15);
                                    ImageLoader.this.mCacheHandler.sendEmptyMessageDelayed(15, 1000);
                                    return;
                                }
                                return;
                            }
                            return;
                        case 12:
                            ImageLoader.this.fixCache();
                            return;
                        case 13:
                            if (message.obj != null) {
                                ImageLoader.this.mFileCache = (HashMap) message.obj;
                                ImageLoader.this.mCacheHandler.removeMessages(15);
                                ImageLoader.this.mCacheHandler.sendEmptyMessageDelayed(15, 1000);
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                } else {
                    ImageLoader.this.mCacheHandler.removeMessages(15);
                    ImageLoader.this.saveCache2File();
                }
            }
        };
        initCacheFromFile();
    }

    public static synchronized ImageLoader getInstance() {
        ImageLoader imageLoader;
        synchronized (ImageLoader.class) {
            if (sImageLoader == null) {
                sImageLoader = new ImageLoader();
            }
            imageLoader = sImageLoader;
        }
        return imageLoader;
    }

    /* access modifiers changed from: private */
    @NonNull
    public String getFinalUrl(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.trim().startsWith("/") && !new File(str).exists()) {
            StringBuilder sb = new StringBuilder();
            sb.append(ZMDomainUtil.getWebDomain());
            sb.append(str.trim());
            str = sb.toString();
        }
        return str;
    }

    @Nullable
    public File getCacheFile(@Nullable String str) {
        if (TextUtils.isEmpty(str) || this.mFileCache == null) {
            return null;
        }
        String cacheFilePath = getCacheFilePath(str);
        if (TextUtils.isEmpty(cacheFilePath)) {
            return null;
        }
        return new File(cacheFilePath);
    }

    @Nullable
    public String getCacheFilePath(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        HashMap<String, String> hashMap = this.mFileCache;
        if (hashMap == null) {
            return null;
        }
        return (String) hashMap.get(getFinalUrl(str));
    }

    public void addCacheFile(@Nullable String str, String str2) {
        if (this.mFileCache == null) {
            this.mFileCache = new HashMap<>();
        }
        this.mFileCache.put(getFinalUrl(str), str2);
    }

    public void displayGif(@Nullable final ZMGifView zMGifView, @Nullable final View view, @Nullable String str) {
        if (zMGifView != null && !TextUtils.isEmpty(str)) {
            zMGifView.setGifRemoteResourse(getFinalUrl(str), new ZMGlideRequestListener() {
                public void onSuccess(String str) {
                    View view = view;
                    if (view != null) {
                        view.setVisibility(8);
                    }
                    if (zMGifView.getVisibility() != 0) {
                        zMGifView.setVisibility(0);
                    }
                    ImageLoader.this.requestToCache(str);
                }

                public void onError(String str, GifException gifException) {
                    View view = view;
                    if (view != null) {
                        view.setVisibility(8);
                    }
                }
            });
        }
    }

    public void displayGif(ZMGifView zMGifView, @Nullable String str, ZMGlideRequestListener zMGlideRequestListener) {
        displayGif(zMGifView, str, zMGlideRequestListener, null);
    }

    public void displayGif(@Nullable final ZMGifView zMGifView, @Nullable String str, @Nullable final ZMGlideRequestListener zMGlideRequestListener, OnResizeListener onResizeListener) {
        if (zMGifView != null && !TextUtils.isEmpty(str)) {
            zMGifView.setGifRemoteResourse(getFinalUrl(str), new ZMGlideRequestListener() {
                public void onSuccess(String str) {
                    if (zMGifView.getVisibility() != 0) {
                        zMGifView.setVisibility(0);
                    }
                    ImageLoader.this.requestToCache(str);
                    ZMGlideRequestListener zMGlideRequestListener = zMGlideRequestListener;
                    if (zMGlideRequestListener != null) {
                        zMGlideRequestListener.onSuccess(str);
                    }
                }

                public void onError(String str, GifException gifException) {
                    ZMGlideRequestListener zMGlideRequestListener = zMGlideRequestListener;
                    if (zMGlideRequestListener != null) {
                        zMGlideRequestListener.onError(str, gifException);
                    }
                }
            }, onResizeListener);
        }
    }

    /* access modifiers changed from: private */
    public void requestToCache(final Object obj) {
        File cacheFile = getCacheFile(getUrlString(obj));
        if (cacheFile == null || !cacheFile.exists()) {
            this.mExecutorService.execute(new Runnable() {
                public void run() {
                    try {
                        File cachedFile = ZMGlideUtil.getCachedFile((Context) VideoBoxApplication.getInstance(), obj);
                        if (cachedFile != null && cachedFile.exists()) {
                            Message obtainMessage = ImageLoader.this.mCacheHandler.obtainMessage(11);
                            obtainMessage.obj = new String[]{ImageLoader.this.getUrlString(obj), cachedFile.getAbsolutePath()};
                            obtainMessage.sendToTarget();
                        }
                    } catch (InterruptedException | ExecutionException unused) {
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    @Nullable
    public String getUrlString(@Nullable Object obj) {
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void saveCache2File() {
        HashMap<String, String> hashMap = this.mFileCache;
        if (hashMap != null && !hashMap.isEmpty()) {
            final HashMap hashMap2 = new HashMap(this.mFileCache);
            this.mExecutorService.execute(new Runnable() {
                public void run() {
                    FileOutputStream openFileOutput;
                    ObjectOutputStream objectOutputStream;
                    Throwable th;
                    Throwable th2;
                    if (!hashMap2.isEmpty()) {
                        try {
                            openFileOutput = VideoBoxApplication.getInstance().openFileOutput("imgcache", 0);
                            objectOutputStream = new ObjectOutputStream(openFileOutput);
                            try {
                                objectOutputStream.writeObject(hashMap2);
                                objectOutputStream.flush();
                                objectOutputStream.close();
                                if (openFileOutput != null) {
                                    openFileOutput.close();
                                }
                            } catch (Throwable th3) {
                                Throwable th4 = th3;
                                th = r3;
                                th2 = th4;
                            }
                        } catch (Exception unused) {
                        } catch (Throwable th5) {
                            r1.addSuppressed(th5);
                        }
                        return;
                    }
                    return;
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
            });
        }
    }

    private void initCacheFromFile() {
        this.mExecutorService.execute(new Runnable() {
            public void run() {
                FileInputStream openFileInput;
                Throwable th;
                Throwable th2;
                try {
                    openFileInput = VideoBoxApplication.getInstance().openFileInput("imgcache");
                    ObjectInputStream objectInputStream = new ObjectInputStream(openFileInput);
                    try {
                        HashMap hashMap = (HashMap) objectInputStream.readObject();
                        if (hashMap != null) {
                            Message obtainMessage = ImageLoader.this.mCacheHandler.obtainMessage(1);
                            obtainMessage.obj = hashMap;
                            obtainMessage.sendToTarget();
                        }
                        objectInputStream.close();
                        if (openFileInput != null) {
                            openFileInput.close();
                            return;
                        }
                        return;
                    } catch (Throwable th3) {
                        Throwable th4 = th3;
                        th = r3;
                        th2 = th4;
                    }
                    throw th2;
                    if (th != null) {
                        try {
                            objectInputStream.close();
                        } catch (Throwable th5) {
                            th.addSuppressed(th5);
                        }
                    } else {
                        objectInputStream.close();
                    }
                    throw th2;
                    throw th;
                } catch (Exception unused) {
                } catch (Throwable th6) {
                    r1.addSuppressed(th6);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void fixCache() {
        HashMap<String, String> hashMap = this.mFileCache;
        if (hashMap != null && !hashMap.isEmpty()) {
            final HashMap hashMap2 = new HashMap(this.mFileCache);
            this.mExecutorService.execute(new Runnable() {
                public void run() {
                    Message obtainMessage = ImageLoader.this.mCacheHandler.obtainMessage(13);
                    HashMap hashMap = new HashMap(hashMap2.size());
                    for (Entry entry : hashMap2.entrySet()) {
                        String str = (String) entry.getValue();
                        if (!TextUtils.isEmpty(str) && new File(str).exists()) {
                            hashMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                    if (hashMap.size() < hashMap2.size()) {
                        obtainMessage.obj = hashMap;
                    }
                    obtainMessage.sendToTarget();
                }
            });
        }
    }

    public void clearImageLoding(@Nullable ImageView imageView) {
        if (imageView != null) {
            ZMGlideUtil.clear(imageView.getContext(), imageView);
        }
    }

    public void displayImage(@Nullable ImageView imageView, @Nullable final String str, int i) {
        if (imageView != null && !TextUtils.isEmpty(str)) {
            ZMGlideUtil.load(imageView.getContext(), imageView, (Object) getFinalUrl(str), i, (RequestListener) new RequestListener() {
                public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target target, boolean z) {
                    return false;
                }

                public boolean onResourceReady(Object obj, Object obj2, Target target, DataSource dataSource, boolean z) {
                    ImageLoader imageLoader = ImageLoader.this;
                    imageLoader.requestToCache(imageLoader.getFinalUrl(str));
                    return false;
                }
            });
        }
    }

    public void displayImage(@Nullable ImageView imageView, @Nullable final String str) {
        if (imageView != null && !TextUtils.isEmpty(str)) {
            ZMGlideUtil.load(imageView.getContext(), imageView, (Object) getFinalUrl(str), (RequestListener) new RequestListener() {
                public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target target, boolean z) {
                    return false;
                }

                public boolean onResourceReady(Object obj, Object obj2, Target target, DataSource dataSource, boolean z) {
                    ImageLoader imageLoader = ImageLoader.this;
                    imageLoader.requestToCache(imageLoader.getFinalUrl(str));
                    return false;
                }
            });
        }
    }

    public void displayAvatar(ImageView imageView, String str, String str2, String str3, ZMAvatarCornerParams zMAvatarCornerParams, Drawable drawable, String str4) {
        displayAvatar(imageView, str, str2, str3, zMAvatarCornerParams, drawable, 0, str4);
    }

    public void displayAvatar(ImageView imageView, @DrawableRes int i, String str, ZMAvatarCornerParams zMAvatarCornerParams, Drawable drawable) {
        displayAvatar(imageView, null, null, str, zMAvatarCornerParams, drawable, i, null);
    }

    public void displayAvatar(ImageView imageView, String str, String str2, ZMAvatarCornerParams zMAvatarCornerParams, Drawable drawable) {
        displayAvatar(imageView, null, str, str2, zMAvatarCornerParams, drawable, 0, null);
    }

    public void displayAvatar(@Nullable ImageView imageView, String str, String str2, String str3, ZMAvatarCornerParams zMAvatarCornerParams, Drawable drawable, @DrawableRes int i, String str4) {
        String str5 = str;
        if (imageView != null && (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2) || i != 0)) {
            ZMAvatarUrl zMAvatarUrl = new ZMAvatarUrl(getFinalUrl(str), str2, str3, i, zMAvatarCornerParams);
            if (TextUtils.isEmpty(str) || (!str.contains("content://com.android.contacts/contacts/") && !ImageUtil.isValidImageFile(str))) {
                Drawable drawable2 = drawable;
                String str6 = str4;
                ZMGlideUtil.load(imageView.getContext(), imageView, (Object) zMAvatarUrl, str4, drawable, drawable, (RequestListener) new RequestListener() {
                    public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target target, boolean z) {
                        return false;
                    }

                    public boolean onResourceReady(Object obj, Object obj2, Target target, DataSource dataSource, boolean z) {
                        return false;
                    }
                });
            } else {
                ZMGlideUtil.load(imageView.getContext(), imageView, (Object) zMAvatarUrl, str4, drawable);
            }
        }
    }
}
