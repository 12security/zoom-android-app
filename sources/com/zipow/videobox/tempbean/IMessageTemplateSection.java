package com.zipow.videobox.tempbean;

import androidx.annotation.Nullable;
import com.google.gson.stream.JsonWriter;
import com.zipow.videobox.markdown.MarkDownUtils;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessageTemplate;
import java.io.IOException;
import java.util.List;

public class IMessageTemplateSection extends IMessageTemplateBase {
    private List<IMessageTemplateExtendMessage> extendMessages;
    private String footer;
    private String footer_fall_back;
    private String footer_icon;
    private int footer_version;
    private boolean markdown;
    private List<IMessageTemplateBase> sections;
    private String sidebar_color;

    /* renamed from: ts */
    private long f333ts;

    public void writeJson(@Nullable JsonWriter jsonWriter) throws IOException {
        if (jsonWriter != null) {
            jsonWriter.beginObject();
            super.writeJson(jsonWriter);
            if (this.sidebar_color != null) {
                jsonWriter.name("sidebar_color").value(this.sidebar_color);
            }
            if (this.sections != null) {
                jsonWriter.name("sections");
                jsonWriter.beginArray();
                for (IMessageTemplateBase iMessageTemplateBase : this.sections) {
                    if (iMessageTemplateBase instanceof IMessageTemplateMessage) {
                        iMessageTemplateBase.writeJson(jsonWriter);
                    } else if (iMessageTemplateBase instanceof IMessageTemplateActions) {
                        iMessageTemplateBase.writeJson(jsonWriter);
                    } else if (iMessageTemplateBase instanceof IMessageTemplateFields) {
                        iMessageTemplateBase.writeJson(jsonWriter);
                    } else if (iMessageTemplateBase instanceof IMessageTemplateAttachments) {
                        iMessageTemplateBase.writeJson(jsonWriter);
                    } else if (iMessageTemplateBase instanceof IMessageTemplateSelect) {
                        iMessageTemplateBase.writeJson(jsonWriter);
                    } else {
                        iMessageTemplateBase.writeJson(jsonWriter);
                    }
                }
                jsonWriter.endArray();
            }
            if (this.footer != null) {
                jsonWriter.name("footer").value(this.footer);
            }
            jsonWriter.name("footer_version").value((long) this.footer_version);
            if (this.footer_fall_back != null) {
                jsonWriter.name("footer_fall_back").value(this.footer_fall_back);
            }
            jsonWriter.name("markdown").value(this.markdown);
            if (this.footer_icon != null) {
                jsonWriter.name("footer_icon").value(this.footer_icon);
            }
            if (this.f333ts > 0) {
                jsonWriter.name("ts").value(this.f333ts);
            }
            if (this.extendMessages != null) {
                jsonWriter.name("extracted_messages");
                jsonWriter.beginArray();
                for (IMessageTemplateExtendMessage writeJson : this.extendMessages) {
                    writeJson.writeJson(jsonWriter);
                }
                jsonWriter.endArray();
            }
            jsonWriter.endObject();
        }
    }

    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.tempbean.IMessageTemplateSection parse(@androidx.annotation.Nullable com.google.gson.JsonObject r9) {
        /*
            r0 = 0
            if (r9 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.zipow.videobox.tempbean.IMessageTemplateSection r1 = new com.zipow.videobox.tempbean.IMessageTemplateSection
            r1.<init>()
            com.zipow.videobox.tempbean.IMessageTemplateBase r1 = parse(r9, r1)
            com.zipow.videobox.tempbean.IMessageTemplateSection r1 = (com.zipow.videobox.tempbean.IMessageTemplateSection) r1
            if (r1 != 0) goto L_0x0012
            return r0
        L_0x0012:
            java.lang.String r0 = "sidebar_color"
            boolean r0 = r9.has(r0)
            if (r0 == 0) goto L_0x002d
            java.lang.String r0 = "sidebar_color"
            com.google.gson.JsonElement r0 = r9.get(r0)
            boolean r2 = r0.isJsonPrimitive()
            if (r2 == 0) goto L_0x002d
            java.lang.String r0 = r0.getAsString()
            r1.setSidebar_color(r0)
        L_0x002d:
            java.lang.String r0 = "sections"
            boolean r0 = r9.has(r0)
            r2 = 0
            if (r0 == 0) goto L_0x0112
            java.lang.String r0 = "sections"
            com.google.gson.JsonElement r0 = r9.get(r0)
            boolean r3 = r0.isJsonArray()
            if (r3 == 0) goto L_0x0112
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            com.google.gson.JsonArray r0 = r0.getAsJsonArray()
            r4 = 0
        L_0x004c:
            int r5 = r0.size()
            if (r4 >= r5) goto L_0x010f
            com.google.gson.JsonElement r5 = r0.get(r4)
            boolean r6 = r5.isJsonObject()
            if (r6 == 0) goto L_0x010b
            com.google.gson.JsonObject r5 = r5.getAsJsonObject()
            java.lang.String r6 = "type"
            boolean r6 = r5.has(r6)
            if (r6 == 0) goto L_0x010b
            java.lang.String r6 = "type"
            com.google.gson.JsonElement r6 = r5.get(r6)
            boolean r7 = r6.isJsonPrimitive()
            if (r7 == 0) goto L_0x010b
            java.lang.String r6 = r6.getAsString()
            r7 = -1
            int r8 = r6.hashCode()
            switch(r8) {
                case -1274708295: goto L_0x00b3;
                case -1161803523: goto L_0x00a9;
                case -906021636: goto L_0x009f;
                case -738997328: goto L_0x0095;
                case 100313435: goto L_0x008b;
                case 954925063: goto L_0x0081;
                default: goto L_0x0080;
            }
        L_0x0080:
            goto L_0x00bd
        L_0x0081:
            java.lang.String r8 = "message"
            boolean r6 = r6.equals(r8)
            if (r6 == 0) goto L_0x00bd
            r6 = 0
            goto L_0x00be
        L_0x008b:
            java.lang.String r8 = "image"
            boolean r6 = r6.equals(r8)
            if (r6 == 0) goto L_0x00bd
            r6 = 5
            goto L_0x00be
        L_0x0095:
            java.lang.String r8 = "attachments"
            boolean r6 = r6.equals(r8)
            if (r6 == 0) goto L_0x00bd
            r6 = 3
            goto L_0x00be
        L_0x009f:
            java.lang.String r8 = "select"
            boolean r6 = r6.equals(r8)
            if (r6 == 0) goto L_0x00bd
            r6 = 1
            goto L_0x00be
        L_0x00a9:
            java.lang.String r8 = "actions"
            boolean r6 = r6.equals(r8)
            if (r6 == 0) goto L_0x00bd
            r6 = 4
            goto L_0x00be
        L_0x00b3:
            java.lang.String r8 = "fields"
            boolean r6 = r6.equals(r8)
            if (r6 == 0) goto L_0x00bd
            r6 = 2
            goto L_0x00be
        L_0x00bd:
            r6 = -1
        L_0x00be:
            switch(r6) {
                case 0: goto L_0x0102;
                case 1: goto L_0x00f8;
                case 2: goto L_0x00ee;
                case 3: goto L_0x00e4;
                case 4: goto L_0x00da;
                case 5: goto L_0x00d0;
                default: goto L_0x00c1;
            }
        L_0x00c1:
            com.zipow.videobox.tempbean.IMessageTemplateBase r6 = new com.zipow.videobox.tempbean.IMessageTemplateBase
            r6.<init>()
            com.zipow.videobox.tempbean.IMessageTemplateBase r5 = com.zipow.videobox.tempbean.IMessageTemplateBase.parse(r5, r6)
            if (r5 == 0) goto L_0x010b
            r3.add(r5)
            goto L_0x010b
        L_0x00d0:
            com.zipow.videobox.tempbean.IMessageTemplateImage r5 = com.zipow.videobox.tempbean.IMessageTemplateImage.parse(r5)
            if (r5 == 0) goto L_0x010b
            r3.add(r5)
            goto L_0x010b
        L_0x00da:
            com.zipow.videobox.tempbean.IMessageTemplateActions r5 = com.zipow.videobox.tempbean.IMessageTemplateActions.parse(r5)
            if (r5 == 0) goto L_0x010b
            r3.add(r5)
            goto L_0x010b
        L_0x00e4:
            com.zipow.videobox.tempbean.IMessageTemplateAttachments r5 = com.zipow.videobox.tempbean.IMessageTemplateAttachments.parse(r5)
            if (r5 == 0) goto L_0x010b
            r3.add(r5)
            goto L_0x010b
        L_0x00ee:
            com.zipow.videobox.tempbean.IMessageTemplateFields r5 = com.zipow.videobox.tempbean.IMessageTemplateFields.parse(r5)
            if (r5 == 0) goto L_0x010b
            r3.add(r5)
            goto L_0x010b
        L_0x00f8:
            com.zipow.videobox.tempbean.IMessageTemplateSelect r5 = com.zipow.videobox.tempbean.IMessageTemplateSelect.parse(r5)
            if (r5 == 0) goto L_0x010b
            r3.add(r5)
            goto L_0x010b
        L_0x0102:
            com.zipow.videobox.tempbean.IMessageTemplateMessage r5 = com.zipow.videobox.tempbean.IMessageTemplateMessage.parse(r5)
            if (r5 == 0) goto L_0x010b
            r3.add(r5)
        L_0x010b:
            int r4 = r4 + 1
            goto L_0x004c
        L_0x010f:
            r1.setSections(r3)
        L_0x0112:
            java.lang.String r0 = "footer"
            boolean r0 = r9.has(r0)
            if (r0 == 0) goto L_0x012d
            java.lang.String r0 = "footer"
            com.google.gson.JsonElement r0 = r9.get(r0)
            boolean r3 = r0.isJsonPrimitive()
            if (r3 == 0) goto L_0x012d
            java.lang.String r0 = r0.getAsString()
            r1.setFooter(r0)
        L_0x012d:
            java.lang.String r0 = "footer_version"
            boolean r0 = r9.has(r0)
            if (r0 == 0) goto L_0x0148
            java.lang.String r0 = "footer_version"
            com.google.gson.JsonElement r0 = r9.get(r0)
            boolean r3 = r0.isJsonPrimitive()
            if (r3 == 0) goto L_0x0148
            int r0 = r0.getAsInt()
            r1.setFooter_version(r0)
        L_0x0148:
            java.lang.String r0 = "footer_fall_back"
            boolean r0 = r9.has(r0)
            if (r0 == 0) goto L_0x0163
            java.lang.String r0 = "footer_fall_back"
            com.google.gson.JsonElement r0 = r9.get(r0)
            boolean r3 = r0.isJsonPrimitive()
            if (r3 == 0) goto L_0x0163
            java.lang.String r0 = r0.getAsString()
            r1.setFooter_fall_back(r0)
        L_0x0163:
            java.lang.String r0 = "footer_icon"
            boolean r0 = r9.has(r0)
            if (r0 == 0) goto L_0x017e
            java.lang.String r0 = "footer_icon"
            com.google.gson.JsonElement r0 = r9.get(r0)
            boolean r3 = r0.isJsonPrimitive()
            if (r3 == 0) goto L_0x017e
            java.lang.String r0 = r0.getAsString()
            r1.setFooter_icon(r0)
        L_0x017e:
            java.lang.String r0 = "ts"
            boolean r0 = r9.has(r0)
            if (r0 == 0) goto L_0x0199
            java.lang.String r0 = "ts"
            com.google.gson.JsonElement r0 = r9.get(r0)
            boolean r3 = r0.isJsonPrimitive()
            if (r3 == 0) goto L_0x0199
            long r3 = r0.getAsLong()
            r1.setTs(r3)
        L_0x0199:
            java.lang.String r0 = "markdown"
            boolean r0 = r9.has(r0)
            if (r0 == 0) goto L_0x01b4
            java.lang.String r0 = "markdown"
            com.google.gson.JsonElement r0 = r9.get(r0)
            boolean r3 = r0.isJsonPrimitive()
            if (r3 == 0) goto L_0x01b4
            boolean r0 = r0.getAsBoolean()
            r1.setMarkdown(r0)
        L_0x01b4:
            java.lang.String r0 = "extracted_messages"
            boolean r0 = r9.has(r0)
            if (r0 == 0) goto L_0x01f4
            java.lang.String r0 = "extracted_messages"
            com.google.gson.JsonElement r9 = r9.get(r0)
            boolean r0 = r9.isJsonArray()
            if (r0 == 0) goto L_0x01f4
            com.google.gson.JsonArray r9 = r9.getAsJsonArray()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
        L_0x01d1:
            int r3 = r9.size()
            if (r2 >= r3) goto L_0x01f1
            com.google.gson.JsonElement r3 = r9.get(r2)
            boolean r4 = r3.isJsonObject()
            if (r4 == 0) goto L_0x01ee
            com.google.gson.JsonObject r3 = r3.getAsJsonObject()
            com.zipow.videobox.tempbean.IMessageTemplateExtendMessage r3 = com.zipow.videobox.tempbean.IMessageTemplateExtendMessage.parse(r3)
            if (r3 == 0) goto L_0x01ee
            r0.add(r3)
        L_0x01ee:
            int r2 = r2 + 1
            goto L_0x01d1
        L_0x01f1:
            r1.setExtendMessages(r0)
        L_0x01f4:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.tempbean.IMessageTemplateSection.parse(com.google.gson.JsonObject):com.zipow.videobox.tempbean.IMessageTemplateSection");
    }

    public String getSidebar_color() {
        return this.sidebar_color;
    }

    public void setSidebar_color(String str) {
        this.sidebar_color = str;
    }

    public List<IMessageTemplateBase> getSections() {
        return this.sections;
    }

    public void setSections(List<IMessageTemplateBase> list) {
        this.sections = list;
    }

    public String getFooter() {
        return this.footer;
    }

    public void setFooter(String str) {
        this.footer = str;
    }

    public String getFooter_icon() {
        return this.footer_icon;
    }

    public void setFooter_icon(String str) {
        this.footer_icon = str;
    }

    public long getTs() {
        return this.f333ts;
    }

    public void setTs(long j) {
        this.f333ts = j;
    }

    public boolean isMarkdown() {
        return this.markdown;
    }

    public void setMarkdown(boolean z) {
        this.markdown = z;
    }

    public int getFooter_version() {
        return this.footer_version;
    }

    public void setFooter_version(int i) {
        this.footer_version = i;
    }

    public String getFooter_fall_back() {
        return this.footer_fall_back;
    }

    public void setFooter_fall_back(String str) {
        this.footer_fall_back = str;
    }

    public List<IMessageTemplateExtendMessage> getExtendMessages() {
        return this.extendMessages;
    }

    public void setExtendMessages(List<IMessageTemplateExtendMessage> list) {
        MarkDownUtils.addMarkDownLabel(list);
        this.extendMessages = list;
    }

    public boolean isSupportFootItem() {
        ZoomMessageTemplate zoomMessageTemplate = PTApp.getInstance().getZoomMessageTemplate();
        if (zoomMessageTemplate == null) {
            return false;
        }
        return zoomMessageTemplate.isSupportItem("footer", this.footer_version);
    }
}
