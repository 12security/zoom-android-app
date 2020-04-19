package com.zipow.videobox.markdown;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMGlideUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class URLImageParser {
    private static final String TAG = "URLImageParser";
    /* access modifiers changed from: private */
    public CacheImageSize mCache;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public int mImageSize;
    /* access modifiers changed from: private */
    public OnUrlDrawableUpdateListener onUrlDrawableUpdateListener;

    static class CacheImageSize {
        private static final String KEY = "imageSize";
        static Map<String, String> cache = new HashMap();
        private final long AGE = 604800000;

        public CacheImageSize() {
            init();
        }

        private void init() {
            String readStringValue = PreferenceUtil.readStringValue(KEY, "");
            if (!TextUtils.isEmpty(readStringValue)) {
                try {
                    Map map = (Map) new Gson().fromJson(readStringValue, new TypeToken<Map<String, String>>() {
                    }.getType());
                    if (map != null) {
                        for (Entry entry : map.entrySet()) {
                            String str = (String) entry.getValue();
                            try {
                                if (!TextUtils.isEmpty(str) && Long.valueOf(str.split(PreferencesConstants.COOKIE_DELIMITER)[2]).longValue() - System.currentTimeMillis() < 604800000) {
                                    cache.put(entry.getKey(), entry.getValue());
                                }
                            } catch (Exception unused) {
                            }
                        }
                    }
                } catch (Exception unused2) {
                }
            }
        }

        public void save(String str, int i, int i2) {
            Map<String, String> map = cache;
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append(PreferencesConstants.COOKIE_DELIMITER);
            sb.append(i2);
            sb.append(PreferencesConstants.COOKIE_DELIMITER);
            sb.append(System.currentTimeMillis());
            map.put(str, sb.toString());
            PreferenceUtil.saveStringValue(KEY, new Gson().toJson((Object) cache));
        }

        public int getWith(@NonNull String str) {
            String str2 = (String) cache.get(str);
            if (!TextUtils.isEmpty(str2)) {
                try {
                    return Integer.valueOf(str2.split(PreferencesConstants.COOKIE_DELIMITER)[0]).intValue();
                } catch (Exception unused) {
                }
            }
            return -1;
        }

        public int getHeight(@NonNull String str) {
            String str2 = (String) cache.get(str);
            if (!TextUtils.isEmpty(str2)) {
                try {
                    return Integer.valueOf(str2.split(PreferencesConstants.COOKIE_DELIMITER)[1]).intValue();
                } catch (Exception unused) {
                }
            }
            return -1;
        }
    }

    public interface OnUrlDrawableUpdateListener {
        void onUrlDrawableUpdate();
    }

    public URLImageParser(@NonNull Context context) {
        this(context, -1);
    }

    public URLImageParser(@NonNull Context context, int i) {
        this.mCache = new CacheImageSize();
        this.mContext = context;
        this.mImageSize = i;
    }

    public Drawable getDrawable(final String str) {
        int with = this.mCache.getWith(str);
        int height = this.mCache.getHeight(str);
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(this.mContext, C4558R.color.zm_mm_markdown_inline_img_place_holder_color));
        int i = this.mImageSize;
        if (i > 0) {
            colorDrawable.setBounds(0, 0, i, i);
        } else {
            if (with <= 0) {
                with = UIUtil.dip2px(this.mContext, 16.0f);
            }
            if (height <= 0) {
                height = UIUtil.dip2px(this.mContext, 16.0f);
            }
            colorDrawable.setBounds(0, 0, with, height);
        }
        final URLDrawable uRLDrawable = new URLDrawable(colorDrawable);
        uRLDrawable.setBounds(colorDrawable.getBounds());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.fitCenter().dontAnimate().diskCacheStrategy(DiskCacheStrategy.DATA);
        ZMGlideUtil.getGlideRequestManager(this.mContext).setDefaultRequestOptions(requestOptions).asBitmap().load(str).into(new SimpleTarget<Bitmap>() {
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                try {
                    float width = (((float) bitmap.getWidth()) * 1.0f) / ((float) bitmap.getHeight());
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(URLImageParser.this.mContext.getResources(), bitmap);
                    if (URLImageParser.this.mImageSize > 0) {
                        bitmapDrawable.setBounds(0, 0, (int) (((float) URLImageParser.this.mImageSize) * width), URLImageParser.this.mImageSize);
                        uRLDrawable.setBounds(0, 0, (int) (((float) URLImageParser.this.mImageSize) * width), URLImageParser.this.mImageSize);
                    } else {
                        Rect bounds = uRLDrawable.getBounds();
                        bitmapDrawable.setBounds(bounds.left, bounds.top, bounds.left + bitmap.getWidth(), bounds.top + bitmap.getHeight());
                        uRLDrawable.setBounds(bounds.left, bounds.top, bounds.left + bitmap.getWidth(), bounds.top + bitmap.getHeight());
                    }
                    URLImageParser.this.mCache.save(str, bitmap.getWidth(), bitmap.getHeight());
                    uRLDrawable.drawable = bitmapDrawable;
                    uRLDrawable.invalidateSelf();
                    if (URLImageParser.this.onUrlDrawableUpdateListener != null) {
                        URLImageParser.this.onUrlDrawableUpdateListener.onUrlDrawableUpdate();
                    }
                } catch (Exception unused) {
                }
            }
        });
        return uRLDrawable;
    }

    public OnUrlDrawableUpdateListener getOnUrlDrawableUpdateListener() {
        return this.onUrlDrawableUpdateListener;
    }

    public void setOnUrlDrawableUpdateListener(OnUrlDrawableUpdateListener onUrlDrawableUpdateListener2) {
        this.onUrlDrawableUpdateListener = onUrlDrawableUpdateListener2;
    }
}
