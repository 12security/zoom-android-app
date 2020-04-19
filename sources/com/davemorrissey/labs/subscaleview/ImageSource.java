package com.davemorrissey.labs.subscaleview;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import androidx.annotation.NonNull;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class ImageSource {
    static final String ASSET_SCHEME = "file:///android_asset/";
    static final String FILE_SCHEME = "file:///";
    private final Bitmap bitmap;
    private boolean cached;
    private final Integer resource;
    private int sHeight;
    private Rect sRegion;
    private int sWidth;
    private boolean tile;
    private final Uri uri;

    private ImageSource(Bitmap bitmap2, boolean z) {
        this.bitmap = bitmap2;
        this.uri = null;
        this.resource = null;
        this.tile = false;
        this.sWidth = bitmap2.getWidth();
        this.sHeight = bitmap2.getHeight();
        this.cached = z;
    }

    private ImageSource(@NonNull Uri uri2) {
        String uri3 = uri2.toString();
        if (uri3.startsWith(FILE_SCHEME) && !new File(uri3.substring(7)).exists()) {
            try {
                uri2 = Uri.parse(URLDecoder.decode(uri3, "UTF-8"));
            } catch (UnsupportedEncodingException unused) {
            }
        }
        this.bitmap = null;
        this.uri = uri2;
        this.resource = null;
        this.tile = true;
    }

    private ImageSource(int i) {
        this.bitmap = null;
        this.uri = null;
        this.resource = Integer.valueOf(i);
        this.tile = true;
    }

    @NonNull
    public static ImageSource resource(int i) {
        return new ImageSource(i);
    }

    @NonNull
    public static ImageSource asset(@NonNull String str) {
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(ASSET_SCHEME);
            sb.append(str);
            return uri(sb.toString());
        }
        throw new NullPointerException("Asset name must not be null");
    }

    @NonNull
    public static ImageSource uri(@NonNull String str) {
        if (str != null) {
            if (!str.contains("://")) {
                if (str.startsWith("/")) {
                    str = str.substring(1);
                }
                StringBuilder sb = new StringBuilder();
                sb.append(FILE_SCHEME);
                sb.append(str);
                str = sb.toString();
            }
            return new ImageSource(Uri.parse(str));
        }
        throw new NullPointerException("Uri must not be null");
    }

    @NonNull
    public static ImageSource uri(@NonNull Uri uri2) {
        if (uri2 != null) {
            return new ImageSource(uri2);
        }
        throw new NullPointerException("Uri must not be null");
    }

    @NonNull
    public static ImageSource bitmap(@NonNull Bitmap bitmap2) {
        if (bitmap2 != null) {
            return new ImageSource(bitmap2, false);
        }
        throw new NullPointerException("Bitmap must not be null");
    }

    @NonNull
    public static ImageSource cachedBitmap(@NonNull Bitmap bitmap2) {
        if (bitmap2 != null) {
            return new ImageSource(bitmap2, true);
        }
        throw new NullPointerException("Bitmap must not be null");
    }

    @NonNull
    public ImageSource tilingEnabled() {
        return tiling(true);
    }

    @NonNull
    public ImageSource tilingDisabled() {
        return tiling(false);
    }

    @NonNull
    public ImageSource tiling(boolean z) {
        this.tile = z;
        return this;
    }

    @NonNull
    public ImageSource region(Rect rect) {
        this.sRegion = rect;
        setInvariants();
        return this;
    }

    @NonNull
    public ImageSource dimensions(int i, int i2) {
        if (this.bitmap == null) {
            this.sWidth = i;
            this.sHeight = i2;
        }
        setInvariants();
        return this;
    }

    private void setInvariants() {
        Rect rect = this.sRegion;
        if (rect != null) {
            this.tile = true;
            this.sWidth = rect.width();
            this.sHeight = this.sRegion.height();
        }
    }

    /* access modifiers changed from: protected */
    public final Uri getUri() {
        return this.uri;
    }

    /* access modifiers changed from: protected */
    public final Bitmap getBitmap() {
        return this.bitmap;
    }

    /* access modifiers changed from: protected */
    public final Integer getResource() {
        return this.resource;
    }

    /* access modifiers changed from: protected */
    public final boolean getTile() {
        return this.tile;
    }

    /* access modifiers changed from: protected */
    public final int getSWidth() {
        return this.sWidth;
    }

    /* access modifiers changed from: protected */
    public final int getSHeight() {
        return this.sHeight;
    }

    /* access modifiers changed from: protected */
    public final Rect getSRegion() {
        return this.sRegion;
    }

    /* access modifiers changed from: protected */
    public final boolean isCached() {
        return this.cached;
    }
}
