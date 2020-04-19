package com.zipow.videobox.markdown;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.tempbean.IMessageTemplateExtendMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.message.TokenParser;
import p021us.zoom.androidlib.util.CollectionsUtil;

public class MarkDownUtils {
    private static int RND = ((int) System.nanoTime());

    static class LinkSpec {
        int end;
        int start;
        String url;

        LinkSpec() {
        }
    }

    public static final int rnd() {
        int i = (RND * 1664525) + 1013904223;
        RND = i;
        return i >>> 22;
    }

    public static final int skipSpaces(String str, int i) {
        while (i < str.length() && (str.charAt(i) == ' ' || str.charAt(i) == 10)) {
            i++;
        }
        if (i < str.length()) {
            return i;
        }
        return -1;
    }

    public static final int escape(@NonNull SpannableStringBuilder spannableStringBuilder, char c, int i) {
        if (!(c == '<' || c == '>' || c == '{' || c == '}')) {
            switch (c) {
                case '!':
                case '\"':
                case '#':
                    break;
                default:
                    switch (c) {
                        case '\'':
                        case '(':
                        case ')':
                        case '*':
                        case '+':
                            break;
                        default:
                            switch (c) {
                                case '-':
                                case '.':
                                    break;
                                default:
                                    switch (c) {
                                        case '[':
                                        case '\\':
                                        case ']':
                                        case '^':
                                        case '_':
                                        case '`':
                                            break;
                                        default:
                                            spannableStringBuilder.append(TokenParser.ESCAPE);
                                            return i;
                                    }
                            }
                    }
            }
        }
        spannableStringBuilder.append(c);
        return i + 1;
    }

