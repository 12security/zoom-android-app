package com.zipow.videobox.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.IMProtos.EmojiItem;
import com.zipow.videobox.ptapp.IMProtos.EmojiList;
import com.zipow.videobox.ptapp.IMProtos.EmojiList.Builder;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper.ZMEmojiSpannableStringBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class EmojiHelper {
    public static final int EMOJI_TYPE_IOS = 1;
    public static final int EMOJI_TYPE_ZOOM = 0;
    private static int IOS_20E3 = 8419;
    private static int IOS_20E3_END = 57;
    private static int IOS_20E3_START = 35;
    private static final String TAG = "EmojiHelper";
    private static int ZM_EMOJI_START = 1048576;
    private static EmojiHelper mInstance;
    @NonNull
    private LongSparseArray<EmojiIndex> mEmojiMaps = new LongSparseArray<>();
    private int mLongEmojiStartMax;
    private int mLongEmojiStartMin;
    @NonNull
    private List<EmojiIndex> mZMEmojis = new ArrayList();

    public static class EmojiIndex {
        /* access modifiers changed from: private */
        public int drawResource;
        private int end;
        /* access modifiers changed from: private */
        public int index;
        /* access modifiers changed from: private */
        public String repstr;
        /* access modifiers changed from: private */
        public String shortCut;
        private int start;
        /* access modifiers changed from: private */
        public int type;

        EmojiIndex(int i, int i2, int i3, int i4, int i5, String str, String str2) {
            this.start = i;
            this.end = i2;
            this.drawResource = i3;
            this.type = i4;
            this.index = i5;
            this.shortCut = str;
            this.repstr = str2;
        }

        EmojiIndex(int i, int i2, int i3, String str, String str2) {
            this.drawResource = i;
            this.type = i2;
            this.index = i3;
            this.shortCut = str;
            this.repstr = str2;
        }

        public String getShortCut() {
            return this.shortCut;
        }

        public String getRepstr() {
            return this.repstr;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        public int getDrawResource() {
            return this.drawResource;
        }

        public int getIndex() {
            return this.index;
        }

        public int getType() {
            return this.type;
        }
    }

    public static class ZoomEmojiSpan extends ImageSpan {
        private int mHeight;
        private int mWidth;

        public ZoomEmojiSpan(@NonNull Drawable drawable) {
            super(drawable);
        }

        public void updateSize(int i, int i2) {
            if (i > 0 && i2 > 0 && (this.mWidth != i || this.mHeight != i2)) {
                getDrawable().setBounds(0, 0, i, i2);
                this.mWidth = i;
                this.mHeight = i2;
            }
        }
    }

    private EmojiHelper() {
        addEmojiConfig(VideoBoxApplication.getInstance(), C4558R.raw.zm_emoji_config);
        initZMEmojis();
    }

    public static synchronized EmojiHelper getInstance() {
        EmojiHelper emojiHelper;
        synchronized (EmojiHelper.class) {
            if (mInstance == null) {
                mInstance = new EmojiHelper();
            }
            emojiHelper = mInstance;
        }
        return emojiHelper;
    }

    public String getRealMsg(String str) {
        for (EmojiIndex emojiIndex : this.mZMEmojis) {
            str = str.replaceAll(emojiIndex.shortCut, emojiIndex.repstr);
        }
        return str;
    }

    private void initZMEmojis() {
        this.mZMEmojis.clear();
        ArrayList<Long> arrayList = new ArrayList<>();
        for (int i = 0; i < this.mEmojiMaps.size(); i++) {
            Long valueOf = Long.valueOf(this.mEmojiMaps.keyAt(i));
            EmojiIndex emojiIndex = (EmojiIndex) this.mEmojiMaps.get(valueOf.longValue());
            if (emojiIndex != null && emojiIndex.getType() == 0) {
                arrayList.add(valueOf);
            }
        }
        Collections.sort(arrayList);
        for (Long longValue : arrayList) {
            this.mZMEmojis.add(this.mEmojiMaps.get(longValue.longValue()));
        }
    }

    @NonNull
    public List<EmojiIndex> getZMEmojis() {
        return new ArrayList();
    }

    @Nullable
    public CharSequence tranToEmojiText(@Nullable CharSequence charSequence, @Nullable EmojiList emojiList) {
        int i;
        if (charSequence != 0 && charSequence.length() > 0 && emojiList != null && emojiList.getEmojiItemCount() > 0) {
            VideoBoxApplication instance = VideoBoxApplication.getInstance();
            if (instance == null) {
                return charSequence;
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
            for (int i2 = 0; i2 < emojiList.getEmojiItemCount(); i2++) {
                EmojiItem emojiItem = emojiList.getEmojiItem(i2);
                if (emojiItem.getPositionStart() < emojiItem.getPositionEnd() && emojiItem.getPositionEnd() <= charSequence.length()) {
                    if (emojiItem.getType() == 0) {
                        Resources resources = instance.getResources();
                        StringBuilder sb = new StringBuilder();
                        sb.append("zm_emoji_");
                        sb.append(emojiItem.getIndex());
                        i = resources.getIdentifier(sb.toString(), "drawable", instance.getPackageName());
                    } else if (!UIUtil.isNavtiveSupportIOSEmoji()) {
                        Resources resources2 = instance.getResources();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("emoji_");
                        sb2.append(emojiItem.getIndex());
                        i = resources2.getIdentifier(sb2.toString(), "drawable", instance.getPackageName());
                    }
                    if (i == 0) {
                        spannableStringBuilder.setSpan(new TextSpan(instance.getString(C4558R.string.zm_mm_msg_no_emoji)), emojiItem.getPositionStart(), emojiItem.getPositionEnd(), 33);
                    } else {
                        Drawable drawable = instance.getResources().getDrawable(i);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        ZoomEmojiSpan zoomEmojiSpan = new ZoomEmojiSpan(drawable);
                        String charSequence2 = charSequence.subSequence(emojiItem.getPositionStart(), emojiItem.getPositionEnd()).toString();
                        List emojiIndex = getEmojiIndex(charSequence2);
                        if (emojiIndex != null && emojiIndex.size() == 1) {
                            EmojiIndex emojiIndex2 = (EmojiIndex) emojiIndex.get(0);
                            if (emojiIndex2.drawResource == i && TextUtils.equals(emojiIndex2.repstr, charSequence2)) {
                                spannableStringBuilder.setSpan(zoomEmojiSpan, emojiItem.getPositionStart(), emojiItem.getPositionEnd(), 33);
                            }
                        }
                    }
                }
            }
            charSequence = spannableStringBuilder;
        }
        return charSequence;
    }

    @Nullable
    public CharSequence tranToShortcutText(@Nullable CharSequence charSequence, @Nullable EmojiList emojiList) {
        int i;
        if (charSequence != 0 && charSequence.length() > 0 && emojiList != null && emojiList.getEmojiItemCount() > 0) {
            VideoBoxApplication instance = VideoBoxApplication.getInstance();
            if (instance == null) {
                return charSequence;
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
            for (int emojiItemCount = emojiList.getEmojiItemCount() - 1; emojiItemCount >= 0; emojiItemCount--) {
                EmojiItem emojiItem = emojiList.getEmojiItem(emojiItemCount);
                if (emojiItem.getPositionStart() < emojiItem.getPositionEnd() && emojiItem.getPositionEnd() <= charSequence.length()) {
                    if (emojiItem.getType() == 0) {
                        Resources resources = instance.getResources();
                        StringBuilder sb = new StringBuilder();
                        sb.append("zm_emoji_");
                        sb.append(emojiItem.getIndex());
                        i = resources.getIdentifier(sb.toString(), "drawable", instance.getPackageName());
                    } else if (!UIUtil.isNavtiveSupportIOSEmoji()) {
                        Resources resources2 = instance.getResources();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("emoji_");
                        sb2.append(emojiItem.getIndex());
                        i = resources2.getIdentifier(sb2.toString(), "drawable", instance.getPackageName());
                    }
                    if (i == 0) {
                        spannableStringBuilder.replace(emojiItem.getPositionStart(), emojiItem.getPositionEnd(), instance.getString(C4558R.string.zm_mm_msg_no_emoji));
                    } else {
                        String shortcut = emojiItem.getShortcut();
                        if (StringUtil.isEmptyOrNull(shortcut) && emojiItem.getType() == 0) {
                            Iterator it = this.mZMEmojis.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                EmojiIndex emojiIndex = (EmojiIndex) it.next();
                                if (emojiIndex.index == emojiItem.getIndex()) {
                                    shortcut = emojiIndex.shortCut;
                                    break;
                                }
                            }
                        }
                        if (!StringUtil.isEmptyOrNull(shortcut)) {
                            spannableStringBuilder.replace(emojiItem.getPositionStart(), emojiItem.getPositionEnd(), shortcut);
                        }
                    }
                }
            }
            charSequence = spannableStringBuilder;
        }
        return charSequence;
    }

    @Nullable
    public EmojiList getEmojiList(@Nullable CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return null;
        }
        List<EmojiIndex> emojiIndex = getEmojiIndex(charSequence);
        if (emojiIndex == null || emojiIndex.size() <= 0) {
            return null;
        }
        Builder newBuilder = EmojiList.newBuilder();
        for (EmojiIndex emojiIndex2 : emojiIndex) {
            EmojiItem.Builder newBuilder2 = EmojiItem.newBuilder();
            newBuilder2.setIndex(emojiIndex2.getIndex());
            newBuilder2.setPositionEnd(emojiIndex2.getEnd());
            newBuilder2.setPositionStart(emojiIndex2.getStart());
            newBuilder2.setType(emojiIndex2.getType());
            newBuilder2.setShortcut(emojiIndex2.getShortCut());
            newBuilder2.setRepstr(emojiIndex2.getRepstr());
            newBuilder.addEmojiItem(newBuilder2.build());
        }
        return newBuilder.build();
    }

    @Nullable
    public ZMEmojiSpannableStringBuilder tranToEmojiText(@Nullable CharSequence charSequence) {
        ZMEmojiSpannableStringBuilder zMEmojiSpannableStringBuilder;
        if (charSequence == null || charSequence.length() <= 0) {
            return null;
        }
        if (charSequence instanceof ZMEmojiSpannableStringBuilder) {
            zMEmojiSpannableStringBuilder = (ZMEmojiSpannableStringBuilder) charSequence;
        } else {
            zMEmojiSpannableStringBuilder = new ZMEmojiSpannableStringBuilder(charSequence);
        }
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        ZoomEmojiSpan[] zoomEmojiSpanArr = (ZoomEmojiSpan[]) zMEmojiSpannableStringBuilder.getSpans(0, zMEmojiSpannableStringBuilder.length(), ZoomEmojiSpan.class);
        if (zoomEmojiSpanArr != null) {
            for (ZoomEmojiSpan removeSpan : zoomEmojiSpanArr) {
                zMEmojiSpannableStringBuilder.removeSpan(removeSpan);
            }
        }
        StringBuffer stringBuffer = null;
        for (int i = 0; i < charSequence.length(); i++) {
            char charAt = charSequence.charAt(i);
            if (charAt == ':') {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer(":");
                } else {
                    stringBuffer.append(':');
                    String stringBuffer2 = stringBuffer.toString();
                    for (EmojiIndex emojiIndex : this.mZMEmojis) {
                        if (emojiIndex.shortCut.equals(stringBuffer2)) {
                            Drawable drawable = instance.getResources().getDrawable(emojiIndex.getDrawResource());
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            zMEmojiSpannableStringBuilder.setSpan(new ZoomEmojiSpan(drawable), (i - emojiIndex.shortCut.length()) + 1, i + 1, 33);
                        }
                    }
                    stringBuffer = null;
                }
            } else if (stringBuffer != null) {
                stringBuffer.append(charAt);
            }
        }
        List emojiIndex2 = getInstance().getEmojiIndex(charSequence);
        if (emojiIndex2 != null && emojiIndex2.size() > 0) {
            for (int size = emojiIndex2.size() - 1; size >= 0; size--) {
                EmojiIndex emojiIndex3 = (EmojiIndex) emojiIndex2.get(size);
                if (!UIUtil.isNavtiveSupportIOSEmoji() || emojiIndex3.type != 1) {
                    if (emojiIndex3.getDrawResource() == 0) {
                        zMEmojiSpannableStringBuilder.setSpan(new TextSpan(instance.getString(C4558R.string.zm_mm_msg_no_emoji)), emojiIndex3.getStart(), emojiIndex3.getEnd(), 33);
                    } else {
                        Drawable drawable2 = instance.getResources().getDrawable(emojiIndex3.getDrawResource());
                        drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
                        zMEmojiSpannableStringBuilder.replace(emojiIndex3.getStart(), emojiIndex3.getEnd(), emojiIndex3.getShortCut());
                        zMEmojiSpannableStringBuilder.setSpan(new ZoomEmojiSpan(drawable2), emojiIndex3.getStart(), emojiIndex3.getStart() + emojiIndex3.getShortCut().length(), 33);
                    }
                }
            }
        }
        return zMEmojiSpannableStringBuilder;
    }

    @Nullable
    public List<EmojiIndex> getEmojiIndex(@Nullable CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            char charAt = charSequence.charAt(i);
            if (charAt < IOS_20E3_START || charAt > IOS_20E3_END) {
                int codePointAt = Character.codePointAt(charSequence, i);
                int charCount = Character.charCount(codePointAt);
                if (charCount <= 2 && charCount > 0 && codePointAt > 0) {
                    if (codePointAt >= this.mLongEmojiStartMin && codePointAt <= this.mLongEmojiStartMax) {
                        int i2 = i + 4;
                        if (i2 <= length) {
                            int codePointAt2 = Character.codePointAt(charSequence, i + 2);
                            if (Character.charCount(codePointAt2) == 2 && codePointAt2 != 0) {
                                EmojiIndex emojiIndex = (EmojiIndex) this.mEmojiMaps.get((((long) codePointAt) << 32) + ((long) codePointAt2));
                                if (emojiIndex != null) {
                                    EmojiIndex emojiIndex2 = new EmojiIndex(i, i2, emojiIndex.drawResource, emojiIndex.type, emojiIndex.index, emojiIndex.shortCut, emojiIndex.repstr);
                                    arrayList.add(emojiIndex2);
                                    i += 3;
                                }
                            }
                        }
                    }
                    EmojiIndex emojiIndex3 = (EmojiIndex) this.mEmojiMaps.get(Long.valueOf((long) codePointAt).longValue());
                    if (emojiIndex3 != null) {
                        EmojiIndex emojiIndex4 = new EmojiIndex(i, i + charCount, emojiIndex3.drawResource, emojiIndex3.type, emojiIndex3.index, emojiIndex3.shortCut, emojiIndex3.repstr);
                        arrayList.add(emojiIndex4);
                        i += charCount - 1;
                    }
                }
            } else {
                int i3 = i + 1;
                if (i3 < length && charSequence.charAt(i3) == IOS_20E3) {
                    EmojiIndex emojiIndex5 = (EmojiIndex) this.mEmojiMaps.get((long) charAt);
                    if (emojiIndex5 != null) {
                        EmojiIndex emojiIndex6 = new EmojiIndex(i, i + 2, emojiIndex5.drawResource, emojiIndex5.type, emojiIndex5.index, emojiIndex5.shortCut, emojiIndex5.repstr);
                        arrayList.add(emojiIndex6);
                        i = i3;
                    }
                }
            }
            i++;
        }
        return arrayList;
    }

    private void parseConfigLine(String str, @Nullable Context context) throws JSONException {
        EmojiIndex emojiIndex;
        if (context != null) {
            JSONObject jSONObject = new JSONObject(str);
            int optInt = jSONObject.optInt("emoji_pos");
            String optString = jSONObject.optString("utf16");
            String optString2 = jSONObject.optString("short_cut");
            if (!TextUtils.isEmpty(optString)) {
                long emojiToLong = emojiToLong(optString);
                if (emojiToLong < ((long) ZM_EMOJI_START) || emojiToLong >= 2147483647L) {
                    Resources resources = context.getResources();
                    StringBuilder sb = new StringBuilder();
                    sb.append("emoji_");
                    sb.append(optInt);
                    emojiIndex = new EmojiIndex(resources.getIdentifier(sb.toString(), "drawable", context.getPackageName()), 1, optInt, "", tranEmojiCodeToString(emojiToLong));
                } else {
                    Resources resources2 = context.getResources();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("zm_emoji_");
                    sb2.append(optInt);
                    emojiIndex = new EmojiIndex(resources2.getIdentifier(sb2.toString(), "drawable", context.getPackageName()), 0, optInt, optString2, tranEmojiCodeToString(emojiToLong));
                }
                this.mEmojiMaps.put(emojiToLong, emojiIndex);
            }
        }
    }

    @NonNull
    private String tranEmojiCodeToString(long j) {
        long j2 = j >> 32;
        int i = (int) j;
        String str = "";
        if (j2 != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(new String(Character.toChars((int) j2)));
            str = sb.toString();
        }
        if (i == 0) {
            return str;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(new String(Character.toChars(i)));
        return sb2.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0025, code lost:
        if (r6 == null) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r6.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addEmojiConfig(@androidx.annotation.Nullable android.content.Context r5, int r6) {
        /*
            r4 = this;
            if (r5 == 0) goto L_0x005a
            if (r6 != 0) goto L_0x0005
            goto L_0x005a
        L_0x0005:
            android.content.res.Resources r0 = r5.getResources()     // Catch:{ Exception -> 0x0059 }
            java.io.InputStream r6 = r0.openRawResource(r6)     // Catch:{ Exception -> 0x0059 }
            r0 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x0045 }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x0045 }
            r2.<init>(r6)     // Catch:{ Throwable -> 0x0045 }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0045 }
        L_0x0018:
            java.lang.String r2 = r1.readLine()     // Catch:{ Throwable -> 0x002e, all -> 0x002b }
            if (r2 == 0) goto L_0x0022
            r4.parseConfigLine(r2, r5)     // Catch:{ Throwable -> 0x002e, all -> 0x002b }
            goto L_0x0018
        L_0x0022:
            r1.close()     // Catch:{ Throwable -> 0x0045 }
            if (r6 == 0) goto L_0x0059
            r6.close()     // Catch:{ Exception -> 0x0059 }
            goto L_0x0059
        L_0x002b:
            r5 = move-exception
            r2 = r0
            goto L_0x0034
        L_0x002e:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0030 }
        L_0x0030:
            r2 = move-exception
            r3 = r2
            r2 = r5
            r5 = r3
        L_0x0034:
            if (r2 == 0) goto L_0x003f
            r1.close()     // Catch:{ Throwable -> 0x003a }
            goto L_0x0042
        L_0x003a:
            r1 = move-exception
            r2.addSuppressed(r1)     // Catch:{ Throwable -> 0x0045 }
            goto L_0x0042
        L_0x003f:
            r1.close()     // Catch:{ Throwable -> 0x0045 }
        L_0x0042:
            throw r5     // Catch:{ Throwable -> 0x0045 }
        L_0x0043:
            r5 = move-exception
            goto L_0x0048
        L_0x0045:
            r5 = move-exception
            r0 = r5
            throw r0     // Catch:{ all -> 0x0043 }
        L_0x0048:
            if (r6 == 0) goto L_0x0058
            if (r0 == 0) goto L_0x0055
            r6.close()     // Catch:{ Throwable -> 0x0050 }
            goto L_0x0058
        L_0x0050:
            r6 = move-exception
            r0.addSuppressed(r6)     // Catch:{ Exception -> 0x0059 }
            goto L_0x0058
        L_0x0055:
            r6.close()     // Catch:{ Exception -> 0x0059 }
        L_0x0058:
            throw r5     // Catch:{ Exception -> 0x0059 }
        L_0x0059:
            return
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.EmojiHelper.addEmojiConfig(android.content.Context, int):void");
    }

    private int parseHex(@NonNull String str) {
        try {
            return Integer.parseInt(str, 16);
        } catch (Exception unused) {
            return 0;
        }
    }

    private long emojiToLong(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (str.length() < 5) {
            return (long) parseHex(str);
        }
        String[] split = str.split(OAuth.SCOPE_DELIMITER);
        if (split.length != 2 && split.length != 4) {
            return 0;
        }
        char[] cArr = new char[split.length];
        for (int i = 0; i < split.length; i++) {
            cArr[i] = (char) parseHex(split[i]);
        }
        if (cArr.length == 2) {
            return (long) Character.codePointAt(cArr, 0);
        }
        long codePointAt = (long) Character.codePointAt(cArr, 0);
        long codePointAt2 = (long) Character.codePointAt(cArr, 2);
        if (this.mLongEmojiStartMax == 0 && this.mLongEmojiStartMin == 0) {
            int i2 = (int) codePointAt;
            this.mLongEmojiStartMax = i2;
            this.mLongEmojiStartMin = i2;
        } else {
            if (((long) this.mLongEmojiStartMax) < codePointAt) {
                this.mLongEmojiStartMax = (int) codePointAt;
            }
            if (((long) this.mLongEmojiStartMin) > codePointAt) {
                this.mLongEmojiStartMin = (int) codePointAt;
            }
        }
        return codePointAt2 + (codePointAt << 32);
    }
}
