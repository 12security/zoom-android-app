package com.zipow.videobox.tempbean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.zipow.videobox.util.ZMActionMsgUtil;
import java.io.IOException;

public class IMessageTemplateExtendMessage {
    private boolean bold;
    private String hyperlink;
    private String img;
    private String img_alt;
    private boolean itatic;
    private String linkto;
    private String mailto;
    private String mention;
    private boolean mention_all;
    private boolean monospace;
    private int quotes;
    private String sip;
    private boolean strikethrough;
    private String text;

    /* JADX WARNING: Removed duplicated region for block: B:108:0x0204  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0259  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x027e  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x0283  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void emitter(@androidx.annotation.Nullable android.text.SpannableStringBuilder r9, @androidx.annotation.NonNull android.widget.TextView r10, com.zipow.videobox.tempbean.IMessageTemplateExtendMessage r11, @androidx.annotation.Nullable com.zipow.videobox.markdown.URLImageParser.OnUrlDrawableUpdateListener r12) {
        /*
            r8 = this;
            if (r9 == 0) goto L_0x0287
            java.lang.String r0 = r8.text
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0014
            java.lang.String r0 = r8.img
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0014
            goto L_0x0287
        L_0x0014:
            java.lang.String r0 = r8.text
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0028
            android.text.SpannableStringBuilder r0 = new android.text.SpannableStringBuilder
            java.lang.String r1 = r8.img_alt
            if (r1 != 0) goto L_0x0024
            java.lang.String r1 = r8.img
        L_0x0024:
            r0.<init>(r1)
            goto L_0x002f
        L_0x0028:
            android.text.SpannableStringBuilder r0 = new android.text.SpannableStringBuilder
            java.lang.String r1 = r8.text
            r0.<init>(r1)
        L_0x002f:
            android.text.SpannableStringBuilder r1 = new android.text.SpannableStringBuilder
            r1.<init>()
            boolean r2 = r8.itatic
            if (r2 == 0) goto L_0x0041
            com.zipow.videobox.markdown.EmitterDecorator.emitterItalic(r1, r0)
            android.text.SpannableStringBuilder r0 = new android.text.SpannableStringBuilder
            r0.<init>()
            goto L_0x0044
        L_0x0041:
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x0044:
            boolean r2 = r8.bold
            if (r2 == 0) goto L_0x0053
            com.zipow.videobox.markdown.EmitterDecorator.emitterBold(r0, r1)
            android.text.SpannableStringBuilder r1 = new android.text.SpannableStringBuilder
            r1.<init>()
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x0053:
            boolean r2 = r8.strikethrough
            if (r2 == 0) goto L_0x0062
            com.zipow.videobox.markdown.EmitterDecorator.emitterStrikethrough(r0, r1)
            android.text.SpannableStringBuilder r1 = new android.text.SpannableStringBuilder
            r1.<init>()
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x0062:
            java.lang.String r2 = r8.hyperlink
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x0077
            java.lang.String r2 = r8.hyperlink
            com.zipow.videobox.markdown.EmitterDecorator.emitterHyperLink(r0, r1, r2)
            android.text.SpannableStringBuilder r1 = new android.text.SpannableStringBuilder
            r1.<init>()
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x0077:
            java.lang.String r2 = r8.sip
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x008c
            java.lang.String r2 = r8.sip
            com.zipow.videobox.markdown.EmitterDecorator.emitterSipcall(r0, r1, r2)
            android.text.SpannableStringBuilder r1 = new android.text.SpannableStringBuilder
            r1.<init>()
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x008c:
            java.lang.String r2 = r8.mailto
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x00a1
            java.lang.String r2 = r8.mailto
            com.zipow.videobox.markdown.EmitterDecorator.emitterMailto(r0, r1, r2)
            android.text.SpannableStringBuilder r1 = new android.text.SpannableStringBuilder
            r1.<init>()
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x00a1:
            java.lang.String r2 = r8.linkto
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            r3 = 0
            r4 = 1
            if (r2 != 0) goto L_0x00f1
            java.lang.String r2 = "@"
            r0.append(r2)
            boolean r2 = android.text.TextUtils.isEmpty(r9)
            if (r2 != 0) goto L_0x00e4
            int r2 = r9.length()
            java.lang.Class<com.zipow.videobox.markdown.ProfileLinkSpan> r5 = com.zipow.videobox.markdown.ProfileLinkSpan.class
            java.lang.Object[] r2 = r9.getSpans(r3, r2, r5)
            com.zipow.videobox.markdown.ProfileLinkSpan[] r2 = (com.zipow.videobox.markdown.ProfileLinkSpan[]) r2
            if (r2 == 0) goto L_0x00e4
            int r5 = r2.length
            if (r5 <= 0) goto L_0x00e4
            int r5 = r2.length
            int r5 = r5 - r4
            r2 = r2[r5]
            int r5 = r9.getSpanEnd(r2)
            int r6 = r9.length()
            if (r5 != r6) goto L_0x00e4
            java.lang.String r5 = r8.linkto
            java.lang.String r2 = r2.getJid()
            boolean r2 = android.text.TextUtils.equals(r5, r2)
            if (r2 == 0) goto L_0x00e4
            r0.clear()
        L_0x00e4:
            java.lang.String r2 = r8.linkto
            com.zipow.videobox.markdown.EmitterDecorator.emitterProfileLink(r0, r1, r2)
            android.text.SpannableStringBuilder r1 = new android.text.SpannableStringBuilder
            r1.<init>()
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x00f1:
            boolean r2 = r8.mention_all
            if (r2 != 0) goto L_0x00fd
            java.lang.String r2 = r8.mention
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x014d
        L_0x00fd:
            java.lang.String r2 = r8.mention
            if (r2 != 0) goto L_0x0105
            java.lang.String r2 = ""
            r8.mention = r2
        L_0x0105:
            java.lang.String r2 = "@"
            r0.append(r2)
            boolean r2 = android.text.TextUtils.isEmpty(r9)
            if (r2 != 0) goto L_0x013e
            int r2 = r9.length()
            java.lang.Class<com.zipow.videobox.markdown.MentionLinkSpan> r5 = com.zipow.videobox.markdown.MentionLinkSpan.class
            java.lang.Object[] r2 = r9.getSpans(r3, r2, r5)
            com.zipow.videobox.markdown.MentionLinkSpan[] r2 = (com.zipow.videobox.markdown.MentionLinkSpan[]) r2
            if (r2 == 0) goto L_0x013e
            int r5 = r2.length
            if (r5 <= 0) goto L_0x013e
            int r5 = r2.length
            int r5 = r5 - r4
            r2 = r2[r5]
            int r5 = r9.getSpanEnd(r2)
            int r6 = r9.length()
            if (r5 != r6) goto L_0x013e
            java.lang.String r5 = r8.mention
            java.lang.String r2 = r2.getJid()
            boolean r2 = android.text.TextUtils.equals(r5, r2)
            if (r2 == 0) goto L_0x013e
            r0.clear()
        L_0x013e:
            java.lang.String r2 = r8.mention
            boolean r5 = r8.mention_all
            com.zipow.videobox.markdown.EmitterDecorator.emitterMentionLink(r0, r1, r2, r5)
            android.text.SpannableStringBuilder r1 = new android.text.SpannableStringBuilder
            r1.<init>()
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x014d:
            java.lang.String r2 = r8.img
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            r5 = 10
            if (r2 != 0) goto L_0x01ba
            int r2 = r9.length()
            if (r2 > 0) goto L_0x017d
            if (r11 != 0) goto L_0x0165
            java.lang.String r11 = r8.img
            com.zipow.videobox.markdown.EmitterDecorator.emitterLargeImage(r0, r1, r10, r11, r12)
            goto L_0x01b4
        L_0x0165:
            java.lang.String r11 = r11.text
            if (r11 == 0) goto L_0x0177
            java.lang.String r2 = "\n"
            boolean r11 = r11.equals(r2)
            if (r11 == 0) goto L_0x0177
            java.lang.String r11 = r8.img
            com.zipow.videobox.markdown.EmitterDecorator.emitterLargeImage(r0, r1, r10, r11, r12)
            goto L_0x01b4
        L_0x0177:
            java.lang.String r11 = r8.img
            com.zipow.videobox.markdown.EmitterDecorator.emitterImage(r0, r1, r10, r11, r12)
            goto L_0x01b4
        L_0x017d:
            int r2 = r9.length()
            int r2 = r2 - r4
            char r2 = r9.charAt(r2)
            if (r2 != r5) goto L_0x019c
            if (r11 == 0) goto L_0x019c
            java.lang.String r2 = r11.text
            if (r2 == 0) goto L_0x019c
            java.lang.String r6 = "\n"
            boolean r2 = r2.equals(r6)
            if (r2 == 0) goto L_0x019c
            java.lang.String r11 = r8.img
            com.zipow.videobox.markdown.EmitterDecorator.emitterLargeImage(r0, r1, r10, r11, r12)
            goto L_0x01b4
        L_0x019c:
            int r2 = r9.length()
            int r2 = r2 - r4
            char r2 = r9.charAt(r2)
            if (r2 != r5) goto L_0x01af
            if (r11 != 0) goto L_0x01af
            java.lang.String r11 = r8.img
            com.zipow.videobox.markdown.EmitterDecorator.emitterLargeImage(r0, r1, r10, r11, r12)
            goto L_0x01b4
        L_0x01af:
            java.lang.String r11 = r8.img
            com.zipow.videobox.markdown.EmitterDecorator.emitterImage(r0, r1, r10, r11, r12)
        L_0x01b4:
            android.text.SpannableStringBuilder r10 = new android.text.SpannableStringBuilder
            r10.<init>()
            goto L_0x01bc
        L_0x01ba:
            r10 = r0
            r0 = r1
        L_0x01bc:
            boolean r11 = android.text.TextUtils.isEmpty(r9)
            r12 = -1
            if (r11 != 0) goto L_0x01fd
            int r11 = r9.length()
            java.lang.Class<com.zipow.videobox.markdown.BackgroundImageSpan> r1 = com.zipow.videobox.markdown.BackgroundImageSpan.class
            java.lang.Object[] r11 = r9.getSpans(r3, r11, r1)
            com.zipow.videobox.markdown.BackgroundImageSpan[] r11 = (com.zipow.videobox.markdown.BackgroundImageSpan[]) r11
            if (r11 == 0) goto L_0x01fd
            int r1 = r11.length
            if (r1 <= 0) goto L_0x01fd
            int r1 = r11.length
            int r1 = r1 - r4
            r11 = r11[r1]
            int r1 = r9.getSpanEnd(r11)
            int r2 = r9.length()
            if (r1 != r2) goto L_0x01f0
            boolean r1 = r8.isMonospace()
            if (r1 == 0) goto L_0x01f0
            int r1 = r9.getSpanStart(r11)
            r9.removeSpan(r11)
            goto L_0x01fe
        L_0x01f0:
            int r11 = r9.getSpanEnd(r11)
            int r1 = r9.length()
            if (r11 != r1) goto L_0x01fd
            r8.isMonospace()
        L_0x01fd:
            r1 = -1
        L_0x01fe:
            boolean r11 = android.text.TextUtils.isEmpty(r9)
            if (r11 != 0) goto L_0x0246
            int r11 = r9.length()
            java.lang.Class<android.text.style.LeadingMarginSpan$Standard> r2 = android.text.style.LeadingMarginSpan.Standard.class
            java.lang.Object[] r11 = r9.getSpans(r3, r11, r2)
            android.text.style.LeadingMarginSpan$Standard[] r11 = (android.text.style.LeadingMarginSpan.Standard[]) r11
            if (r11 == 0) goto L_0x0246
            int r2 = r11.length
            if (r2 <= 0) goto L_0x0246
            int r2 = r11.length
            int r2 = r2 - r4
            r11 = r11[r2]
            int r2 = r9.getSpanEnd(r11)
            int r6 = r9.length()
            if (r2 != r6) goto L_0x022f
            int r2 = r8.quotes
            if (r2 <= 0) goto L_0x0246
            int r12 = r9.getSpanStart(r11)
            r9.removeSpan(r11)
            goto L_0x0246
        L_0x022f:
            int r2 = r9.length()
            int r2 = r2 - r4
            char r2 = r9.charAt(r2)
            if (r2 != r5) goto L_0x0246
            int r11 = r9.getSpanEnd(r11)
            int r2 = r9.length()
            int r2 = r2 - r4
            if (r11 != r2) goto L_0x0246
            r3 = 1
        L_0x0246:
            if (r1 >= 0) goto L_0x0255
            boolean r11 = r8.monospace
            if (r11 == 0) goto L_0x0255
            com.zipow.videobox.markdown.EmitterDecorator.emitterMonospace(r10, r0)
            android.text.SpannableStringBuilder r11 = new android.text.SpannableStringBuilder
            r11.<init>()
            goto L_0x0257
        L_0x0255:
            r11 = r10
            r10 = r0
        L_0x0257:
            if (r12 >= 0) goto L_0x0279
            int r0 = r8.quotes
            if (r0 <= 0) goto L_0x0274
            boolean r0 = android.text.TextUtils.isEmpty(r9)
            if (r0 != 0) goto L_0x026a
            if (r3 != 0) goto L_0x026a
            java.lang.String r0 = "\n"
            r11.append(r0)
        L_0x026a:
            com.zipow.videobox.markdown.EmitterDecorator.emitterQuotes(r11, r10)
            android.text.SpannableStringBuilder r10 = new android.text.SpannableStringBuilder
            r10.<init>()
            r10 = r11
            goto L_0x0279
        L_0x0274:
            if (r3 == 0) goto L_0x0279
            r9.append(r5)
        L_0x0279:
            r9.append(r10)
            if (r1 < 0) goto L_0x0281
            com.zipow.videobox.markdown.EmitterDecorator.emitterMonospace(r1, r9)
        L_0x0281:
            if (r12 < 0) goto L_0x0286
            com.zipow.videobox.markdown.EmitterDecorator.emitterQuotes(r12, r9)
        L_0x0286:
            return
        L_0x0287:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.tempbean.IMessageTemplateExtendMessage.emitter(android.text.SpannableStringBuilder, android.widget.TextView, com.zipow.videobox.tempbean.IMessageTemplateExtendMessage, com.zipow.videobox.markdown.URLImageParser$OnUrlDrawableUpdateListener):void");
    }

    @Nullable
    public static IMessageTemplateExtendMessage parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateExtendMessage iMessageTemplateExtendMessage = new IMessageTemplateExtendMessage();
        if (jsonObject.has(ZMActionMsgUtil.KEY_EVENT)) {
            JsonElement jsonElement = jsonObject.get(ZMActionMsgUtil.KEY_EVENT);
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setText(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("italic")) {
            JsonElement jsonElement2 = jsonObject.get("italic");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setItatic(jsonElement2.getAsBoolean());
            }
        }
        if (jsonObject.has("bold")) {
            JsonElement jsonElement3 = jsonObject.get("bold");
            if (jsonElement3.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setBold(jsonElement3.getAsBoolean());
            }
        }
        if (jsonObject.has("strikethrough")) {
            JsonElement jsonElement4 = jsonObject.get("strikethrough");
            if (jsonElement4.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setStrikethrough(jsonElement4.getAsBoolean());
            }
        }
        if (jsonObject.has("monospace")) {
            JsonElement jsonElement5 = jsonObject.get("monospace");
            if (jsonElement5.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setMonospace(jsonElement5.getAsBoolean());
            }
        }
        if (jsonObject.has("quotes")) {
            JsonElement jsonElement6 = jsonObject.get("quotes");
            if (jsonElement6.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setQuotes(jsonElement6.getAsInt());
            }
        }
        if (jsonObject.has("hyperlink")) {
            JsonElement jsonElement7 = jsonObject.get("hyperlink");
            if (jsonElement7.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setHyperlink(jsonElement7.getAsString());
            }
        }
        if (jsonObject.has("sip")) {
            JsonElement jsonElement8 = jsonObject.get("sip");
            if (jsonElement8.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setSip(jsonElement8.getAsString());
            }
        }
        if (jsonObject.has("mailto")) {
            JsonElement jsonElement9 = jsonObject.get("mailto");
            if (jsonElement9.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setMailto(jsonElement9.getAsString());
            }
        }
        if (jsonObject.has("mention_all")) {
            JsonElement jsonElement10 = jsonObject.get("mention_all");
            if (jsonElement10.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setMention_all(jsonElement10.getAsBoolean());
            }
        }
        if (jsonObject.has("mention")) {
            JsonElement jsonElement11 = jsonObject.get("mention");
            if (jsonElement11.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setMention(jsonElement11.getAsString());
            }
        }
        if (jsonObject.has("linkto")) {
            JsonElement jsonElement12 = jsonObject.get("linkto");
            if (jsonElement12.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setLinkto(jsonElement12.getAsString());
            }
        }
        if (jsonObject.has("img")) {
            JsonElement jsonElement13 = jsonObject.get("img");
            if (jsonElement13.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setImg(jsonElement13.getAsString());
            }
        }
        if (jsonObject.has("img_alt")) {
            JsonElement jsonElement14 = jsonObject.get("img_alt");
            if (jsonElement14.isJsonPrimitive()) {
                iMessageTemplateExtendMessage.setImg_alt(jsonElement14.getAsString());
            }
        }
        return iMessageTemplateExtendMessage;
    }

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        if (this.text != null) {
            jsonWriter.name(ZMActionMsgUtil.KEY_EVENT).value(this.text);
        }
        jsonWriter.name("italic").value(this.itatic);
        jsonWriter.name("bold").value(this.bold);
        jsonWriter.name("strikethrough").value(this.strikethrough);
        jsonWriter.name("monospace").value(this.monospace);
        if (this.quotes >= 0) {
            jsonWriter.name("quotes").value((long) this.quotes);
        }
        if (this.hyperlink != null) {
            jsonWriter.name("hyperlink").value(this.hyperlink);
        }
        if (this.sip != null) {
            jsonWriter.name("sip").value(this.sip);
        }
        if (this.mailto != null) {
            jsonWriter.name("mailto").value(this.mailto);
        }
        jsonWriter.name("mention_all").value(this.mention_all);
        if (this.mention != null) {
            jsonWriter.name("mention").value(this.mention);
        }
        if (this.linkto != null) {
            jsonWriter.name("linkto").value(this.linkto);
        }
        if (this.img != null) {
            jsonWriter.name("img").value(this.img);
        }
        if (this.img_alt != null) {
            jsonWriter.name("img_alt").value(this.img_alt);
        }
        jsonWriter.endObject();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public boolean isItatic() {
        return this.itatic;
    }

    public void setItatic(boolean z) {
        this.itatic = z;
    }

    public boolean isBold() {
        return this.bold;
    }

    public void setBold(boolean z) {
        this.bold = z;
    }

    public boolean isStrikethrough() {
        return this.strikethrough;
    }

    public void setStrikethrough(boolean z) {
        this.strikethrough = z;
    }

    public boolean isMonospace() {
        return this.monospace;
    }

    public void setMonospace(boolean z) {
        this.monospace = z;
    }

    public int getQuotes() {
        return this.quotes;
    }

    public void setQuotes(int i) {
        this.quotes = i;
    }

    public String getHyperlink() {
        return this.hyperlink;
    }

    public void setHyperlink(String str) {
        this.hyperlink = str;
    }

    public String getSip() {
        return this.sip;
    }

    public void setSip(String str) {
        this.sip = str;
    }

    public String getMailto() {
        return this.mailto;
    }

    public void setMailto(String str) {
        this.mailto = str;
    }

    public boolean isMention_all() {
        return this.mention_all;
    }

    public void setMention_all(boolean z) {
        this.mention_all = z;
    }

    public String getMention() {
        return this.mention;
    }

    public void setMention(String str) {
        this.mention = str;
    }

    public String getLinkto() {
        return this.linkto;
    }

    public void setLinkto(String str) {
        this.linkto = str;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String str) {
        this.img = str;
    }

    public String getImg_alt() {
        return this.img_alt;
    }

    public void setImg_alt(String str) {
        this.img_alt = str;
    }
}
