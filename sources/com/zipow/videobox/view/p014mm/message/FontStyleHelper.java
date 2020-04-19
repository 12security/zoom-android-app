package com.zipow.videobox.view.p014mm.message;

import android.text.ParcelableSpan;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.IMProtos.FontStyte;
import com.zipow.videobox.ptapp.IMProtos.FontStyteItem;
import java.util.ArrayList;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.FontStyleHelper */
public class FontStyleHelper {
    public static final int BulletSpanGapWidth = 20;
    public static final String SPLITOR = "\n";

    private static boolean invalidPos(int i, int i2, int i3) {
        return i < 0 || i2 < 0 || i > i2 || i >= i3 || i2 > i3;
    }

    public static CharSequence getCharSequenceFromMMMessageItem(CharSequence charSequence, FontStyte fontStyte) {
        if (fontStyte == null) {
            return charSequence;
        }
        int itemCount = fontStyte.getItemCount();
        SpannableString spannableString = new SpannableString(charSequence);
        for (int i = 0; i < itemCount; i++) {
            FontStyteItem item = fontStyte.getItem(i);
            long type = item.getType();
            int startpos = item.getStartpos();
            int endpos = item.getEndpos();
            if (!invalidPos(startpos, endpos, charSequence.length())) {
                int i2 = endpos + 1;
                if (i2 > charSequence.length()) {
                    i2 = charSequence.length();
                }
                if (1 == type) {
                    spannableString.setSpan(new StyleSpan(1), startpos, i2, 18);
                } else if (2 == type) {
                    spannableString.setSpan(new StyleSpan(2), startpos, i2, 18);
                } else if (3 == type) {
                    spannableString.setSpan(new StyleSpan(3), startpos, i2, 18);
                } else if (8 == type) {
                    String[] split = charSequence.subSequence(startpos, i2).toString().split(SPLITOR);
                    for (int i3 = 0; i3 < split.length; i3++) {
                        if (split[i3].length() != 0) {
                            spannableString.setSpan(new BulletSpan(20), startpos, (split[i3].length() + startpos) - 1, 18);
                            startpos = startpos + split[i3].length() + 1;
                        }
                    }
                } else if (4 == type) {
                    spannableString.setSpan(new StrikethroughSpan(), startpos, i2, 18);
                } else {
                    spannableString.setSpan(new ImageSpan(VideoBoxApplication.getGlobalContext().getResources().getDrawable(C4558R.C4559drawable.question)), startpos, i2, 18);
                }
            }
        }
        return spannableString;
    }

    public static FontStyte buildFromCharSequence(CharSequence charSequence) {
        if (charSequence == null || (charSequence instanceof String)) {
            return null;
        }
        int length = charSequence.length();
        Spanned spanned = (Spanned) charSequence;
        StyleSpan[] styleSpanArr = (StyleSpan[]) spanned.getSpans(0, length, StyleSpan.class);
        BulletSpan[] bulletSpanArr = (BulletSpan[]) spanned.getSpans(0, length, BulletSpan.class);
        StrikethroughSpan[] strikethroughSpanArr = (StrikethroughSpan[]) spanned.getSpans(0, length, StrikethroughSpan.class);
        ArrayList arrayList = new ArrayList();
        installSpan(styleSpanArr, spanned, arrayList);
        installSpan(bulletSpanArr, spanned, arrayList);
        installSpan(strikethroughSpanArr, spanned, arrayList);
        if (CollectionsUtil.isCollectionEmpty(arrayList)) {
            return null;
        }
        return FontStyte.newBuilder().addAllItem(arrayList).build();
    }

    private static void installSpan(ParcelableSpan[] parcelableSpanArr, Spanned spanned, ArrayList<FontStyteItem> arrayList) {
        if (parcelableSpanArr != null) {
            for (StyleSpan styleSpan : parcelableSpanArr) {
                int spanStart = spanned.getSpanStart(styleSpan);
                int spanEnd = spanned.getSpanEnd(styleSpan);
                long j = -1;
                if (styleSpan instanceof StyleSpan) {
                    StyleSpan styleSpan2 = styleSpan;
                    if (1 == styleSpan2.getStyle()) {
                        j = 1;
                    } else if (2 == styleSpan2.getStyle()) {
                        j = 2;
                    } else if (3 == styleSpan2.getStyle()) {
                        j = 3;
                    }
                } else if (styleSpan instanceof BulletSpan) {
                    j = 8;
                } else if (styleSpan instanceof StrikethroughSpan) {
                    j = 4;
                }
                arrayList.add(FontStyteItem.newBuilder().setType(j).setStartpos(spanStart).setEndpos(spanEnd - 1).build());
            }
        }
    }
}
