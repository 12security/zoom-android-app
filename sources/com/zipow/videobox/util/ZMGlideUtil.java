package com.zipow.videobox.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.zipow.videobox.GlideApp;
import com.zipow.videobox.GlideRequest;
import com.zipow.videobox.GlideRequests;
import com.zipow.videobox.util.zmurl.avatar.BorderTransformation;
import com.zipow.videobox.util.zmurl.avatar.ZMAvatarCornerParams;
import com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl;
import java.io.File;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.concurrent.ExecutionException;
import p021us.zoom.androidlib.util.AndroidLifecycleUtils;
import p021us.zoom.videomeetings.C4558R;

public class ZMGlideUtil {
    private static final String TAG = "ZMGlideUtil";

    static {
        init();
    }

    public static void initAuthenticator() {
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                String[] authenticator = ZMUtils.getAuthenticator();
                if (authenticator == null) {
                    return null;
                }
                String str = authenticator[0];
                String str2 = authenticator[1];
                if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
                    return null;
                }
                return new PasswordAuthentication(str, str2.toCharArray());
            }
        });
    }

    public static void init() {
        initAuthenticator();
    }

    public static void load(@NonNull Context context, @NonNull ImageView imageView, Object obj, RequestListener requestListener) {
        load(context, imageView, obj, C4558R.C4559drawable.zm_image_placeholder, C4558R.C4559drawable.zm_image_download_error, requestListener);
    }

    public static void load(@NonNull Context context, @NonNull ImageView imageView, Object obj, int i, RequestListener requestListener) {
        load(context, imageView, obj, i, C4558R.C4559drawable.zm_image_download_error, requestListener);
    }

    public static void load(@NonNull Context context, @NonNull ImageView imageView, Object obj, int i, int i2, RequestListener requestListener) {
        load(context, imageView, obj, i, i2, null, requestListener);
    }

    public static void load(@NonNull Context context, @NonNull ImageView imageView, Object obj, int i, int i2, @Nullable Transformation<Bitmap> transformation, RequestListener requestListener) {
        if (AndroidLifecycleUtils.canLoadImage(context)) {
            RequestOptions requestOptions = new RequestOptions();
            if (i > -1) {
                requestOptions.placeholder(i);
            }
            if (i2 > -1) {
                requestOptions.error(i2);
            }
            requestOptions.fitCenter().dontAnimate().diskCacheStrategy(DiskCacheStrategy.DATA);
            GlideRequest load = GlideApp.with(context).setDefaultRequestOptions(requestOptions).asBitmap().listener(requestListener).load(obj);
            if (transformation != null) {
                load.apply(RequestOptions.bitmapTransform(transformation));
            }
            load.into(imageView);
        }
    }

    public static void load(@NonNull Context context, @NonNull ImageView imageView, Object obj, String str, Drawable drawable, Drawable drawable2, RequestListener requestListener) {
        load(context, imageView, obj, str, drawable, drawable2, null, requestListener);
    }

    public static void load(@NonNull Context context, @NonNull ImageView imageView, Object obj, String str, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Transformation<Bitmap> transformation, RequestListener requestListener) {
        if (AndroidLifecycleUtils.canLoadImage(context)) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(C4558R.C4559drawable.zm_no_avatar).error(C4558R.C4559drawable.zm_no_avatar).fitCenter().dontAnimate();
            try {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
            } catch (Exception unused) {
            }
            if (!TextUtils.isEmpty(str)) {
                requestOptions.signature(new ObjectKey(str));
            }
            GlideRequest load = GlideApp.with(context).setDefaultRequestOptions(requestOptions).asBitmap().listener(requestListener).load(obj);
            if (transformation != null) {
                load.apply(RequestOptions.bitmapTransform(transformation));
            }
            load.into(imageView);
        }
    }

    public static void load(@NonNull Context context, @NonNull ImageView imageView, Object obj, String str, Drawable drawable) {
        GlideRequest glideRequest;
        if (AndroidLifecycleUtils.canLoadImage(context) && (obj instanceof ZMAvatarUrl)) {
            ZMAvatarUrl zMAvatarUrl = (ZMAvatarUrl) obj;
            if (!TextUtils.isEmpty(zMAvatarUrl.getUrl())) {
                if (zMAvatarUrl.getUrl().contains("content://com.android.contacts/contacts/")) {
                    glideRequest = GlideApp.with(imageView.getContext()).load(Uri.parse(zMAvatarUrl.getUrl()));
                } else {
                    glideRequest = GlideApp.with(imageView.getContext()).load(zMAvatarUrl.getUrl());
                }
                glideRequest.placeholder(C4558R.C4559drawable.zm_no_avatar).error(C4558R.C4559drawable.zm_no_avatar).fitCenter().dontAnimate();
                try {
                    glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                } catch (Exception unused) {
                }
                ZMAvatarCornerParams zMAvatarCornerParams = zMAvatarUrl.getZMAvatarCornerParams();
                if (zMAvatarCornerParams != null && zMAvatarCornerParams.isbCircle()) {
                    glideRequest.transform(new BorderTransformation(zMAvatarCornerParams.getBorderSize(), zMAvatarCornerParams.getBorderColor(), (int) (((float) zMAvatarCornerParams.getClientWidth()) * zMAvatarCornerParams.getCornerRatio())));
                }
                if (!TextUtils.isEmpty(str)) {
                    glideRequest.signature(new ObjectKey(str));
                }
                glideRequest.into(imageView);
            }
        }
    }

    public static <Y extends Target> void load(@NonNull Context context, @NonNull Y y, Uri uri, RequestListener requestListener) {
        load(context, y, uri, C4558R.C4559drawable.zm_image_placeholder, C4558R.C4559drawable.zm_image_download_error, requestListener);
    }

    public static <Y extends Target> void load(@NonNull Context context, @NonNull Y y, String str, RequestListener requestListener) {
        load(context, y, str, C4558R.C4559drawable.zm_image_placeholder, C4558R.C4559drawable.zm_image_download_error, requestListener);
    }

    public static <Y extends Target> void load(@NonNull Context context, @NonNull Y y, Uri uri, int i, int i2, RequestListener requestListener) {
        load(context, y, uri, i, i2, null, requestListener);
    }

    public static <Y extends Target> void load(@NonNull Context context, @NonNull Y y, String str, int i, int i2, RequestListener requestListener) {
        load(context, y, str, i, i2, null, requestListener);
    }

    public static <Y extends Target> void load(@NonNull Context context, @NonNull Y y, Uri uri, int i, int i2, @Nullable Transformation<Bitmap> transformation, RequestListener requestListener) {
        if (AndroidLifecycleUtils.canLoadImage(context)) {
            RequestOptions requestOptions = new RequestOptions();
            if (i > -1) {
                requestOptions.placeholder(i);
            }
            if (i2 > -1) {
                requestOptions.error(i2);
            }
            requestOptions.fitCenter().dontAnimate().diskCacheStrategy(DiskCacheStrategy.DATA);
            GlideRequest listener = GlideApp.with(context).setDefaultRequestOptions(requestOptions).load(uri).listener(requestListener);
            if (transformation != null) {
                listener.apply(RequestOptions.bitmapTransform(transformation));
            }
            listener.into(y);
        }
    }

    public static <Y extends Target> void load(@NonNull Context context, @NonNull Y y, String str, int i, int i2, @Nullable Transformation<Bitmap> transformation, RequestListener requestListener) {
        if (AndroidLifecycleUtils.canLoadImage(context)) {
            RequestOptions requestOptions = new RequestOptions();
            if (i > -1) {
                requestOptions.placeholder(i);
            }
            if (i2 > -1) {
                requestOptions.error(i2);
            }
            requestOptions.fitCenter().dontAnimate().diskCacheStrategy(DiskCacheStrategy.DATA);
            GlideRequest listener = GlideApp.with(context).setDefaultRequestOptions(requestOptions).load(str).listener(requestListener);
            if (transformation != null) {
                listener.apply(RequestOptions.bitmapTransform(transformation));
            }
            listener.into(y);
        }
    }

    public static <Y extends Target> void loadGif(@NonNull Context context, @NonNull Y y, Uri uri, RequestListener requestListener) {
        if (AndroidLifecycleUtils.canLoadImage(context)) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.fitCenter().diskCacheStrategy(DiskCacheStrategy.DATA);
            GlideApp.with(context).setDefaultRequestOptions(requestOptions).asGif().load(uri).listener(requestListener).into(y);
        }
    }

    public static <Y extends Target> void loadGif(@NonNull Context context, @NonNull Y y, String str, RequestListener requestListener) {
        if (AndroidLifecycleUtils.canLoadImage(context)) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.fitCenter().diskCacheStrategy(DiskCacheStrategy.DATA);
            GlideApp.with(context).setDefaultRequestOptions(requestOptions).asGif().load(str).listener(requestListener).into(y);
        }
    }

    public static File getCachedFile(@NonNull Context context, String str) throws ExecutionException, InterruptedException {
        if (!AndroidLifecycleUtils.canLoadImage(context)) {
            return null;
        }
        return (File) GlideApp.with(context).downloadOnly().load(str).submit().get();
    }

    public static File getCachedFile(@NonNull Context context, Object obj) throws ExecutionException, InterruptedException {
        if (!AndroidLifecycleUtils.canLoadImage(context)) {
            return null;
        }
        return (File) GlideApp.with(context).downloadOnly().load(obj).submit().get();
    }

    public static void clear(@NonNull Context context, @NonNull ImageView imageView) {
        if (AndroidLifecycleUtils.canLoadImage(context)) {
            GlideApp.with(context).clear((View) imageView);
        }
    }

    public static void clear(@NonNull Context context, Target target) {
        if (AndroidLifecycleUtils.canLoadImage(context)) {
            GlideApp.with(context).clear(target);
        }
    }

    public static GlideRequests getGlideRequestManager(@NonNull Context context) {
        return GlideApp.with(context);
    }

    public static RequestManager getGlideRequestManager(@NonNull Fragment fragment) {
        return GlideApp.with(fragment);
    }
}
