package com.zipow.videobox.util;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.util.GroupAvatarDrawable.GroupAvatarItem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpHost;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.cache.IoUtils;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class ImageUtil {
    private static final int MAX_PIXEL_THRESHOLD = 4262400;
    private static final String TAG = "ImageUtil";

    @NonNull
    public static String getNewFilePathForTakingPhoto() {
        File zoomGalleryPath = getZoomGalleryPath();
        if (zoomGalleryPath != null) {
            if (!zoomGalleryPath.exists()) {
                zoomGalleryPath.mkdirs();
            }
            if (zoomGalleryPath.exists()) {
                String format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(new Date());
                StringBuilder sb = new StringBuilder();
                sb.append(zoomGalleryPath.getAbsolutePath());
                sb.append(File.separator);
                sb.append(format);
                sb.append(".jpg");
                String sb2 = sb.toString();
                int i = 1;
                while (new File(sb2).exists()) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(zoomGalleryPath.getAbsolutePath());
                    sb3.append(File.separator);
                    sb3.append(format);
                    sb3.append("(");
                    i++;
                    sb3.append(i);
                    sb3.append(").jpg");
                    sb2 = sb3.toString();
                }
                return sb2;
            }
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append(AppUtil.getTempPath());
        sb4.append("/capture.jpg");
        return sb4.toString();
    }

    public static File getZoomGalleryPath() {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (externalStoragePublicDirectory == null) {
            return externalStoragePublicDirectory;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(externalStoragePublicDirectory.getAbsolutePath());
        sb.append("/zoom.us");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static boolean translateImageAsSmallJpeg(Context context, Uri uri, int i, String str) throws FileNotFoundException {
        return translateImageAsSmallJpeg(context, uri, i, false, str);
    }

    public static boolean translateImageAsSmallJpegInArea(Context context, Uri uri, int i, String str) throws FileNotFoundException {
        return translateImageAsSmallJpegInArea(context, uri, i, false, str);
    }

    public static boolean translateImageAsSmallJpeg(Context context, Uri uri, int i, boolean z, @Nullable String str) throws FileNotFoundException {
        if (str == null) {
            return false;
        }
        return translateImageAsSmallJpeg(context, uri, i, z, (OutputStream) new FileOutputStream(str));
    }

    public static boolean translateImageAsSmallJpegInArea(Context context, Uri uri, int i, boolean z, @Nullable String str) throws FileNotFoundException {
        if (str == null) {
            return false;
        }
        return translateImageAsSmallJpegInArea(context, uri, i, z, (OutputStream) new FileOutputStream(str));
    }

    public static boolean translateImageAsSmallPngInArea(Context context, Uri uri, int i, String str) throws FileNotFoundException {
        return translateImageAsSmallPngInArea(context, uri, i, false, str);
    }

    public static boolean translateImageAsSmallPngInArea(Context context, Uri uri, int i, boolean z, @Nullable String str) throws FileNotFoundException {
        if (str == null) {
            return false;
        }
        return translateImageAsSmallPngInArea(context, uri, i, z, (OutputStream) new FileOutputStream(str));
    }

    public static boolean translateImageAsSmallJpeg(@Nullable Context context, @Nullable Uri uri, int i, boolean z, @Nullable OutputStream outputStream) {
        if (context == null || uri == null || outputStream == null || i <= 0) {
            return false;
        }
        try {
            Bitmap translateImageAsSmallBitmap = translateImageAsSmallBitmap(context, uri, i, false, z);
            if (translateImageAsSmallBitmap == null) {
                return false;
            }
            boolean compress = translateImageAsSmallBitmap.compress(CompressFormat.JPEG, 60, outputStream);
            translateImageAsSmallBitmap.recycle();
            if (!compress) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean translateImageAsSmallImageInArea(@Nullable Context context, @Nullable Uri uri, int i, boolean z, @Nullable OutputStream outputStream, CompressFormat compressFormat) {
        if (context == null || uri == null || outputStream == null || i <= 0) {
            return false;
        }
        try {
            Bitmap translateImageAsSmallBitmapInArea = translateImageAsSmallBitmapInArea(context, uri, i, false, z);
            if (translateImageAsSmallBitmapInArea == null) {
                return false;
            }
            boolean compress = translateImageAsSmallBitmapInArea.compress(compressFormat, 60, outputStream);
            translateImageAsSmallBitmapInArea.recycle();
            if (!compress) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean translateImageAsSmallPngInArea(Context context, Uri uri, int i, boolean z, OutputStream outputStream) {
        return translateImageAsSmallImageInArea(context, uri, i, z, outputStream, CompressFormat.PNG);
    }

    public static boolean translateImageAsSmallJpegInArea(Context context, Uri uri, int i, boolean z, OutputStream outputStream) {
        return translateImageAsSmallImageInArea(context, uri, i, z, outputStream, CompressFormat.JPEG);
    }

    @Nullable
    public static Bitmap translateImageAsSmallBitmap(Context context, Uri uri, int i, boolean z) {
        return translateImageAsSmallBitmap(context, uri, i, z, false);
    }

    @Nullable
    public static Bitmap translateImageAsSmallBitmap(Context context, String str, int i, boolean z) {
        return translateImageAsSmallBitmap(context, str, i, z, false);
    }

    @Nullable
    public static Bitmap translateImageAsSmallBitmapInArea(Context context, Uri uri, int i, boolean z) {
        return translateImageAsSmallBitmapInArea(context, uri, i, z, false);
    }

    @Nullable
    public static Bitmap translateImageAsSmallBitmap(@Nullable Context context, @Nullable Uri uri, int i, boolean z, boolean z2) {
        InputStream inputStream;
        Uri uri2;
        InputStream inputStream2 = null;
        if (context == null || uri == null || i <= 0) {
            return null;
        }
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            String scheme = uri.getScheme();
            String uri3 = uri.toString();
            if (!HttpHost.DEFAULT_SCHEME_NAME.equalsIgnoreCase(scheme)) {
                if (!"https".equalsIgnoreCase(scheme)) {
                    if (!StringUtil.isEmptyOrNull(scheme)) {
                        inputStream = context.getContentResolver().openInputStream(uri);
                        uri2 = uri;
                    } else {
                        InputStream fileInputStream = new FileInputStream(uri3);
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append("file://");
                            sb.append(uri3);
                            uri2 = Uri.parse(sb.toString());
                            inputStream = fileInputStream;
                        } catch (Exception unused) {
                            inputStream = fileInputStream;
                            IoUtils.closeSilently(inputStream);
                            return null;
                        } catch (Throwable th) {
                            th = th;
                            inputStream2 = fileInputStream;
                            IoUtils.closeSilently(inputStream2);
                            throw th;
                        }
                    }
                    BitmapFactory.decodeStream(inputStream, null, options);
                    Bitmap translateImageAsSmallBitmap = translateImageAsSmallBitmap(context, uri2, options.outWidth, options.outHeight, i, z, z2);
                    IoUtils.closeSilently(inputStream);
                    return translateImageAsSmallBitmap;
                }
            }
            String downloadFile = FileUtils.downloadFile(context, new URL(uri.toString()));
            if (downloadFile == null) {
                IoUtils.closeSilently(null);
                return null;
            }
            inputStream = new FileInputStream(downloadFile);
            try {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("file://");
                sb2.append(downloadFile);
                uri2 = Uri.parse(sb2.toString());
                BitmapFactory.decodeStream(inputStream, null, options);
                Bitmap translateImageAsSmallBitmap2 = translateImageAsSmallBitmap(context, uri2, options.outWidth, options.outHeight, i, z, z2);
                IoUtils.closeSilently(inputStream);
                return translateImageAsSmallBitmap2;
            } catch (Exception unused2) {
                IoUtils.closeSilently(inputStream);
                return null;
            } catch (Throwable th2) {
                th = th2;
                inputStream2 = inputStream;
                IoUtils.closeSilently(inputStream2);
                throw th;
            }
        } catch (Exception unused3) {
            inputStream = null;
            IoUtils.closeSilently(inputStream);
            return null;
        } catch (Throwable th3) {
            th = th3;
            IoUtils.closeSilently(inputStream2);
            throw th;
        }
    }

    @Nullable
    public static Bitmap translateImageAsSmallBitmap(@Nullable Context context, @Nullable String str, int i, boolean z, boolean z2) {
        if (context == null || str == null || !new File(str).exists() || i <= 0) {
            return null;
        }
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            return translateImageAsSmallBitmap(context, str, options.outWidth, options.outHeight, i, z, z2);
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a7 A[Catch:{ Exception -> 0x00c2, all -> 0x00b9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a9 A[Catch:{ Exception -> 0x00c2, all -> 0x00b9 }] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap translateImageAsSmallBitmapInArea(@androidx.annotation.Nullable android.content.Context r10, @androidx.annotation.Nullable android.net.Uri r11, int r12, boolean r13, boolean r14) {
        /*
            r0 = 0
            if (r10 == 0) goto L_0x00c6
            if (r11 != 0) goto L_0x0007
            goto L_0x00c6
        L_0x0007:
            if (r12 > 0) goto L_0x000a
            return r0
        L_0x000a:
            android.graphics.BitmapFactory$Options r1 = new android.graphics.BitmapFactory$Options     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            r1.<init>()     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            r2 = 1
            r1.inJustDecodeBounds = r2     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            java.lang.String r2 = r11.getScheme()     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            java.lang.String r3 = r11.toString()     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            java.lang.String r4 = "http"
            boolean r4 = r4.equalsIgnoreCase(r2)     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            if (r4 != 0) goto L_0x005d
            java.lang.String r4 = "https"
            boolean r4 = r4.equalsIgnoreCase(r2)     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            if (r4 == 0) goto L_0x002b
            goto L_0x005d
        L_0x002b:
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r2)     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            if (r2 != 0) goto L_0x003b
            android.content.ContentResolver r2 = r10.getContentResolver()     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            java.io.InputStream r2 = r2.openInputStream(r11)     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            r4 = r11
            goto L_0x008b
        L_0x003b:
            java.io.FileInputStream r11 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            r11.<init>(r3)     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r2.<init>()     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.lang.String r4 = "file://"
            r2.append(r4)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r2.append(r3)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r4 = r2
            r2 = r11
            goto L_0x008b
        L_0x0058:
            r10 = move-exception
            r0 = r11
            goto L_0x00bd
        L_0x005b:
            r2 = r11
            goto L_0x00c2
        L_0x005d:
            java.net.URL r2 = new java.net.URL     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            r2.<init>(r11)     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            java.lang.String r11 = p021us.zoom.androidlib.util.FileUtils.downloadFile(r10, r2)     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            if (r11 != 0) goto L_0x0070
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r0)
            return r0
        L_0x0070:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            r2.<init>(r11)     // Catch:{ Exception -> 0x00c1, all -> 0x00bc }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            r3.<init>()     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            java.lang.String r4 = "file://"
            r3.append(r4)     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            r3.append(r11)     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            java.lang.String r11 = r3.toString()     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            android.net.Uri r11 = android.net.Uri.parse(r11)     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            r4 = r11
        L_0x008b:
            android.graphics.BitmapFactory.decodeStream(r2, r0, r1)     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            float r11 = (float) r12     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            int r12 = r1.outWidth     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            float r12 = (float) r12     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            int r3 = r1.outHeight     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            float r3 = (float) r3     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            float r12 = r12 / r3
            float r11 = r11 * r12
            double r11 = (double) r11     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            double r11 = java.lang.Math.sqrt(r11)     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            int r11 = (int) r11     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            int r12 = r1.outHeight     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            int r12 = r12 * r11
            int r3 = r1.outWidth     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            int r12 = r12 / r3
            if (r11 <= r12) goto L_0x00a9
            r7 = r11
            goto L_0x00aa
        L_0x00a9:
            r7 = r12
        L_0x00aa:
            int r5 = r1.outWidth     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            int r6 = r1.outHeight     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            r3 = r10
            r8 = r13
            r9 = r14
            android.graphics.Bitmap r10 = translateImageAsSmallBitmap(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ Exception -> 0x00c2, all -> 0x00b9 }
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r2)
            return r10
        L_0x00b9:
            r10 = move-exception
            r0 = r2
            goto L_0x00bd
        L_0x00bc:
            r10 = move-exception
        L_0x00bd:
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r0)
            throw r10
        L_0x00c1:
            r2 = r0
        L_0x00c2:
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r2)
            return r0
        L_0x00c6:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ImageUtil.translateImageAsSmallBitmapInArea(android.content.Context, android.net.Uri, int, boolean, boolean):android.graphics.Bitmap");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:58:?, code lost:
        p021us.zoom.androidlib.cache.IoUtils.closeSilently(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00e7, code lost:
        if (isJpegFile(r3) == false) goto L_0x00fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
        r3 = getJpegRotation(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00ed, code lost:
        if (r3 <= 0) goto L_0x00fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00ef, code lost:
        r3 = rotateBitmap(r0, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00f3, code lost:
        if (r3 == null) goto L_0x00fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00f5, code lost:
        if (r3 == r0) goto L_0x00fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00f7, code lost:
        r0.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00fa, code lost:
        r0 = r3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0179 A[Catch:{ all -> 0x0186 }] */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0181 A[ADDED_TO_REGION, SYNTHETIC] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.graphics.Bitmap translateImageAsSmallBitmap(@androidx.annotation.Nullable android.content.Context r15, @androidx.annotation.Nullable android.net.Uri r16, int r17, int r18, int r19, boolean r20, boolean r21) {
        /*
            r0 = r15
            r1 = r19
            r2 = 0
            if (r0 == 0) goto L_0x0194
            if (r16 != 0) goto L_0x000a
            goto L_0x0194
        L_0x000a:
            if (r1 > 0) goto L_0x000d
            return r2
        L_0x000d:
            java.lang.String r3 = getPathFromUri(r15, r16)     // Catch:{ Exception -> 0x0193 }
            r4 = 3
            r5 = 1
            if (r21 == 0) goto L_0x003e
            r7 = r17
            r8 = r18
            r6 = 1
        L_0x001a:
            if (r6 >= r4) goto L_0x002c
            int r9 = r6 + 1
            int r10 = r17 / r9
            int r11 = r1 * 3
            int r12 = r11 / 4
            if (r10 < r12) goto L_0x005f
            int r9 = r18 / r9
            int r11 = r11 / 4
            if (r9 < r11) goto L_0x005f
        L_0x002c:
            int r9 = r1 * 3
            int r10 = r9 / 2
            if (r7 < r10) goto L_0x005f
            int r9 = r9 / 2
            if (r8 >= r9) goto L_0x0037
            goto L_0x005f
        L_0x0037:
            int r6 = r6 + 1
            int r7 = r17 / r6
            int r8 = r18 / r6
            goto L_0x001a
        L_0x003e:
            r7 = r17
            r8 = r18
            r6 = 1
        L_0x0043:
            if (r6 >= r4) goto L_0x0055
            int r9 = r6 + 1
            int r10 = r17 / r9
            int r11 = r1 * 3
            int r12 = r11 / 4
            if (r10 >= r12) goto L_0x0055
            int r9 = r18 / r9
            int r11 = r11 / 4
            if (r9 < r11) goto L_0x005f
        L_0x0055:
            int r9 = r1 * 3
            int r10 = r9 / 2
            if (r7 >= r10) goto L_0x018b
            int r9 = r9 / 2
            if (r8 >= r9) goto L_0x018b
        L_0x005f:
            android.graphics.BitmapFactory$Options r4 = new android.graphics.BitmapFactory$Options     // Catch:{ Exception -> 0x0193 }
            r4.<init>()     // Catch:{ Exception -> 0x0193 }
            r4.inSampleSize = r6     // Catch:{ Exception -> 0x0193 }
            r9 = r8
            r8 = r7
            r7 = r6
            r6 = r16
        L_0x006b:
            java.lang.String r10 = r6.getScheme()     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            java.lang.String r11 = r6.toString()     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            java.lang.String r12 = "http"
            boolean r12 = r12.equalsIgnoreCase(r10)     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            if (r12 != 0) goto L_0x00ae
            java.lang.String r12 = "https"
            boolean r12 = r12.equalsIgnoreCase(r10)     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            if (r12 == 0) goto L_0x0084
            goto L_0x00ae
        L_0x0084:
            boolean r10 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r10)     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            if (r10 != 0) goto L_0x0093
            android.content.ContentResolver r10 = r15.getContentResolver()     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            java.io.InputStream r10 = r10.openInputStream(r6)     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            goto L_0x00dc
        L_0x0093:
            java.io.FileInputStream r10 = new java.io.FileInputStream     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            r10.<init>(r11)     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ OutOfMemoryError -> 0x016e }
            r12.<init>()     // Catch:{ OutOfMemoryError -> 0x016e }
            java.lang.String r13 = "file://"
            r12.append(r13)     // Catch:{ OutOfMemoryError -> 0x016e }
            r12.append(r11)     // Catch:{ OutOfMemoryError -> 0x016e }
            java.lang.String r11 = r12.toString()     // Catch:{ OutOfMemoryError -> 0x016e }
            android.net.Uri r6 = android.net.Uri.parse(r11)     // Catch:{ OutOfMemoryError -> 0x016e }
            goto L_0x00dc
        L_0x00ae:
            java.net.URL r10 = new java.net.URL     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            java.lang.String r11 = r6.toString()     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            r10.<init>(r11)     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            java.lang.String r10 = p021us.zoom.androidlib.util.FileUtils.downloadFile(r15, r10)     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            if (r10 != 0) goto L_0x00c1
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r2)     // Catch:{ Exception -> 0x0193 }
            return r2
        L_0x00c1:
            java.io.FileInputStream r11 = new java.io.FileInputStream     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            r11.<init>(r10)     // Catch:{ OutOfMemoryError -> 0x016d, all -> 0x016a }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ OutOfMemoryError -> 0x0168, all -> 0x0165 }
            r12.<init>()     // Catch:{ OutOfMemoryError -> 0x0168, all -> 0x0165 }
            java.lang.String r13 = "file://"
            r12.append(r13)     // Catch:{ OutOfMemoryError -> 0x0168, all -> 0x0165 }
            r12.append(r10)     // Catch:{ OutOfMemoryError -> 0x0168, all -> 0x0165 }
            java.lang.String r10 = r12.toString()     // Catch:{ OutOfMemoryError -> 0x0168, all -> 0x0165 }
            android.net.Uri r6 = android.net.Uri.parse(r10)     // Catch:{ OutOfMemoryError -> 0x0168, all -> 0x0165 }
            r10 = r11
        L_0x00dc:
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeStream(r10, r2, r4)     // Catch:{ OutOfMemoryError -> 0x016e }
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r10)     // Catch:{ Exception -> 0x0193 }
            boolean r4 = isJpegFile(r3)     // Catch:{ Exception -> 0x0193 }
            if (r4 == 0) goto L_0x00fb
            int r3 = getJpegRotation(r3)     // Catch:{ Exception -> 0x00fb }
            if (r3 <= 0) goto L_0x00fb
            android.graphics.Bitmap r3 = rotateBitmap(r0, r3)     // Catch:{ Exception -> 0x00fb }
            if (r3 == 0) goto L_0x00fb
            if (r3 == r0) goto L_0x00fb
            r0.recycle()     // Catch:{ Exception -> 0x00fb }
            r0 = r3
        L_0x00fb:
            if (r20 == 0) goto L_0x0101
            if (r8 > r1) goto L_0x0106
            if (r9 > r1) goto L_0x0106
        L_0x0101:
            if (r21 == 0) goto L_0x0164
            if (r8 != r9) goto L_0x0106
            goto L_0x0164
        L_0x0106:
            int r3 = r0.getWidth()     // Catch:{ Exception -> 0x0193 }
            int r4 = r0.getHeight()     // Catch:{ Exception -> 0x0193 }
            if (r3 < r4) goto L_0x0115
            int r4 = r4 * r1
            int r3 = r4 / r3
            goto L_0x011b
        L_0x0115:
            int r3 = r3 * r1
            int r3 = r3 / r4
            r14 = r3
            r3 = r1
            r1 = r14
        L_0x011b:
            r4 = 0
            if (r21 == 0) goto L_0x0132
            if (r1 <= r3) goto L_0x0126
            int r5 = r1 - r3
            int r5 = r5 / 2
            r6 = 0
            goto L_0x012c
        L_0x0126:
            int r5 = r3 - r1
            int r5 = r5 / 2
            r6 = r5
            r5 = 0
        L_0x012c:
            int r1 = java.lang.Math.min(r1, r3)     // Catch:{ Exception -> 0x0193 }
            r3 = r1
            goto L_0x0134
        L_0x0132:
            r5 = 0
            r6 = 0
        L_0x0134:
            android.graphics.Bitmap$Config r7 = r0.getConfig()     // Catch:{ OutOfMemoryError -> 0x0163 }
            android.graphics.Bitmap r7 = android.graphics.Bitmap.createBitmap(r1, r3, r7)     // Catch:{ OutOfMemoryError -> 0x0163 }
            android.graphics.Canvas r8 = new android.graphics.Canvas     // Catch:{ Exception -> 0x0193 }
            r8.<init>(r7)     // Catch:{ Exception -> 0x0193 }
            android.graphics.Paint r9 = new android.graphics.Paint     // Catch:{ Exception -> 0x0193 }
            r9.<init>()     // Catch:{ Exception -> 0x0193 }
            android.graphics.Rect r10 = new android.graphics.Rect     // Catch:{ Exception -> 0x0193 }
            int r11 = r0.getWidth()     // Catch:{ Exception -> 0x0193 }
            int r12 = r0.getHeight()     // Catch:{ Exception -> 0x0193 }
            r10.<init>(r4, r4, r11, r12)     // Catch:{ Exception -> 0x0193 }
            android.graphics.Rect r4 = new android.graphics.Rect     // Catch:{ Exception -> 0x0193 }
            int r11 = -r5
            int r12 = -r6
            int r1 = r1 + r5
            int r3 = r3 + r6
            r4.<init>(r11, r12, r1, r3)     // Catch:{ Exception -> 0x0193 }
            r8.drawBitmap(r0, r10, r4, r9)     // Catch:{ Exception -> 0x0193 }
            r0.recycle()     // Catch:{ Exception -> 0x0193 }
            return r7
        L_0x0163:
            return r0
        L_0x0164:
            return r0
        L_0x0165:
            r0 = move-exception
            r10 = r11
            goto L_0x0187
        L_0x0168:
            r10 = r11
            goto L_0x016e
        L_0x016a:
            r0 = move-exception
            r10 = r2
            goto L_0x0187
        L_0x016d:
            r10 = r2
        L_0x016e:
            int r7 = r7 + r5
            int r8 = r17 / r7
            int r9 = r18 / r7
            r4.inSampleSize = r7     // Catch:{ all -> 0x0186 }
            int r11 = r1 / 4
            if (r8 >= r11) goto L_0x0181
            int r11 = r1 / 4
            if (r9 >= r11) goto L_0x0181
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r10)     // Catch:{ Exception -> 0x0193 }
            return r2
        L_0x0181:
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r10)     // Catch:{ Exception -> 0x0193 }
            goto L_0x006b
        L_0x0186:
            r0 = move-exception
        L_0x0187:
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r10)     // Catch:{ Exception -> 0x0193 }
            throw r0     // Catch:{ Exception -> 0x0193 }
        L_0x018b:
            int r6 = r6 + 1
            int r7 = r17 / r6
            int r8 = r18 / r6
            goto L_0x0043
        L_0x0193:
            return r2
        L_0x0194:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ImageUtil.translateImageAsSmallBitmap(android.content.Context, android.net.Uri, int, int, int, boolean, boolean):android.graphics.Bitmap");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0075, code lost:
        if (isJpegFile(r15) == false) goto L_0x0089;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r0 = getJpegRotation(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x007b, code lost:
        if (r0 <= 0) goto L_0x0089;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007d, code lost:
        r0 = rotateBitmap(r3, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0081, code lost:
        if (r0 == null) goto L_0x0089;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0083, code lost:
        if (r0 == r3) goto L_0x0089;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0085, code lost:
        r3.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0088, code lost:
        r3 = r0;
     */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.graphics.Bitmap translateImageAsSmallBitmap(@androidx.annotation.Nullable android.content.Context r14, @androidx.annotation.Nullable java.lang.String r15, int r16, int r17, int r18, boolean r19, boolean r20) {
        /*
            r0 = r15
            r1 = r18
            r2 = 0
            if (r14 == 0) goto L_0x010c
            if (r0 == 0) goto L_0x010c
            java.io.File r3 = new java.io.File
            r3.<init>(r15)
            boolean r3 = r3.exists()
            if (r3 != 0) goto L_0x0015
            goto L_0x010c
        L_0x0015:
            if (r1 > 0) goto L_0x0018
            return r2
        L_0x0018:
            r3 = 3
            r4 = 1
            if (r20 == 0) goto L_0x0045
            r6 = r16
            r7 = r17
            r5 = 1
        L_0x0021:
            if (r5 >= r3) goto L_0x0033
            int r8 = r5 + 1
            int r9 = r16 / r8
            int r10 = r1 * 3
            int r11 = r10 / 4
            if (r9 < r11) goto L_0x0066
            int r8 = r17 / r8
            int r10 = r10 / 4
            if (r8 < r10) goto L_0x0066
        L_0x0033:
            int r8 = r1 * 3
            int r9 = r8 / 2
            if (r6 < r9) goto L_0x0066
            int r8 = r8 / 2
            if (r7 >= r8) goto L_0x003e
            goto L_0x0066
        L_0x003e:
            int r5 = r5 + 1
            int r6 = r16 / r5
            int r7 = r17 / r5
            goto L_0x0021
        L_0x0045:
            r6 = r16
            r7 = r17
            r5 = 1
        L_0x004a:
            if (r5 >= r3) goto L_0x005c
            int r8 = r5 + 1
            int r9 = r16 / r8
            int r10 = r1 * 3
            int r11 = r10 / 4
            if (r9 >= r11) goto L_0x005c
            int r8 = r17 / r8
            int r10 = r10 / 4
            if (r8 < r10) goto L_0x0066
        L_0x005c:
            int r8 = r1 * 3
            int r9 = r8 / 2
            if (r6 >= r9) goto L_0x0103
            int r8 = r8 / 2
            if (r7 >= r8) goto L_0x0103
        L_0x0066:
            android.graphics.BitmapFactory$Options r3 = new android.graphics.BitmapFactory$Options     // Catch:{ Exception -> 0x010b }
            r3.<init>()     // Catch:{ Exception -> 0x010b }
            r3.inSampleSize = r5     // Catch:{ Exception -> 0x010b }
        L_0x006d:
            android.graphics.Bitmap r3 = android.graphics.BitmapFactory.decodeFile(r15, r3)     // Catch:{ OutOfMemoryError -> 0x00f3 }
            boolean r4 = isJpegFile(r15)     // Catch:{ Exception -> 0x010b }
            if (r4 == 0) goto L_0x0089
            int r0 = getJpegRotation(r15)     // Catch:{ Exception -> 0x0089 }
            if (r0 <= 0) goto L_0x0089
            android.graphics.Bitmap r0 = rotateBitmap(r3, r0)     // Catch:{ Exception -> 0x0089 }
            if (r0 == 0) goto L_0x0089
            if (r0 == r3) goto L_0x0089
            r3.recycle()     // Catch:{ Exception -> 0x0089 }
            r3 = r0
        L_0x0089:
            if (r19 == 0) goto L_0x008f
            if (r6 > r1) goto L_0x0094
            if (r7 > r1) goto L_0x0094
        L_0x008f:
            if (r20 == 0) goto L_0x00f2
            if (r6 != r7) goto L_0x0094
            goto L_0x00f2
        L_0x0094:
            int r0 = r3.getWidth()     // Catch:{ Exception -> 0x010b }
            int r4 = r3.getHeight()     // Catch:{ Exception -> 0x010b }
            if (r0 < r4) goto L_0x00a6
            int r4 = r4 * r1
            int r0 = r4 / r0
            r13 = r1
            r1 = r0
            r0 = r13
            goto L_0x00a9
        L_0x00a6:
            int r0 = r0 * r1
            int r0 = r0 / r4
        L_0x00a9:
            r4 = 0
            if (r20 == 0) goto L_0x00c0
            if (r0 <= r1) goto L_0x00b4
            int r5 = r0 - r1
            int r5 = r5 / 2
            r6 = 0
            goto L_0x00ba
        L_0x00b4:
            int r5 = r1 - r0
            int r5 = r5 / 2
            r6 = r5
            r5 = 0
        L_0x00ba:
            int r0 = java.lang.Math.min(r0, r1)     // Catch:{ Exception -> 0x010b }
            r1 = r0
            goto L_0x00c2
        L_0x00c0:
            r5 = 0
            r6 = 0
        L_0x00c2:
            android.graphics.Bitmap$Config r7 = r3.getConfig()     // Catch:{ OutOfMemoryError -> 0x00f1 }
            android.graphics.Bitmap r7 = android.graphics.Bitmap.createBitmap(r0, r1, r7)     // Catch:{ OutOfMemoryError -> 0x00f1 }
            android.graphics.Canvas r8 = new android.graphics.Canvas     // Catch:{ Exception -> 0x010b }
            r8.<init>(r7)     // Catch:{ Exception -> 0x010b }
            android.graphics.Paint r9 = new android.graphics.Paint     // Catch:{ Exception -> 0x010b }
            r9.<init>()     // Catch:{ Exception -> 0x010b }
            android.graphics.Rect r10 = new android.graphics.Rect     // Catch:{ Exception -> 0x010b }
            int r11 = r3.getWidth()     // Catch:{ Exception -> 0x010b }
            int r12 = r3.getHeight()     // Catch:{ Exception -> 0x010b }
            r10.<init>(r4, r4, r11, r12)     // Catch:{ Exception -> 0x010b }
            android.graphics.Rect r4 = new android.graphics.Rect     // Catch:{ Exception -> 0x010b }
            int r11 = -r5
            int r12 = -r6
            int r0 = r0 + r5
            int r1 = r1 + r6
            r4.<init>(r11, r12, r0, r1)     // Catch:{ Exception -> 0x010b }
            r8.drawBitmap(r3, r10, r4, r9)     // Catch:{ Exception -> 0x010b }
            r3.recycle()     // Catch:{ Exception -> 0x010b }
            return r7
        L_0x00f1:
            return r3
        L_0x00f2:
            return r3
        L_0x00f3:
            int r5 = r5 + r4
            int r6 = r16 / r5
            int r7 = r17 / r5
            r3.inSampleSize = r5     // Catch:{ Exception -> 0x010b }
            int r8 = r1 / 4
            if (r6 >= r8) goto L_0x006d
            int r8 = r1 / 4
            if (r7 >= r8) goto L_0x006d
            return r2
        L_0x0103:
            int r5 = r5 + 1
            int r6 = r16 / r5
            int r7 = r17 / r5
            goto L_0x004a
        L_0x010b:
            return r2
        L_0x010c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ImageUtil.translateImageAsSmallBitmap(android.content.Context, java.lang.String, int, int, int, boolean, boolean):android.graphics.Bitmap");
    }

    public static boolean compressImage(@NonNull String str, int i) {
        return compressImage(str, null, i);
    }

    public static boolean compressImage(@NonNull String str, String str2, int i) {
        FileOutputStream fileOutputStream;
        Throwable th;
        if (i <= 0) {
            return false;
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = str;
        }
        if (new File(str).length() >= ((long) i)) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            int i2 = 1;
            while (true) {
                options.inSampleSize = i2;
                BitmapFactory.decodeFile(str, options);
                if (options.outWidth * options.outHeight <= MAX_PIXEL_THRESHOLD) {
                    break;
                }
                i2 <<= 1;
            }
            Bitmap decodeFile = ZMBitmapFactory.decodeFile(str, Config.RGB_565, i2);
            if (decodeFile == null) {
                return false;
            }
            if (isJpegFile(str)) {
                try {
                    int jpegRotation = getJpegRotation(str);
                    if (jpegRotation > 0) {
                        Bitmap rotateBitmap = rotateBitmap(decodeFile, jpegRotation);
                        if (!(rotateBitmap == null || rotateBitmap == decodeFile)) {
                            decodeFile.recycle();
                            decodeFile = rotateBitmap;
                        }
                    }
                } catch (Exception unused) {
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i3 = 75;
            int i4 = 0;
            do {
                if (i3 <= 0) {
                    Matrix matrix = new Matrix();
                    matrix.setScale(0.5f, 0.5f);
                    decodeFile = scaleBitmap(decodeFile, matrix);
                    byteArrayOutputStream.reset();
                    try {
                        decodeFile.compress(CompressFormat.JPEG, 100 - (i4 * 15), byteArrayOutputStream);
                    } catch (Exception unused2) {
                    }
                    i4++;
                    if (i4 >= 2) {
                        break;
                    }
                }
                byteArrayOutputStream.reset();
                try {
                    decodeFile.compress(CompressFormat.JPEG, i3, byteArrayOutputStream);
                } catch (Exception unused3) {
                }
                i3 -= 25;
            } while (byteArrayOutputStream.size() > i);
            if (decodeFile != null && !decodeFile.isRecycled()) {
                decodeFile.recycle();
            }
            FileOutputStream fileOutputStream2 = null;
            try {
                if (FileUtils.createFile(str2, false)) {
                    fileOutputStream = new FileOutputStream(str2);
                    try {
                        fileOutputStream.write(byteArrayOutputStream.toByteArray());
                        fileOutputStream.flush();
                        IoUtils.closeSilently(fileOutputStream);
                        IoUtils.closeSilently(byteArrayOutputStream);
                        return true;
                    } catch (Exception unused4) {
                        fileOutputStream2 = fileOutputStream;
                        IoUtils.closeSilently(fileOutputStream2);
                        IoUtils.closeSilently(byteArrayOutputStream);
                        return false;
                    } catch (Throwable th2) {
                        th = th2;
                        IoUtils.closeSilently(fileOutputStream);
                        IoUtils.closeSilently(byteArrayOutputStream);
                        throw th;
                    }
                } else {
                    IoUtils.closeSilently(null);
                    IoUtils.closeSilently(byteArrayOutputStream);
                    return false;
                }
            } catch (Exception unused5) {
                IoUtils.closeSilently(fileOutputStream2);
                IoUtils.closeSilently(byteArrayOutputStream);
                return false;
            } catch (Throwable th3) {
                fileOutputStream = null;
                th = th3;
                IoUtils.closeSilently(fileOutputStream);
                IoUtils.closeSilently(byteArrayOutputStream);
                throw th;
            }
        } else if (!str2.equals(str)) {
            return FileUtils.copyFile(str, str2);
        } else {
            return true;
        }
    }

    @Nullable
    public static Bitmap scaleBitmap(@Nullable Bitmap bitmap, @Nullable Matrix matrix) {
        Bitmap bitmap2;
        if (bitmap == null) {
            return null;
        }
        if (matrix == null) {
            return bitmap;
        }
        try {
            bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception unused) {
            bitmap2 = null;
        }
        if (bitmap2 != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = bitmap2;
        }
        return bitmap;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0078, code lost:
        r10 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0079, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x007d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x007e, code lost:
        r8 = r0;
        r0 = r10;
        r10 = r8;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0078 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:18:0x0042] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] getPreviewImgData(@androidx.annotation.Nullable java.lang.String r9, int r10) {
        /*
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r9)
            r1 = 0
            if (r0 != 0) goto L_0x00d5
            if (r10 > 0) goto L_0x000b
            goto L_0x00d5
        L_0x000b:
            java.io.File r0 = new java.io.File
            r0.<init>(r9)
            boolean r2 = r0.exists()
            if (r2 == 0) goto L_0x00d4
            boolean r2 = r0.isFile()
            if (r2 != 0) goto L_0x001e
            goto L_0x00d4
        L_0x001e:
            android.graphics.BitmapFactory$Options r2 = new android.graphics.BitmapFactory$Options
            r2.<init>()
            r3 = 1
            r2.inJustDecodeBounds = r3
            android.graphics.BitmapFactory.decodeFile(r9, r2)     // Catch:{ Exception -> 0x00d3 }
            int r4 = r2.outHeight
            if (r4 == 0) goto L_0x00d2
            int r2 = r2.outWidth
            if (r2 != 0) goto L_0x0033
            goto L_0x00d2
        L_0x0033:
            long r4 = r0.length()
            long r6 = (long) r10
            r2 = 0
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 > 0) goto L_0x0091
            java.io.ByteArrayOutputStream r9 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0090 }
            r9.<init>()     // Catch:{ Exception -> 0x0090 }
            java.io.FileInputStream r10 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x007b, all -> 0x0078 }
            r10.<init>(r0)     // Catch:{ Throwable -> 0x007b, all -> 0x0078 }
            r0 = 8192(0x2000, float:1.14794E-41)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x0063, all -> 0x0060 }
        L_0x004b:
            int r3 = r10.read(r0)     // Catch:{ Throwable -> 0x0063, all -> 0x0060 }
            if (r3 <= 0) goto L_0x0055
            r9.write(r0, r2, r3)     // Catch:{ Throwable -> 0x0063, all -> 0x0060 }
            goto L_0x004b
        L_0x0055:
            byte[] r0 = r9.toByteArray()     // Catch:{ Throwable -> 0x0063, all -> 0x0060 }
            r10.close()     // Catch:{ Throwable -> 0x007b, all -> 0x0078 }
            r9.close()     // Catch:{ Exception -> 0x0090 }
            return r0
        L_0x0060:
            r0 = move-exception
            r2 = r1
            goto L_0x0069
        L_0x0063:
            r0 = move-exception
            throw r0     // Catch:{ all -> 0x0065 }
        L_0x0065:
            r2 = move-exception
            r8 = r2
            r2 = r0
            r0 = r8
        L_0x0069:
            if (r2 == 0) goto L_0x0074
            r10.close()     // Catch:{ Throwable -> 0x006f, all -> 0x0078 }
            goto L_0x0077
        L_0x006f:
            r10 = move-exception
            r2.addSuppressed(r10)     // Catch:{ Throwable -> 0x007b, all -> 0x0078 }
            goto L_0x0077
        L_0x0074:
            r10.close()     // Catch:{ Throwable -> 0x007b, all -> 0x0078 }
        L_0x0077:
            throw r0     // Catch:{ Throwable -> 0x007b, all -> 0x0078 }
        L_0x0078:
            r10 = move-exception
            r0 = r1
            goto L_0x0081
        L_0x007b:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x007d }
        L_0x007d:
            r0 = move-exception
            r8 = r0
            r0 = r10
            r10 = r8
        L_0x0081:
            if (r0 == 0) goto L_0x008c
            r9.close()     // Catch:{ Throwable -> 0x0087 }
            goto L_0x008f
        L_0x0087:
            r9 = move-exception
            r0.addSuppressed(r9)     // Catch:{ Exception -> 0x0090 }
            goto L_0x008f
        L_0x008c:
            r9.close()     // Catch:{ Exception -> 0x0090 }
        L_0x008f:
            throw r10     // Catch:{ Exception -> 0x0090 }
        L_0x0090:
            return r1
        L_0x0091:
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
        L_0x0096:
            r1 = 40000(0x9c40, float:5.6052E-41)
            int r1 = r1 / r3
            android.graphics.Bitmap r1 = com.zipow.videobox.util.ZMBitmapFactory.decodeFile(r9, r1, r2, r2)
            boolean r4 = isJpegFile(r9)
            if (r4 == 0) goto L_0x00b8
            int r4 = getJpegRotation(r9)     // Catch:{ Exception -> 0x00b8 }
            if (r4 <= 0) goto L_0x00b8
            if (r1 == 0) goto L_0x00b8
            android.graphics.Bitmap r4 = rotateBitmap(r1, r4)     // Catch:{ Exception -> 0x00b8 }
            if (r4 == 0) goto L_0x00b8
            if (r4 == r1) goto L_0x00b8
            r1.recycle()     // Catch:{ Exception -> 0x00b8 }
            r1 = r4
        L_0x00b8:
            r0.reset()
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.JPEG
            r5 = 75
            r1.compress(r4, r5, r0)
            int r3 = r3 * 4
            int r1 = r0.size()
            if (r1 > r10) goto L_0x0096
            byte[] r9 = r0.toByteArray()
            r0.close()     // Catch:{ IOException -> 0x00d1 }
        L_0x00d1:
            return r9
        L_0x00d2:
            return r1
        L_0x00d3:
            return r1
        L_0x00d4:
            return r1
        L_0x00d5:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ImageUtil.getPreviewImgData(java.lang.String, int):byte[]");
    }

    private static boolean isJpegFile(@Nullable String str) {
        return str != null && (str.endsWith(".jpg") || str.endsWith(".JPG") || str.endsWith(".jpeg") || str.endsWith(".JPEG"));
    }

    public static String getImageMimeType(@NonNull String str) {
        Throwable th;
        Throwable th2;
        String str2 = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            try {
                byte[] bArr = new byte[10];
                fileInputStream.read(bArr);
                byte b = bArr[0];
                byte b2 = bArr[1];
                byte b3 = bArr[2];
                byte b4 = bArr[3];
                byte b5 = bArr[6];
                byte b6 = bArr[7];
                byte b7 = bArr[8];
                byte b8 = bArr[9];
                if (b == 71 && b2 == 73 && b3 == 70) {
                    str2 = AndroidAppUtil.IMAGE_MIME_TYPE_GIF;
                } else if (b2 == 80 && b3 == 78 && b4 == 71) {
                    str2 = AndroidAppUtil.IMAGE_MIME_TYPE_PNG;
                } else if (b5 == 74 && b6 == 70 && b7 == 73 && b8 == 70) {
                    str2 = AndroidAppUtil.IMAGE_MIME_TYPE_JPG;
                }
                fileInputStream.close();
                if (str2 == null) {
                    try {
                        int lastIndexOf = str.trim().lastIndexOf(".");
                        if (lastIndexOf != -1) {
                            String substring = str.substring(lastIndexOf + 1);
                            if (TextUtils.equals("jpg", substring.toLowerCase())) {
                                str2 = AndroidAppUtil.IMAGE_MIME_TYPE_JPG;
                            } else if (TextUtils.equals("png", substring.toLowerCase())) {
                                str2 = AndroidAppUtil.IMAGE_MIME_TYPE_PNG;
                            } else if (TextUtils.equals("gif", substring.toLowerCase())) {
                                str2 = AndroidAppUtil.IMAGE_MIME_TYPE_GIF;
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
                return str2 == null ? AndroidAppUtil.IMAGE_MIME_TYPE_UNKNOW : str2;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                th = r3;
                th2 = th4;
            }
            throw th2;
            if (th != null) {
                try {
                    fileInputStream.close();
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                }
            } else {
                fileInputStream.close();
            }
            throw th2;
        } catch (Exception unused2) {
        }
    }

    @Nullable
    public static String getPathFromUri(@Nullable Context context, @Nullable Uri uri) {
        if (context == null || uri == null) {
            return null;
        }
        return FileUtils.getPathFromUri(context, uri);
    }

    private static int getJpegRotation(String str) throws IOException {
        int attributeInt = new ExifInterface(str).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
        if (attributeInt == 6) {
            return 90;
        }
        if (attributeInt == 3) {
            return 180;
        }
        if (attributeInt == 8) {
            return SubsamplingScaleImageView.ORIENTATION_270;
        }
        return 0;
    }

    public static Bitmap getJpegThumbnail(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        try {
            ExifInterface exifInterface = new ExifInterface(new File(str).getPath());
            if (!exifInterface.hasThumbnail()) {
                return null;
            }
            byte[] thumbnail = exifInterface.getThumbnail();
            if (thumbnail == null) {
                return null;
            }
            return BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length, new Options());
        } catch (Exception unused) {
            return null;
        }
    }

    private static Bitmap rotateBitmap(@NonNull Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) i);
        try {
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError unused) {
            return null;
        }
    }

    @Nullable
    public static Bitmap getRoundedCornerBitmap(@Nullable Bitmap bitmap, float f, float f2) {
        if (bitmap == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float width = f * ((float) bitmap.getWidth());
        float height = f2 * ((float) bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(SupportMenu.CATEGORY_MASK);
        canvas.drawRoundRect(rectF, width, height, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0029 A[SYNTHETIC, Splitter:B:19:0x0029] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0030 A[SYNTHETIC, Splitter:B:25:0x0030] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean makeGroupAvatarFromMembers(android.content.Context r2, @androidx.annotation.NonNull java.util.List<com.zipow.videobox.util.GroupAvatarDrawable.GroupAvatarItem> r3, int r4, @androidx.annotation.NonNull java.lang.String r5) {
        /*
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r5)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            android.graphics.Bitmap r2 = makeGroupAvatarFromMembers(r2, r3, r4)
            if (r2 != 0) goto L_0x000f
            return r1
        L_0x000f:
            r3 = 0
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x002d, all -> 0x0026 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x002d, all -> 0x0026 }
            android.graphics.Bitmap$CompressFormat r3 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r5 = 100
            boolean r1 = r2.compress(r3, r5, r4)     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r4.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x0033
        L_0x0021:
            r2 = move-exception
            r3 = r4
            goto L_0x0027
        L_0x0024:
            r3 = r4
            goto L_0x002e
        L_0x0026:
            r2 = move-exception
        L_0x0027:
            if (r3 == 0) goto L_0x002c
            r3.close()     // Catch:{ IOException -> 0x002c }
        L_0x002c:
            throw r2
        L_0x002d:
        L_0x002e:
            if (r3 == 0) goto L_0x0033
            r3.close()     // Catch:{ IOException -> 0x0033 }
        L_0x0033:
            r2.recycle()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ImageUtil.makeGroupAvatarFromMembers(android.content.Context, java.util.List, int, java.lang.String):boolean");
    }

    public static Bitmap makeGroupAvatarFromMembers(Context context, @NonNull List<GroupAvatarItem> list, int i) {
        if (i <= 0) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        new GroupAvatarDrawable(list).draw(canvas);
        return createBitmap;
    }

    public static boolean isValidImageFile(String str) {
        boolean z = false;
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(str, options);
            if (options.outWidth > 0 && options.outHeight > 0) {
                z = true;
            }
            return z;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isValidImageFile(Uri uri) {
        boolean z = false;
        if (uri == null) {
            return false;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(VideoBoxApplication.getNonNullInstance().getContentResolver().openInputStream(uri), null, options);
            if (options.outWidth > 0 && options.outHeight > 0) {
                z = true;
            }
            return z;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean compressImageFile(@Nullable String str, @Nullable String str2, int i) {
        if (str == null || str2 == null) {
            return false;
        }
        Bitmap decodeFile = ZMBitmapFactory.decodeFile(str, -1, false, false);
        if (decodeFile == null) {
            return false;
        }
        boolean saveBitmapAsJPEG = saveBitmapAsJPEG(decodeFile, str2, i);
        decodeFile.recycle();
        return saveBitmapAsJPEG;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0022 A[SYNTHETIC, Splitter:B:18:0x0022] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0029 A[SYNTHETIC, Splitter:B:24:0x0029] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean saveBitmapAsJPEG(@androidx.annotation.Nullable android.graphics.Bitmap r3, @androidx.annotation.Nullable java.lang.String r4, int r5) {
        /*
            r0 = 0
            if (r3 == 0) goto L_0x002d
            if (r4 != 0) goto L_0x0006
            goto L_0x002d
        L_0x0006:
            if (r5 != 0) goto L_0x000a
            r5 = 100
        L_0x000a:
            r1 = 0
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0026, all -> 0x001f }
            r2.<init>(r4)     // Catch:{ Exception -> 0x0026, all -> 0x001f }
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x001d, all -> 0x001a }
            boolean r0 = r3.compress(r4, r5, r2)     // Catch:{ Exception -> 0x001d, all -> 0x001a }
            r2.close()     // Catch:{ IOException -> 0x002c }
            goto L_0x002c
        L_0x001a:
            r3 = move-exception
            r1 = r2
            goto L_0x0020
        L_0x001d:
            r1 = r2
            goto L_0x0027
        L_0x001f:
            r3 = move-exception
        L_0x0020:
            if (r1 == 0) goto L_0x0025
            r1.close()     // Catch:{ IOException -> 0x0025 }
        L_0x0025:
            throw r3
        L_0x0026:
        L_0x0027:
            if (r1 == 0) goto L_0x002c
            r1.close()     // Catch:{ IOException -> 0x002c }
        L_0x002c:
            return r0
        L_0x002d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ImageUtil.saveBitmapAsJPEG(android.graphics.Bitmap, java.lang.String, int):boolean");
    }

    public static Bitmap blurBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            try {
                if (!bitmap.isRecycled()) {
                    Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
                    RenderScript create = RenderScript.create(VideoBoxApplication.getGlobalContext());
                    Allocation createFromBitmap = Allocation.createFromBitmap(create, copy);
                    ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, createFromBitmap.getElement());
                    create2.setInput(createFromBitmap);
                    create2.setRadius(10.0f);
                    create2.forEach(createFromBitmap);
                    createFromBitmap.copyTo(copy);
                    create.destroy();
                    return copy;
                }
            } catch (Exception unused) {
                return null;
            }
        }
        return null;
    }

    public static void saveToGallery(final Fragment fragment, File file) {
        if (file != null) {
            final String absolutePath = file.getAbsolutePath();
            if (!StringUtil.isEmptyOrNull(absolutePath) && new File(absolutePath).exists()) {
                final Handler handler = new Handler();
                C33811 r1 = new Thread("SaveImage") {
                    /* JADX WARNING: Code restructure failed: missing block: B:102:0x0104, code lost:
                        if (r3 != null) goto L_0x0106;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:104:?, code lost:
                        r16.close();
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0110, code lost:
                        r16.close();
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0114, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:112:0x0115, code lost:
                        r2 = r0;
                        r3 = null;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:113:0x0118, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:114:0x0119, code lost:
                        r2 = r0;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:116:?, code lost:
                        throw r2;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:117:0x011b, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:118:0x011c, code lost:
                        r3 = r2;
                        r2 = r0;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:87:0x00ee, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:90:?, code lost:
                        r3.addSuppressed(r0);
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:93:0x00f8, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:94:0x00f9, code lost:
                        r2 = r0;
                        r3 = null;
                     */
                    /* JADX WARNING: Failed to process nested try/catch */
                    /* JADX WARNING: Removed duplicated region for block: B:102:0x0104  */
                    /* JADX WARNING: Removed duplicated region for block: B:111:0x0114 A[ExcHandler: all (r0v10 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:31:0x007e] */
                    /* JADX WARNING: Removed duplicated region for block: B:93:0x00f8 A[ExcHandler: all (r0v14 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:33:0x0082] */
                    /* JADX WARNING: Unknown top exception splitter block from list: {B:60:0x00c3=Splitter:B:60:0x00c3, B:71:0x00d4=Splitter:B:71:0x00d4, B:47:0x00a8=Splitter:B:47:0x00a8} */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                            r18 = this;
                            r1 = r18
                            java.lang.String r0 = r4
                            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0)
                            if (r0 == 0) goto L_0x000b
                            return
                        L_0x000b:
                            java.io.File r0 = new java.io.File
                            java.lang.String r2 = r4
                            r0.<init>(r2)
                            boolean r2 = r0.exists()
                            if (r2 != 0) goto L_0x0019
                            return
                        L_0x0019:
                            boolean r2 = p021us.zoom.androidlib.util.OsUtil.isAtLeastQ()
                            r3 = 1
                            if (r2 == 0) goto L_0x0037
                            android.content.Context r2 = com.zipow.videobox.VideoBoxApplication.getGlobalContext()
                            if (r2 != 0) goto L_0x0027
                            return
                        L_0x0027:
                            android.net.Uri r4 = p021us.zoom.androidlib.util.FileUtils.insertFileIntoMediaStore(r2, r0)
                            if (r4 == 0) goto L_0x0144
                            boolean r0 = p021us.zoom.androidlib.util.FileUtils.copyFileToUri(r2, r0, r4)
                            if (r0 == 0) goto L_0x0144
                            r1._onSaveImageDone(r3)
                            return
                        L_0x0037:
                            java.io.File r2 = com.zipow.videobox.util.ImageUtil.getZoomGalleryPath()
                            if (r2 != 0) goto L_0x003e
                            return
                        L_0x003e:
                            java.lang.StringBuilder r4 = new java.lang.StringBuilder
                            r4.<init>()
                            java.lang.String r2 = r2.getPath()
                            r4.append(r2)
                            java.lang.String r2 = java.io.File.separator
                            r4.append(r2)
                            java.lang.String r2 = r0.getName()
                            r4.append(r2)
                            java.lang.String r2 = r4.toString()
                            java.io.File r4 = new java.io.File
                            r4.<init>(r2)
                            boolean r5 = r4.exists()
                            r6 = 0
                            if (r5 == 0) goto L_0x0073
                            long r8 = r4.length()
                            int r5 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
                            if (r5 <= 0) goto L_0x0073
                            r1._onSaveImageDone(r3)
                            return
                        L_0x0073:
                            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0144 }
                            r5.<init>(r0)     // Catch:{ Exception -> 0x0144 }
                            r8 = 0
                            java.io.FileOutputStream r9 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0131 }
                            r9.<init>(r4)     // Catch:{ Throwable -> 0x0131 }
                            java.nio.channels.FileChannel r16 = r5.getChannel()     // Catch:{ Throwable -> 0x0118, all -> 0x0114 }
                            java.nio.channels.FileChannel r17 = r9.getChannel()     // Catch:{ Throwable -> 0x00fc, all -> 0x00f8 }
                            r12 = 0
                            long r14 = r16.size()     // Catch:{ Throwable -> 0x00e0, all -> 0x00dc }
                            r10 = r17
                            r11 = r16
                            long r10 = r10.transferFrom(r11, r12, r14)     // Catch:{ Throwable -> 0x00e0, all -> 0x00dc }
                            int r0 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1))
                            if (r0 <= 0) goto L_0x00ca
                            android.content.Context r0 = com.zipow.videobox.VideoBoxApplication.getGlobalContext()     // Catch:{ Throwable -> 0x00e0, all -> 0x00dc }
                            if (r0 != 0) goto L_0x00af
                            if (r17 == 0) goto L_0x00a3
                            r17.close()     // Catch:{ Throwable -> 0x00fc, all -> 0x00f8 }
                        L_0x00a3:
                            if (r16 == 0) goto L_0x00a8
                            r16.close()     // Catch:{ Throwable -> 0x0118, all -> 0x0114 }
                        L_0x00a8:
                            r9.close()     // Catch:{ Throwable -> 0x0131 }
                            r5.close()     // Catch:{ Exception -> 0x0144 }
                            return
                        L_0x00af:
                            java.lang.String r2 = com.zipow.videobox.util.ImageUtil.getImageMimeType(r2)     // Catch:{ Throwable -> 0x00e0, all -> 0x00dc }
                            p021us.zoom.androidlib.util.AndroidAppUtil.addImageToGallery(r0, r4, r2)     // Catch:{ Throwable -> 0x00e0, all -> 0x00dc }
                            r1._onSaveImageDone(r3)     // Catch:{ Throwable -> 0x00e0, all -> 0x00dc }
                            if (r17 == 0) goto L_0x00be
                            r17.close()     // Catch:{ Throwable -> 0x00fc, all -> 0x00f8 }
                        L_0x00be:
                            if (r16 == 0) goto L_0x00c3
                            r16.close()     // Catch:{ Throwable -> 0x0118, all -> 0x0114 }
                        L_0x00c3:
                            r9.close()     // Catch:{ Throwable -> 0x0131 }
                            r5.close()     // Catch:{ Exception -> 0x0144 }
                            return
                        L_0x00ca:
                            if (r17 == 0) goto L_0x00cf
                            r17.close()     // Catch:{ Throwable -> 0x00fc, all -> 0x00f8 }
                        L_0x00cf:
                            if (r16 == 0) goto L_0x00d4
                            r16.close()     // Catch:{ Throwable -> 0x0118, all -> 0x0114 }
                        L_0x00d4:
                            r9.close()     // Catch:{ Throwable -> 0x0131 }
                            r5.close()     // Catch:{ Exception -> 0x0144 }
                            goto L_0x0144
                        L_0x00dc:
                            r0 = move-exception
                            r2 = r0
                            r3 = r8
                            goto L_0x00e6
                        L_0x00e0:
                            r0 = move-exception
                            r2 = r0
                            throw r2     // Catch:{ all -> 0x00e3 }
                        L_0x00e3:
                            r0 = move-exception
                            r3 = r2
                            r2 = r0
                        L_0x00e6:
                            if (r17 == 0) goto L_0x00f7
                            if (r3 == 0) goto L_0x00f4
                            r17.close()     // Catch:{ Throwable -> 0x00ee, all -> 0x00f8 }
                            goto L_0x00f7
                        L_0x00ee:
                            r0 = move-exception
                            r4 = r0
                            r3.addSuppressed(r4)     // Catch:{ Throwable -> 0x00fc, all -> 0x00f8 }
                            goto L_0x00f7
                        L_0x00f4:
                            r17.close()     // Catch:{ Throwable -> 0x00fc, all -> 0x00f8 }
                        L_0x00f7:
                            throw r2     // Catch:{ Throwable -> 0x00fc, all -> 0x00f8 }
                        L_0x00f8:
                            r0 = move-exception
                            r2 = r0
                            r3 = r8
                            goto L_0x0102
                        L_0x00fc:
                            r0 = move-exception
                            r2 = r0
                            throw r2     // Catch:{ all -> 0x00ff }
                        L_0x00ff:
                            r0 = move-exception
                            r3 = r2
                            r2 = r0
                        L_0x0102:
                            if (r16 == 0) goto L_0x0113
                            if (r3 == 0) goto L_0x0110
                            r16.close()     // Catch:{ Throwable -> 0x010a, all -> 0x0114 }
                            goto L_0x0113
                        L_0x010a:
                            r0 = move-exception
                            r4 = r0
                            r3.addSuppressed(r4)     // Catch:{ Throwable -> 0x0118, all -> 0x0114 }
                            goto L_0x0113
                        L_0x0110:
                            r16.close()     // Catch:{ Throwable -> 0x0118, all -> 0x0114 }
                        L_0x0113:
                            throw r2     // Catch:{ Throwable -> 0x0118, all -> 0x0114 }
                        L_0x0114:
                            r0 = move-exception
                            r2 = r0
                            r3 = r8
                            goto L_0x011e
                        L_0x0118:
                            r0 = move-exception
                            r2 = r0
                            throw r2     // Catch:{ all -> 0x011b }
                        L_0x011b:
                            r0 = move-exception
                            r3 = r2
                            r2 = r0
                        L_0x011e:
                            if (r3 == 0) goto L_0x012a
                            r9.close()     // Catch:{ Throwable -> 0x0124 }
                            goto L_0x012d
                        L_0x0124:
                            r0 = move-exception
                            r4 = r0
                            r3.addSuppressed(r4)     // Catch:{ Throwable -> 0x0131 }
                            goto L_0x012d
                        L_0x012a:
                            r9.close()     // Catch:{ Throwable -> 0x0131 }
                        L_0x012d:
                            throw r2     // Catch:{ Throwable -> 0x0131 }
                        L_0x012e:
                            r0 = move-exception
                            r2 = r0
                            goto L_0x0134
                        L_0x0131:
                            r0 = move-exception
                            r8 = r0
                            throw r8     // Catch:{ all -> 0x012e }
                        L_0x0134:
                            if (r8 == 0) goto L_0x0140
                            r5.close()     // Catch:{ Throwable -> 0x013a }
                            goto L_0x0143
                        L_0x013a:
                            r0 = move-exception
                            r3 = r0
                            r8.addSuppressed(r3)     // Catch:{ Exception -> 0x0144 }
                            goto L_0x0143
                        L_0x0140:
                            r5.close()     // Catch:{ Exception -> 0x0144 }
                        L_0x0143:
                            throw r2     // Catch:{ Exception -> 0x0144 }
                        L_0x0144:
                            r0 = 0
                            r1._onSaveImageDone(r0)
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ImageUtil.C33811.run():void");
                    }

                    private void _onSaveImageDone(final boolean z) {
                        handler.post(new Runnable() {
                            public void run() {
                                if (fragment != null && fragment.isAdded()) {
                                    FragmentManager fragmentManager = fragment.getFragmentManager();
                                    if (fragmentManager != null) {
                                        ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
                                        if (zMDialogFragment != null) {
                                            zMDialogFragment.dismissAllowingStateLoss();
                                        }
                                        FragmentActivity activity = fragment.getActivity();
                                        if (activity != null) {
                                            Toast.makeText(activity, z ? C4558R.string.zm_mm_msg_saved_to_album : C4558R.string.zm_mm_msg_saved_to_album_failed_102727, 0).show();
                                        }
                                    }
                                }
                            }
                        });
                    }
                };
                if (fragment != null) {
                    FragmentManager fragmentManager = fragment.getFragmentManager();
                    if (fragmentManager != null) {
                        WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
                        newInstance.setCancelable(true);
                        newInstance.show(fragmentManager, "WaitingDialog");
                    } else {
                        return;
                    }
                }
                r1.start();
            }
        }
    }

    public static void saveGiphyEmoji(File file) {
        if (file != null && file.exists()) {
            final MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
            if (zoomPrivateStickerMgr != null) {
                new ZMAsyncTask<File, Void, String>() {
                    /* access modifiers changed from: protected */
                    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0191, code lost:
                        r0 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0192, code lost:
                        r11 = r0;
                        r0 = r13;
                        r13 = r11;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:67:0x015a, code lost:
                        r1 = th;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:68:0x015b, code lost:
                        r4 = null;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:72:0x015f, code lost:
                        r4 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0160, code lost:
                        r11 = r4;
                        r4 = r1;
                        r1 = r11;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0174, code lost:
                        r0 = th;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0175, code lost:
                        r1 = null;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0179, code lost:
                        r1 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:89:0x017a, code lost:
                        r11 = r1;
                        r1 = r0;
                        r0 = r11;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:98:0x018c, code lost:
                        r13 = th;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:99:0x018d, code lost:
                        r0 = null;
                     */
                    /* JADX WARNING: Failed to process nested try/catch */
                    /* JADX WARNING: Removed duplicated region for block: B:67:0x015a A[ExcHandler: all (th java.lang.Throwable), Splitter:B:35:0x0120] */
                    /* JADX WARNING: Removed duplicated region for block: B:83:0x0174 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:33:0x011c] */
                    /* JADX WARNING: Removed duplicated region for block: B:98:0x018c A[ExcHandler: all (th java.lang.Throwable), Splitter:B:31:0x0117] */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public java.lang.String doInBackground(java.io.File... r13) {
                        /*
                            r12 = this;
                            r0 = 0
                            r1 = r13[r0]
                            if (r1 != 0) goto L_0x0008
                            java.lang.String r13 = ""
                            return r13
                        L_0x0008:
                            r13 = r13[r0]
                            boolean r0 = r13.exists()
                            if (r0 != 0) goto L_0x0013
                            java.lang.String r13 = ""
                            return r13
                        L_0x0013:
                            java.io.File r0 = com.zipow.videobox.util.ImageUtil.getZoomGalleryPath()
                            if (r0 != 0) goto L_0x001c
                            java.lang.String r13 = ""
                            return r13
                        L_0x001c:
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r2 = r0.getPath()
                            r1.append(r2)
                            java.lang.String r2 = java.io.File.separator
                            r1.append(r2)
                            java.lang.String r2 = r13.getName()
                            r1.append(r2)
                            java.lang.String r1 = r1.toString()
                            java.lang.String r2 = r13.getName()
                            java.lang.String r2 = r2.toLowerCase()
                            java.lang.String r3 = ".png"
                            boolean r2 = r2.endsWith(r3)
                            if (r2 != 0) goto L_0x00f0
                            java.lang.String r2 = r13.getName()
                            java.lang.String r2 = r2.toLowerCase()
                            java.lang.String r3 = ".jpg"
                            boolean r2 = r2.endsWith(r3)
                            if (r2 != 0) goto L_0x00f0
                            java.lang.String r2 = r13.getName()
                            java.lang.String r2 = r2.toLowerCase()
                            java.lang.String r3 = ".gif"
                            boolean r2 = r2.endsWith(r3)
                            if (r2 == 0) goto L_0x006a
                            goto L_0x00f0
                        L_0x006a:
                            java.lang.String r2 = r13.getAbsolutePath()
                            java.lang.String r2 = com.zipow.videobox.util.ImageUtil.getImageMimeType(r2)
                            java.lang.String r3 = "image/gif"
                            boolean r3 = r2.equalsIgnoreCase(r3)
                            if (r3 == 0) goto L_0x009c
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r0 = r0.getPath()
                            r1.append(r0)
                            java.lang.String r0 = java.io.File.separator
                            r1.append(r0)
                            java.lang.String r0 = r13.getName()
                            r1.append(r0)
                            java.lang.String r0 = ".gif"
                            r1.append(r0)
                            java.lang.String r1 = r1.toString()
                            goto L_0x010c
                        L_0x009c:
                            java.lang.String r3 = "image/jpeg"
                            boolean r3 = r2.equalsIgnoreCase(r3)
                            if (r3 == 0) goto L_0x00c6
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r0 = r0.getPath()
                            r1.append(r0)
                            java.lang.String r0 = java.io.File.separator
                            r1.append(r0)
                            java.lang.String r0 = r13.getName()
                            r1.append(r0)
                            java.lang.String r0 = ".jpg"
                            r1.append(r0)
                            java.lang.String r1 = r1.toString()
                            goto L_0x010c
                        L_0x00c6:
                            java.lang.String r3 = "image/png"
                            boolean r2 = r2.equalsIgnoreCase(r3)
                            if (r2 == 0) goto L_0x010c
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r0 = r0.getPath()
                            r1.append(r0)
                            java.lang.String r0 = java.io.File.separator
                            r1.append(r0)
                            java.lang.String r0 = r13.getName()
                            r1.append(r0)
                            java.lang.String r0 = ".png"
                            r1.append(r0)
                            java.lang.String r1 = r1.toString()
                            goto L_0x010c
                        L_0x00f0:
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r0 = r0.getPath()
                            r1.append(r0)
                            java.lang.String r0 = java.io.File.separator
                            r1.append(r0)
                            java.lang.String r0 = r13.getName()
                            r1.append(r0)
                            java.lang.String r1 = r1.toString()
                        L_0x010c:
                            java.io.File r0 = new java.io.File
                            r0.<init>(r1)
                            r2 = 0
                            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x01a4 }
                            r3.<init>(r13)     // Catch:{ Exception -> 0x01a4 }
                            java.io.FileOutputStream r13 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x018f, all -> 0x018c }
                            r13.<init>(r0)     // Catch:{ Throwable -> 0x018f, all -> 0x018c }
                            java.nio.channels.FileChannel r0 = r3.getChannel()     // Catch:{ Throwable -> 0x0177, all -> 0x0174 }
                            java.nio.channels.FileChannel r10 = r13.getChannel()     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                            r6 = 0
                            long r8 = r0.size()     // Catch:{ Throwable -> 0x0143, all -> 0x0140 }
                            r4 = r10
                            r5 = r0
                            r4.transferFrom(r5, r6, r8)     // Catch:{ Throwable -> 0x0143, all -> 0x0140 }
                            if (r10 == 0) goto L_0x0134
                            r10.close()     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                        L_0x0134:
                            if (r0 == 0) goto L_0x0139
                            r0.close()     // Catch:{ Throwable -> 0x0177, all -> 0x0174 }
                        L_0x0139:
                            r13.close()     // Catch:{ Throwable -> 0x018f, all -> 0x018c }
                            r3.close()     // Catch:{ Exception -> 0x01a4 }
                            return r1
                        L_0x0140:
                            r1 = move-exception
                            r4 = r2
                            goto L_0x0149
                        L_0x0143:
                            r1 = move-exception
                            throw r1     // Catch:{ all -> 0x0145 }
                        L_0x0145:
                            r4 = move-exception
                            r11 = r4
                            r4 = r1
                            r1 = r11
                        L_0x0149:
                            if (r10 == 0) goto L_0x0159
                            if (r4 == 0) goto L_0x0156
                            r10.close()     // Catch:{ Throwable -> 0x0151, all -> 0x015a }
                            goto L_0x0159
                        L_0x0151:
                            r5 = move-exception
                            r4.addSuppressed(r5)     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                            goto L_0x0159
                        L_0x0156:
                            r10.close()     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                        L_0x0159:
                            throw r1     // Catch:{ Throwable -> 0x015d, all -> 0x015a }
                        L_0x015a:
                            r1 = move-exception
                            r4 = r2
                            goto L_0x0163
                        L_0x015d:
                            r1 = move-exception
                            throw r1     // Catch:{ all -> 0x015f }
                        L_0x015f:
                            r4 = move-exception
                            r11 = r4
                            r4 = r1
                            r1 = r11
                        L_0x0163:
                            if (r0 == 0) goto L_0x0173
                            if (r4 == 0) goto L_0x0170
                            r0.close()     // Catch:{ Throwable -> 0x016b, all -> 0x0174 }
                            goto L_0x0173
                        L_0x016b:
                            r0 = move-exception
                            r4.addSuppressed(r0)     // Catch:{ Throwable -> 0x0177, all -> 0x0174 }
                            goto L_0x0173
                        L_0x0170:
                            r0.close()     // Catch:{ Throwable -> 0x0177, all -> 0x0174 }
                        L_0x0173:
                            throw r1     // Catch:{ Throwable -> 0x0177, all -> 0x0174 }
                        L_0x0174:
                            r0 = move-exception
                            r1 = r2
                            goto L_0x017d
                        L_0x0177:
                            r0 = move-exception
                            throw r0     // Catch:{ all -> 0x0179 }
                        L_0x0179:
                            r1 = move-exception
                            r11 = r1
                            r1 = r0
                            r0 = r11
                        L_0x017d:
                            if (r1 == 0) goto L_0x0188
                            r13.close()     // Catch:{ Throwable -> 0x0183, all -> 0x018c }
                            goto L_0x018b
                        L_0x0183:
                            r13 = move-exception
                            r1.addSuppressed(r13)     // Catch:{ Throwable -> 0x018f, all -> 0x018c }
                            goto L_0x018b
                        L_0x0188:
                            r13.close()     // Catch:{ Throwable -> 0x018f, all -> 0x018c }
                        L_0x018b:
                            throw r0     // Catch:{ Throwable -> 0x018f, all -> 0x018c }
                        L_0x018c:
                            r13 = move-exception
                            r0 = r2
                            goto L_0x0195
                        L_0x018f:
                            r13 = move-exception
                            throw r13     // Catch:{ all -> 0x0191 }
                        L_0x0191:
                            r0 = move-exception
                            r11 = r0
                            r0 = r13
                            r13 = r11
                        L_0x0195:
                            if (r0 == 0) goto L_0x01a0
                            r3.close()     // Catch:{ Throwable -> 0x019b }
                            goto L_0x01a3
                        L_0x019b:
                            r1 = move-exception
                            r0.addSuppressed(r1)     // Catch:{ Exception -> 0x01a4 }
                            goto L_0x01a3
                        L_0x01a0:
                            r3.close()     // Catch:{ Exception -> 0x01a4 }
                        L_0x01a3:
                            throw r13     // Catch:{ Exception -> 0x01a4 }
                        L_0x01a4:
                            return r2
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ImageUtil.C33832.doInBackground(java.io.File[]):java.lang.String");
                    }

                    /* access modifiers changed from: protected */
                    public void onPostExecute(String str) {
                        if (!TextUtils.isEmpty(str)) {
                            zoomPrivateStickerMgr.uploadAndMakePrivateSticker(str);
                        }
                        super.onPostExecute(str);
                    }
                }.execute((Params[]) new File[]{file});
            }
        }
    }

    @Nullable
    public static Uri createImageUri() {
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext == null) {
            return null;
        }
        if (Environment.getExternalStorageState().equals("mounted")) {
            return globalContext.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
        }
        return globalContext.getContentResolver().insert(Media.INTERNAL_CONTENT_URI, new ContentValues());
    }
}
