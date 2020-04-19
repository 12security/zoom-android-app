package com.zipow.videobox.util.zmurl.avatar;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import java.io.IOException;
import java.io.InputStream;

public class ZMAvatarUrlFetcher implements DataFetcher<InputStream> {
    private static final String TAG = "com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrlFetcher";
    private volatile boolean isCancelled;
    private int mHeight;
    private InputStream mInputStream;
    private int mWidth;
    private ZMAvatarUrl mZMAvatarUrl;

    public ZMAvatarUrlFetcher(ZMAvatarUrl zMAvatarUrl, int i, int i2) {
        this.mZMAvatarUrl = zMAvatarUrl;
        this.mWidth = i;
        this.mHeight = i2;
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [android.graphics.drawable.Drawable] */
    /* JADX WARNING: type inference failed for: r1v19, types: [com.zipow.videobox.util.NameAbbrAvatarDrawable] */
    /* JADX WARNING: type inference failed for: r5v2 */
    /* JADX WARNING: type inference failed for: r1v21, types: [android.graphics.drawable.Drawable] */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r1v25, types: [com.zipow.videobox.util.IconAvatarDrawable] */
    /* JADX WARNING: type inference failed for: r5v4 */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00fd, code lost:
        if (r0 == null) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0158, code lost:
        if (r0 == null) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x019c, code lost:
        if (r0 == null) goto L_0x01a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x019e, code lost:
        r0.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x01a1, code lost:
        return;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00e9 A[Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00f5 A[SYNTHETIC, Splitter:B:42:0x00f5] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0153 A[SYNTHETIC, Splitter:B:63:0x0153] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0197 A[SYNTHETIC, Splitter:B:72:0x0197] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x01a5 A[SYNTHETIC, Splitter:B:80:0x01a5] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01ac  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:60:0x0119=Splitter:B:60:0x0119, B:69:0x015d=Splitter:B:69:0x015d} */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadData(@androidx.annotation.NonNull com.bumptech.glide.Priority r14, @androidx.annotation.NonNull com.bumptech.glide.load.data.DataFetcher.DataCallback<? super java.io.InputStream> r15) {
        /*
            r13 = this;
            int r14 = r13.mWidth
            r0 = 1112014848(0x42480000, float:50.0)
            if (r14 > 0) goto L_0x0010
            com.zipow.videobox.VideoBoxApplication r14 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            int r14 = p021us.zoom.androidlib.util.UIUtil.dip2px(r14, r0)
            r13.mWidth = r14
        L_0x0010:
            int r14 = r13.mHeight
            if (r14 > 0) goto L_0x001e
            com.zipow.videobox.VideoBoxApplication r14 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            int r14 = p021us.zoom.androidlib.util.UIUtil.dip2px(r14, r0)
            r13.mHeight = r14
        L_0x001e:
            r14 = 0
            int r0 = r13.mWidth     // Catch:{ OutOfMemoryError -> 0x015b, Exception -> 0x0117, all -> 0x0113 }
            int r1 = r13.mHeight     // Catch:{ OutOfMemoryError -> 0x015b, Exception -> 0x0117, all -> 0x0113 }
            android.graphics.Bitmap$Config r2 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ OutOfMemoryError -> 0x015b, Exception -> 0x0117, all -> 0x0113 }
            android.graphics.Bitmap r0 = android.graphics.Bitmap.createBitmap(r0, r1, r2)     // Catch:{ OutOfMemoryError -> 0x015b, Exception -> 0x0117, all -> 0x0113 }
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r1 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r1 = r1.getDrawIcon()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            if (r1 == 0) goto L_0x0058
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r1 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            java.lang.String r1 = r1.getBgColorSeedString()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            if (r1 != 0) goto L_0x0058
            com.zipow.videobox.util.IconAvatarDrawable r1 = new com.zipow.videobox.util.IconAvatarDrawable     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            android.content.Context r2 = com.zipow.videobox.VideoBoxApplication.getGlobalContext()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r3 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r3 = r3.getDrawIcon()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            android.graphics.drawable.Drawable r2 = androidx.core.content.ContextCompat.getDrawable(r2, r3)     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r3 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            java.lang.String r3 = r3.getBgColorSeedString()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r1.<init>(r2, r3)     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r5 = r1
            goto L_0x0082
        L_0x0058:
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r1 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r1 = r1.getDrawIcon()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            if (r1 == 0) goto L_0x0070
            com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r2 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r2 = r2.getDrawIcon()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            android.graphics.drawable.Drawable r1 = androidx.core.content.ContextCompat.getDrawable(r1, r2)     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r5 = r1
            goto L_0x0082
        L_0x0070:
            com.zipow.videobox.util.NameAbbrAvatarDrawable r1 = new com.zipow.videobox.util.NameAbbrAvatarDrawable     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r2 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            java.lang.String r2 = r2.getBgNameSeedString()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r3 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            java.lang.String r3 = r3.getBgColorSeedString()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r1.<init>(r2, r3)     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r5 = r1
        L_0x0082:
            if (r5 != 0) goto L_0x008a
            if (r0 == 0) goto L_0x0089
            r0.recycle()
        L_0x0089:
            return
        L_0x008a:
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r1 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            boolean r1 = r1.isRoundCorner()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            if (r1 == 0) goto L_0x00b9
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r1 = r13.mZMAvatarUrl     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarCornerParams r1 = r1.getZMAvatarCornerParams()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            if (r1 == 0) goto L_0x00b9
            com.zipow.videobox.util.RoundDrawable r2 = new com.zipow.videobox.util.RoundDrawable     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            float r6 = r1.getCornerRatio()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r7 = r1.getBorderColor()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            boolean r8 = r1.isbCircle()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r9 = r1.getClientWidth()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r10 = r1.getClientHeight()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r11 = r1.getBorderSize()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r4 = r2
            r4.<init>(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            goto L_0x00ba
        L_0x00b9:
            r2 = r5
        L_0x00ba:
            android.graphics.Canvas r1 = new android.graphics.Canvas     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r1.<init>(r0)     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r3 = r1.getWidth()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            int r4 = r1.getHeight()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r5 = 0
            r2.setBounds(r5, r5, r3, r4)     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r2.draw(r1)     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            r1.<init>()     // Catch:{ OutOfMemoryError -> 0x0111, Exception -> 0x010f }
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            r3 = 100
            r0.compress(r2, r3, r1)     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            java.io.ByteArrayInputStream r2 = new java.io.ByteArrayInputStream     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            byte[] r3 = r1.toByteArray()     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            r2.<init>(r3)     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            r13.mInputStream = r2     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            boolean r2 = r13.isCancelled     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            if (r2 == 0) goto L_0x00f5
            r15.onDataReady(r14)     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            r1.close()     // Catch:{ IOException -> 0x00ef }
        L_0x00ef:
            if (r0 == 0) goto L_0x00f4
            r0.recycle()
        L_0x00f4:
            return
        L_0x00f5:
            java.io.InputStream r14 = r13.mInputStream     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            r15.onDataReady(r14)     // Catch:{ OutOfMemoryError -> 0x010a, Exception -> 0x0105, all -> 0x0101 }
            r1.close()     // Catch:{ IOException -> 0x00fd }
        L_0x00fd:
            if (r0 == 0) goto L_0x01a1
            goto L_0x019e
        L_0x0101:
            r15 = move-exception
            r14 = r1
            goto L_0x01a3
        L_0x0105:
            r14 = move-exception
            r12 = r1
            r1 = r14
            r14 = r12
            goto L_0x0119
        L_0x010a:
            r14 = move-exception
            r12 = r1
            r1 = r14
            r14 = r12
            goto L_0x015d
        L_0x010f:
            r1 = move-exception
            goto L_0x0119
        L_0x0111:
            r1 = move-exception
            goto L_0x015d
        L_0x0113:
            r15 = move-exception
            r0 = r14
            goto L_0x01a3
        L_0x0117:
            r1 = move-exception
            r0 = r14
        L_0x0119:
            java.lang.Exception r2 = new java.lang.Exception     // Catch:{ all -> 0x01a2 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01a2 }
            r3.<init>()     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = "Avatar error:"
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r4 = r13.mZMAvatarUrl     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x01a2 }
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = ">>>width*height=("
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            int r4 = r13.mWidth     // Catch:{ all -> 0x01a2 }
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = "*"
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            int r4 = r13.mHeight     // Catch:{ all -> 0x01a2 }
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = ")"
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x01a2 }
            r2.<init>(r3, r1)     // Catch:{ all -> 0x01a2 }
            r15.onLoadFailed(r2)     // Catch:{ all -> 0x01a2 }
            if (r14 == 0) goto L_0x0158
            r14.close()     // Catch:{ IOException -> 0x0157 }
            goto L_0x0158
        L_0x0157:
        L_0x0158:
            if (r0 == 0) goto L_0x01a1
            goto L_0x019e
        L_0x015b:
            r1 = move-exception
            r0 = r14
        L_0x015d:
            java.lang.Exception r2 = new java.lang.Exception     // Catch:{ all -> 0x01a2 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01a2 }
            r3.<init>()     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = "OutOfMemoryError:"
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrl r4 = r13.mZMAvatarUrl     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x01a2 }
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = ">>>width*height=("
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            int r4 = r13.mWidth     // Catch:{ all -> 0x01a2 }
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = "*"
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            int r4 = r13.mHeight     // Catch:{ all -> 0x01a2 }
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            java.lang.String r4 = ")"
            r3.append(r4)     // Catch:{ all -> 0x01a2 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x01a2 }
            r2.<init>(r3, r1)     // Catch:{ all -> 0x01a2 }
            r15.onLoadFailed(r2)     // Catch:{ all -> 0x01a2 }
            if (r14 == 0) goto L_0x019c
            r14.close()     // Catch:{ IOException -> 0x019b }
            goto L_0x019c
        L_0x019b:
        L_0x019c:
            if (r0 == 0) goto L_0x01a1
        L_0x019e:
            r0.recycle()
        L_0x01a1:
            return
        L_0x01a2:
            r15 = move-exception
        L_0x01a3:
            if (r14 == 0) goto L_0x01aa
            r14.close()     // Catch:{ IOException -> 0x01a9 }
            goto L_0x01aa
        L_0x01a9:
        L_0x01aa:
            if (r0 == 0) goto L_0x01af
            r0.recycle()
        L_0x01af:
            throw r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.zmurl.avatar.ZMAvatarUrlFetcher.loadData(com.bumptech.glide.Priority, com.bumptech.glide.load.data.DataFetcher$DataCallback):void");
    }

    public void cleanup() {
        InputStream inputStream = this.mInputStream;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException unused) {
            }
        }
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @NonNull
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