    public static final int readUntil(@NonNull SpannableStringBuilder spannableStringBuilder, String str, int i, @NonNull char... cArr) {
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (charAt == '\\') {
                int i2 = i + 1;
                if (i2 < str.length()) {
                    i = escape(spannableStringBuilder, str.charAt(i2), i);
                    i++;
                }
            }
            boolean z = false;
            int i3 = 0;
            while (true) {
                if (i3 >= cArr.length) {
                    break;
                } else if (charAt == cArr[i3]) {
                    z = true;
                    break;
                } else {
                    i3++;
                }
            }
            if (z) {
                break;
            }
            spannableStringBuilder.append(charAt);
            i++;
        }
        if (i == str.length()) {
            return -1;
        }
        return i;
    }

    public static final int readUntil(@NonNull SpannableStringBuilder spannableStringBuilder, String str, int i, char c) {
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (charAt == '\\') {
                int i2 = i + 1;
                if (i2 < str.length()) {
                    i = escape(spannableStringBuilder, str.charAt(i2), i);
                    i++;
                }
            }
            if (charAt == c) {
                break;
            }
            spannableStringBuilder.append(charAt);
            i++;
        }
        if (i == str.length()) {
            return -1;
        }
        return i;
    }

    private static final void gatherLinks(ArrayList<LinkSpec> arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        Matcher matcher = pattern.matcher(spannable);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (matchFilter == null || matchFilter.acceptMatch(spannable, start, end)) {
                LinkSpec linkSpec = new LinkSpec();
                linkSpec.url = makeUrl(matcher.group(0), strArr, matcher, transformFilter);
                linkSpec.start = start;
                linkSpec.end = end;
                arrayList.add(linkSpec);
            }
        }
    }

    private static final String makeUrl(@NonNull String str, @NonNull String[] strArr, Matcher matcher, @Nullable TransformFilter transformFilter) {
        boolean z;
        if (transformFilter != null) {
            str = transformFilter.transformUrl(matcher, str);
        }
        int i = 0;
        while (true) {
            z = true;
            if (i >= strArr.length) {
                z = false;
                break;
            }
            if (str.regionMatches(true, 0, strArr[i], 0, strArr[i].length())) {
                if (!str.regionMatches(false, 0, strArr[i], 0, strArr[i].length())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(strArr[i]);
                    sb.append(str.substring(strArr[i].length()));
                    str = sb.toString();
                }
            } else {
                i++;
            }
        }
        if (z || strArr.length <= 0) {
            return str;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(strArr[0]);
        sb2.append(str);
        return sb2.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:62:0x011d  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x00cb A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x013c A[EDGE_INSN: B:96:0x013c->B:72:0x013c ?: BREAK  
    EDGE_INSN: B:96:0x013c->B:72:0x013c ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void addAutoLink(@androidx.annotation.Nullable android.widget.TextView r15) {
        /*
            if (r15 == 0) goto L_0x0159
            java.lang.CharSequence r0 = r15.getText()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x000e
            goto L_0x0159
        L_0x000e:
            android.content.Context r0 = com.zipow.videobox.VideoBoxApplication.getGlobalContext()
            if (r0 != 0) goto L_0x0015
            return
        L_0x0015:
            java.lang.CharSequence r0 = r15.getText()
            boolean r1 = r0 instanceof android.text.SpannableString
            if (r1 == 0) goto L_0x0020
            android.text.SpannableString r0 = (android.text.SpannableString) r0
            goto L_0x0026
        L_0x0020:
            android.text.SpannableString r2 = new android.text.SpannableString
            r2.<init>(r0)
            r0 = r2
        L_0x0026:
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.util.regex.Pattern r5 = android.util.Patterns.WEB_URL
            java.lang.String r3 = "http://"
            java.lang.String r4 = "https://"
            java.lang.String[] r6 = new java.lang.String[]{r3, r4}
            android.text.util.Linkify$MatchFilter r7 = android.text.util.Linkify.sUrlMatchFilter
            r8 = 0
            r3 = r2
            r4 = r0
            gatherLinks(r3, r4, r5, r6, r7, r8)
            int r3 = r0.length()
            java.lang.Class<android.text.style.URLSpan> r4 = android.text.style.URLSpan.class
            r9 = 0
            java.lang.Object[] r3 = r0.getSpans(r9, r3, r4)
            r10 = r3
            android.text.style.URLSpan[] r10 = (android.text.style.URLSpan[]) r10
            int r3 = r0.length()
            java.lang.Class<android.text.style.ClickableSpan> r4 = android.text.style.ClickableSpan.class
            java.lang.Object[] r3 = r0.getSpans(r9, r3, r4)
            r11 = r3
            android.text.style.ClickableSpan[] r11 = (android.text.style.ClickableSpan[]) r11
            int r3 = r2.size()
            r12 = 33
            r13 = 1
            if (r3 <= 0) goto L_0x00ad
            java.util.Iterator r2 = r2.iterator()
        L_0x0065:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x00ad
            java.lang.Object r3 = r2.next()
            com.zipow.videobox.markdown.MarkDownUtils$LinkSpec r3 = (com.zipow.videobox.markdown.MarkDownUtils.LinkSpec) r3
            int r4 = r3.start
            int r5 = r3.end
            java.lang.Class<android.text.style.ImageSpan> r6 = android.text.style.ImageSpan.class
            java.lang.Object[] r4 = r0.getSpans(r4, r5, r6)
            android.text.style.ImageSpan[] r4 = (android.text.style.ImageSpan[]) r4
            if (r4 == 0) goto L_0x0083
            int r4 = r4.length
            if (r4 <= 0) goto L_0x0083
            goto L_0x0065
        L_0x0083:
            if (r10 == 0) goto L_0x009b
            int r4 = r10.length
            if (r4 <= 0) goto L_0x009b
            int r4 = r10.length
            r5 = 0
        L_0x008a:
            if (r5 >= r4) goto L_0x009b
            r6 = r10[r5]
            int r6 = r0.getSpanStart(r6)
            int r7 = r3.start
            if (r6 != r7) goto L_0x0098
            r4 = 1
            goto L_0x009c
        L_0x0098:
            int r5 = r5 + 1
            goto L_0x008a
        L_0x009b:
            r4 = 0
        L_0x009c:
            if (r4 != 0) goto L_0x0065
            com.zipow.videobox.markdown.MarkDownUtils$1 r4 = new com.zipow.videobox.markdown.MarkDownUtils$1
            java.lang.String r5 = r3.url
            r4.<init>(r5)
            int r5 = r3.start
            int r3 = r3.end
            r0.setSpan(r4, r5, r3, r12)
            goto L_0x0065
        L_0x00ad:
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.util.regex.Pattern r5 = android.util.Patterns.EMAIL_ADDRESS
            java.lang.String r3 = "mailto:"
            java.lang.String[] r6 = new java.lang.String[]{r3}
            r7 = 0
            r8 = 0
            r3 = r2
            r4 = r0
            gatherLinks(r3, r4, r5, r6, r7, r8)
            int r3 = r2.size()
            if (r3 <= 0) goto L_0x0153
            java.util.Iterator r2 = r2.iterator()
        L_0x00cb:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0153
            java.lang.Object r3 = r2.next()
            com.zipow.videobox.markdown.MarkDownUtils$LinkSpec r3 = (com.zipow.videobox.markdown.MarkDownUtils.LinkSpec) r3
            int r4 = r3.start
            int r5 = r3.end
            java.lang.Class<android.text.style.ImageSpan> r6 = android.text.style.ImageSpan.class
            java.lang.Object[] r4 = r0.getSpans(r4, r5, r6)
            android.text.style.ImageSpan[] r4 = (android.text.style.ImageSpan[]) r4
            if (r4 == 0) goto L_0x00e9
            int r4 = r4.length
            if (r4 <= 0) goto L_0x00e9
            goto L_0x00cb
        L_0x00e9:
            if (r10 == 0) goto L_0x0111
            int r4 = r10.length
            if (r4 <= 0) goto L_0x0111
            int r4 = r10.length
            r5 = 0
        L_0x00f0:
            if (r5 >= r4) goto L_0x0111
            r6 = r10[r5]
            int r7 = r0.getSpanStart(r6)
            int r6 = r0.getSpanEnd(r6)
            int r8 = r3.start
            if (r7 > r8) goto L_0x0104
            int r8 = r3.start
            if (r6 >= r8) goto L_0x010c
        L_0x0104:
            int r8 = r3.end
            if (r6 < r8) goto L_0x010e
            int r6 = r3.end
            if (r7 > r6) goto L_0x010e
        L_0x010c:
            r4 = 1
            goto L_0x0112
        L_0x010e:
            int r5 = r5 + 1
            goto L_0x00f0
        L_0x0111:
            r4 = 0
        L_0x0112:
            if (r4 != 0) goto L_0x013c
            if (r11 == 0) goto L_0x013c
            int r5 = r11.length
            if (r5 <= 0) goto L_0x013c
            int r5 = r11.length
            r6 = 0
        L_0x011b:
            if (r6 >= r5) goto L_0x013c
            r7 = r11[r6]
            int r8 = r0.getSpanStart(r7)
            int r7 = r0.getSpanEnd(r7)
            int r14 = r3.start
            if (r8 > r14) goto L_0x012f
            int r14 = r3.start
            if (r7 >= r14) goto L_0x0137
        L_0x012f:
            int r14 = r3.end
            if (r7 < r14) goto L_0x0139
            int r7 = r3.end
            if (r8 > r7) goto L_0x0139
        L_0x0137:
            r4 = 1
            goto L_0x013c
        L_0x0139:
            int r6 = r6 + 1
            goto L_0x011b
        L_0x013c:
            if (r4 != 0) goto L_0x00cb
            android.text.method.MovementMethod r4 = android.text.method.LinkMovementMethod.getInstance()
            r15.setMovementMethod(r4)
            com.zipow.videobox.markdown.MarkDownUtils$2 r4 = new com.zipow.videobox.markdown.MarkDownUtils$2
            r4.<init>(r3)
            int r5 = r3.start
            int r3 = r3.end
            r0.setSpan(r4, r5, r3, r12)
            goto L_0x00cb
        L_0x0153:
            if (r1 != 0) goto L_0x0158
            r15.setText(r0)
        L_0x0158:
            return
        L_0x0159:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.markdown.MarkDownUtils.addAutoLink(android.widget.TextView):void");
    }

    public static final int readRawUntil(@NonNull SpannableStringBuilder spannableStringBuilder, String str, int i, @NonNull char... cArr) {
        while (i < str.length()) {
            char charAt = str.charAt(i);
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= cArr.length) {
                    break;
                } else if (charAt == cArr[i2]) {
                    z = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (z) {
                break;
            }
            spannableStringBuilder.append(charAt);
            i++;
        }
        if (i == str.length()) {
            return -1;
        }
        return i;
    }

    public static final int readRawUntil(@NonNull SpannableStringBuilder spannableStringBuilder, String str, int i, char c) {
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (charAt == c) {
                break;
            }
            spannableStringBuilder.append(charAt);
            i++;
        }
        if (i == str.length()) {
            return -1;
        }
        return i;
    }

    public static void addMarkDownLabel(List<IMessageTemplateExtendMessage> list) {
        if (!CollectionsUtil.isListEmpty(list)) {
            int i = 0;
            IMessageTemplateExtendMessage iMessageTemplateExtendMessage = null;
            while (i < list.size()) {
                if (i > 0) {
                    iMessageTemplateExtendMessage = (IMessageTemplateExtendMessage) list.get(i - 1);
                }
                int i2 = i + 1;
                IMessageTemplateExtendMessage iMessageTemplateExtendMessage2 = i2 < list.size() ? (IMessageTemplateExtendMessage) list.get(i2) : null;
                IMessageTemplateExtendMessage iMessageTemplateExtendMessage3 = (IMessageTemplateExtendMessage) list.get(i);
                if (iMessageTemplateExtendMessage3 != null && iMessageTemplateExtendMessage3.isMonospace()) {
                    if (iMessageTemplateExtendMessage == null || !iMessageTemplateExtendMessage.isMonospace()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(" ");
                        sb.append(iMessageTemplateExtendMessage3.getText());
                        iMessageTemplateExtendMessage3.setText(sb.toString());
                    }
                    if (iMessageTemplateExtendMessage2 == null || !iMessageTemplateExtendMessage2.isMonospace()) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(iMessageTemplateExtendMessage3.getText());
                        sb2.append(" ");
                        iMessageTemplateExtendMessage3.setText(sb2.toString());
                    }
                }
                i = i2;
            }
        }
    }
}
