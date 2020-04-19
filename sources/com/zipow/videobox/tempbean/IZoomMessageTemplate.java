package com.zipow.videobox.tempbean;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;

public class IZoomMessageTemplate {
    private static final String TAG = "IZoomMessageTemplate";
    private List<IMessageTemplateBase> body;
    private IMessageTemplateHead head;
    private IMessageTemplateSettings settings;

    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.tempbean.IZoomMessageTemplate parse(@androidx.annotation.Nullable com.google.gson.JsonObject r8) {
        /*
            if (r8 != 0) goto L_0x0004
            r8 = 0
            return r8
        L_0x0004:
            com.zipow.videobox.tempbean.IZoomMessageTemplate r0 = new com.zipow.videobox.tempbean.IZoomMessageTemplate
            r0.<init>()
            java.lang.String r1 = "head"
            boolean r1 = r8.has(r1)
            if (r1 == 0) goto L_0x0028
            java.lang.String r1 = "head"
            com.google.gson.JsonElement r1 = r8.get(r1)
            boolean r2 = r1.isJsonObject()
            if (r2 == 0) goto L_0x0028
            com.google.gson.JsonObject r1 = r1.getAsJsonObject()
            com.zipow.videobox.tempbean.IMessageTemplateHead r1 = com.zipow.videobox.tempbean.IMessageTemplateHead.parse(r1)
            r0.setHead(r1)
        L_0x0028:
            java.lang.String r1 = "settings"
            boolean r1 = r8.has(r1)
            if (r1 == 0) goto L_0x0047
            java.lang.String r1 = "settings"
            com.google.gson.JsonElement r1 = r8.get(r1)
            boolean r2 = r1.isJsonObject()
            if (r2 == 0) goto L_0x0047
            com.google.gson.JsonObject r1 = r1.getAsJsonObject()
            com.zipow.videobox.tempbean.IMessageTemplateSettings r1 = com.zipow.videobox.tempbean.IMessageTemplateSettings.parse(r1)
            r0.setSettings(r1)
        L_0x0047:
            java.lang.String r1 = "body"
            boolean r1 = r8.has(r1)
            if (r1 == 0) goto L_0x0144
            java.lang.String r1 = "body"
            com.google.gson.JsonElement r8 = r8.get(r1)
            boolean r1 = r8.isJsonArray()
            if (r1 == 0) goto L_0x0144
            com.google.gson.JsonArray r8 = r8.getAsJsonArray()
            if (r8 == 0) goto L_0x0144
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r2 = 0
            r3 = 0
        L_0x0068:
            int r4 = r8.size()
            if (r3 >= r4) goto L_0x0141
            com.google.gson.JsonElement r4 = r8.get(r3)
            boolean r5 = r4.isJsonObject()
            if (r5 == 0) goto L_0x013d
            com.google.gson.JsonObject r4 = r4.getAsJsonObject()
            java.lang.String r5 = "type"
            boolean r5 = r4.has(r5)
            if (r5 == 0) goto L_0x013d
            java.lang.String r5 = "type"
            com.google.gson.JsonElement r5 = r4.get(r5)
            boolean r6 = r5.isJsonPrimitive()
            if (r6 == 0) goto L_0x013d
            java.lang.String r5 = r5.getAsString()
            r6 = -1
            int r7 = r5.hashCode()
            switch(r7) {
                case -1274708295: goto L_0x00d9;
                case -1161803523: goto L_0x00cf;
                case -906021636: goto L_0x00c5;
                case -738997328: goto L_0x00bb;
                case 100313435: goto L_0x00b1;
                case 954925063: goto L_0x00a7;
                case 1970241253: goto L_0x009d;
                default: goto L_0x009c;
            }
        L_0x009c:
            goto L_0x00e3
        L_0x009d:
            java.lang.String r7 = "section"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00e3
            r5 = 6
            goto L_0x00e4
        L_0x00a7:
            java.lang.String r7 = "message"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00e3
            r5 = 0
            goto L_0x00e4
        L_0x00b1:
            java.lang.String r7 = "image"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00e3
            r5 = 5
            goto L_0x00e4
        L_0x00bb:
            java.lang.String r7 = "attachments"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00e3
            r5 = 3
            goto L_0x00e4
        L_0x00c5:
            java.lang.String r7 = "select"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00e3
            r5 = 1
            goto L_0x00e4
        L_0x00cf:
            java.lang.String r7 = "actions"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00e3
            r5 = 4
            goto L_0x00e4
        L_0x00d9:
            java.lang.String r7 = "fields"
            boolean r5 = r5.equals(r7)
            if (r5 == 0) goto L_0x00e3
            r5 = 2
            goto L_0x00e4
        L_0x00e3:
            r5 = -1
        L_0x00e4:
            switch(r5) {
                case 0: goto L_0x0134;
                case 1: goto L_0x012a;
                case 2: goto L_0x0120;
                case 3: goto L_0x0116;
                case 4: goto L_0x010c;
                case 5: goto L_0x0102;
                case 6: goto L_0x00f8;
                default: goto L_0x00e7;
            }
        L_0x00e7:
            com.zipow.videobox.tempbean.IMessageTemplateSection r5 = new com.zipow.videobox.tempbean.IMessageTemplateSection
            r5.<init>()
            com.zipow.videobox.tempbean.IMessageTemplateBase r4 = com.zipow.videobox.tempbean.IMessageTemplateSection.parse(r4, r5)
            com.zipow.videobox.tempbean.IMessageTemplateSection r4 = (com.zipow.videobox.tempbean.IMessageTemplateSection) r4
            if (r4 == 0) goto L_0x013d
            r1.add(r4)
            goto L_0x013d
        L_0x00f8:
            com.zipow.videobox.tempbean.IMessageTemplateSection r4 = com.zipow.videobox.tempbean.IMessageTemplateSection.parse(r4)
            if (r4 == 0) goto L_0x013d
            r1.add(r4)
            goto L_0x013d
        L_0x0102:
            com.zipow.videobox.tempbean.IMessageTemplateImage r4 = com.zipow.videobox.tempbean.IMessageTemplateImage.parse(r4)
            if (r4 == 0) goto L_0x013d
            r1.add(r4)
            goto L_0x013d
        L_0x010c:
            com.zipow.videobox.tempbean.IMessageTemplateActions r4 = com.zipow.videobox.tempbean.IMessageTemplateActions.parse(r4)
            if (r4 == 0) goto L_0x013d
            r1.add(r4)
            goto L_0x013d
        L_0x0116:
            com.zipow.videobox.tempbean.IMessageTemplateAttachments r4 = com.zipow.videobox.tempbean.IMessageTemplateAttachments.parse(r4)
            if (r4 == 0) goto L_0x013d
            r1.add(r4)
            goto L_0x013d
        L_0x0120:
            com.zipow.videobox.tempbean.IMessageTemplateFields r4 = com.zipow.videobox.tempbean.IMessageTemplateFields.parse(r4)
            if (r4 == 0) goto L_0x013d
            r1.add(r4)
            goto L_0x013d
        L_0x012a:
            com.zipow.videobox.tempbean.IMessageTemplateSelect r4 = com.zipow.videobox.tempbean.IMessageTemplateSelect.parse(r4)
            if (r4 == 0) goto L_0x013d
            r1.add(r4)
            goto L_0x013d
        L_0x0134:
            com.zipow.videobox.tempbean.IMessageTemplateMessage r4 = com.zipow.videobox.tempbean.IMessageTemplateMessage.parse(r4)
            if (r4 == 0) goto L_0x013d
            r1.add(r4)
        L_0x013d:
            int r3 = r3 + 1
            goto L_0x0068
        L_0x0141:
            r0.setBody(r1)
        L_0x0144:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.tempbean.IZoomMessageTemplate.parse(com.google.gson.JsonObject):com.zipow.videobox.tempbean.IZoomMessageTemplate");
    }

    @NonNull
    public String toJSONString() {
        StringWriter stringWriter;
        Throwable th;
        Throwable th2;
        try {
            stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            try {
                writeJson(jsonWriter);
                String stringWriter2 = stringWriter.toString();
                jsonWriter.close();
                stringWriter.close();
                return stringWriter2;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                th = r3;
                th2 = th4;
            }
            throw th2;
            if (th != null) {
                try {
                    jsonWriter.close();
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                }
            } else {
                jsonWriter.close();
            }
            throw th2;
            throw th;
        } catch (Exception unused) {
            return "";
        } catch (Throwable th6) {
            r1.addSuppressed(th6);
        }
    }

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        if (this.head != null) {
            jsonWriter.name("head");
            this.head.writeJson(jsonWriter);
        }
        if (this.settings != null) {
            jsonWriter.name("settings");
            this.settings.writeJson(jsonWriter);
        }
        if (this.body != null) {
            jsonWriter.name("body");
            jsonWriter.beginArray();
            for (IMessageTemplateBase iMessageTemplateBase : this.body) {
                String type = iMessageTemplateBase.getType();
                char c = 65535;
                switch (type.hashCode()) {
                    case -1274708295:
                        if (type.equals("fields")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -1161803523:
                        if (type.equals("actions")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -906021636:
                        if (type.equals("select")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -738997328:
                        if (type.equals("attachments")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 954925063:
                        if (type.equals("message")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 1970241253:
                        if (type.equals("section")) {
                            c = 5;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        if (!(iMessageTemplateBase instanceof IMessageTemplateMessage)) {
                            break;
                        } else {
                            iMessageTemplateBase.writeJson(jsonWriter);
                            break;
                        }
                    case 1:
                        if (!(iMessageTemplateBase instanceof IMessageTemplateSelect)) {
                            break;
                        } else {
                            iMessageTemplateBase.writeJson(jsonWriter);
                            break;
                        }
                    case 2:
                        if (!(iMessageTemplateBase instanceof IMessageTemplateFields)) {
                            break;
                        } else {
                            iMessageTemplateBase.writeJson(jsonWriter);
                            break;
                        }
                    case 3:
                        if (!(iMessageTemplateBase instanceof IMessageTemplateAttachments)) {
                            break;
                        } else {
                            iMessageTemplateBase.writeJson(jsonWriter);
                            break;
                        }
                    case 4:
                        if (!(iMessageTemplateBase instanceof IMessageTemplateActions)) {
                            break;
                        } else {
                            iMessageTemplateBase.writeJson(jsonWriter);
                            break;
                        }
                    case 5:
                        if (!(iMessageTemplateBase instanceof IMessageTemplateSection)) {
                            break;
                        } else {
                            iMessageTemplateBase.writeJson(jsonWriter);
                            break;
                        }
                    default:
                        iMessageTemplateBase.writeJson(jsonWriter);
                        break;
                }
            }
            jsonWriter.endArray();
        }
        jsonWriter.endObject();
    }

    @Nullable
    public IMessageTemplateSelect findSelectItem(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        List<IMessageTemplateBase> list = this.body;
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (IMessageTemplateBase iMessageTemplateBase : this.body) {
            if (iMessageTemplateBase instanceof IMessageTemplateSelect) {
                IMessageTemplateSelect iMessageTemplateSelect = (IMessageTemplateSelect) iMessageTemplateBase;
                if (TextUtils.equals(iMessageTemplateSelect.getEvent_id(), str)) {
                    return iMessageTemplateSelect;
                }
            } else if (iMessageTemplateBase instanceof IMessageTemplateSection) {
                List<IMessageTemplateBase> sections = ((IMessageTemplateSection) iMessageTemplateBase).getSections();
                if (!CollectionsUtil.isListEmpty(sections)) {
                    for (IMessageTemplateBase iMessageTemplateBase2 : sections) {
                        if (iMessageTemplateBase2 instanceof IMessageTemplateSelect) {
                            IMessageTemplateSelect iMessageTemplateSelect2 = (IMessageTemplateSelect) iMessageTemplateBase2;
                            if (TextUtils.equals(iMessageTemplateSelect2.getEvent_id(), str)) {
                                return iMessageTemplateSelect2;
                            }
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return null;
    }

    @Nullable
    public Object findEditItem(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        List<IMessageTemplateBase> list = this.body;
        if (list == null || list.isEmpty()) {
            return null;
        }
        ArrayList<IMessageTemplateBase> arrayList = new ArrayList<>();
        for (IMessageTemplateBase iMessageTemplateBase : this.body) {
            if (iMessageTemplateBase instanceof IMessageTemplateSection) {
                List sections = ((IMessageTemplateSection) iMessageTemplateBase).getSections();
                if (!CollectionsUtil.isListEmpty(sections)) {
                    arrayList.addAll(sections);
                }
            } else if (iMessageTemplateBase instanceof IMessageTemplateMessage) {
                arrayList.add(iMessageTemplateBase);
            } else if (iMessageTemplateBase instanceof IMessageTemplateFields) {
                arrayList.add(iMessageTemplateBase);
            }
        }
        for (IMessageTemplateBase iMessageTemplateBase2 : arrayList) {
            if (iMessageTemplateBase2 instanceof IMessageTemplateMessage) {
                IMessageTemplateMessage iMessageTemplateMessage = (IMessageTemplateMessage) iMessageTemplateBase2;
                String event_id = iMessageTemplateMessage.getEvent_id();
                if (iMessageTemplateMessage.isEditable() && TextUtils.equals(str, event_id)) {
                    return iMessageTemplateMessage;
                }
            } else if (iMessageTemplateBase2 instanceof IMessageTemplateFields) {
                IMessageTemplateFields iMessageTemplateFields = (IMessageTemplateFields) iMessageTemplateBase2;
                if (TextUtils.equals(iMessageTemplateFields.getEvent_id(), str)) {
                    List<IMessageTemplateFieldItem> items = iMessageTemplateFields.getItems();
                    if (items != null) {
                        for (IMessageTemplateFieldItem iMessageTemplateFieldItem : items) {
                            if (iMessageTemplateFieldItem.isEditable() && TextUtils.equals(iMessageTemplateFieldItem.getKey(), str2)) {
                                return iMessageTemplateFieldItem;
                            }
                        }
                        continue;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return null;
    }

    public IMessageTemplateHead getHead() {
        return this.head;
    }

    public void setHead(IMessageTemplateHead iMessageTemplateHead) {
        this.head = iMessageTemplateHead;
    }

    public List<IMessageTemplateBase> getBody() {
        return this.body;
    }

    public void setBody(List<IMessageTemplateBase> list) {
        this.body = list;
    }

    public IMessageTemplateSettings getSettings() {
        return this.settings;
    }

    public void setSettings(IMessageTemplateSettings iMessageTemplateSettings) {
        this.settings = iMessageTemplateSettings;
    }
}
