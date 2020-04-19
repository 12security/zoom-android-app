package com.zipow.videobox.tempbean;

import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class IMessageTemplateAttachments extends IMessageTemplateBase {
    private String event_id;
    private String ext;
    private String img_url;
    private IMessageTemplateAttachmentInfo information;
    private String resource_url;
    private long size = -1;

    public void writeJson(@Nullable JsonWriter jsonWriter) throws IOException {
        if (jsonWriter != null) {
            jsonWriter.beginObject();
            super.writeJson(jsonWriter);
            if (this.resource_url != null) {
                jsonWriter.name("resource_url").value(this.resource_url);
            }
            if (this.img_url != null) {
                jsonWriter.name("img_url").value(this.img_url);
            }
            if (this.ext != null) {
                jsonWriter.name("ext").value(this.ext);
            }
            if (this.size >= 0) {
                jsonWriter.name("size").value(this.size);
            }
            if (this.information != null) {
                jsonWriter.name("information");
                this.information.writeJson(jsonWriter);
            }
            if (this.event_id != null) {
                jsonWriter.name("event_id").value(this.event_id);
            }
            jsonWriter.endObject();
        }
    }

    @Nullable
    public static IMessageTemplateAttachments parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateAttachments iMessageTemplateAttachments = (IMessageTemplateAttachments) parse(jsonObject, new IMessageTemplateAttachments());
        if (iMessageTemplateAttachments == null) {
            return null;
        }
        if (jsonObject.has("resource_url")) {
            JsonElement jsonElement = jsonObject.get("resource_url");
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateAttachments.setResource_url(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("img_url")) {
            JsonElement jsonElement2 = jsonObject.get("img_url");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateAttachments.setImg_url(jsonElement2.getAsString());
            }
        }
        if (jsonObject.has("ext")) {
            JsonElement jsonElement3 = jsonObject.get("ext");
            if (jsonElement3.isJsonPrimitive()) {
                iMessageTemplateAttachments.setExt(jsonElement3.getAsString());
            }
        }
        if (jsonObject.has("size")) {
            JsonElement jsonElement4 = jsonObject.get("size");
            if (jsonElement4.isJsonPrimitive()) {
                iMessageTemplateAttachments.setSize(jsonElement4.getAsLong());
            }
        }
        if (jsonObject.has("event_id")) {
            JsonElement jsonElement5 = jsonObject.get("event_id");
            if (jsonElement5.isJsonPrimitive()) {
                iMessageTemplateAttachments.setEvent_id(jsonElement5.getAsString());
            }
        }
        if (jsonObject.has("information")) {
            JsonElement jsonElement6 = jsonObject.get("information");
            if (jsonElement6.isJsonObject()) {
                iMessageTemplateAttachments.setInformation(IMessageTemplateAttachmentInfo.parse(jsonElement6.getAsJsonObject()));
            }
        }
        return iMessageTemplateAttachments;
    }

    public String getResource_url() {
        return this.resource_url;
    }

    public void setResource_url(String str) {
        this.resource_url = str;
    }

    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url(String str) {
        this.img_url = str;
    }

    public String getExt() {
        return this.ext;
    }

    public void setExt(String str) {
        this.ext = str;
    }

    public IMessageTemplateAttachmentInfo getInformation() {
        return this.information;
    }

    public void setInformation(IMessageTemplateAttachmentInfo iMessageTemplateAttachmentInfo) {
        this.information = iMessageTemplateAttachmentInfo;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public String getEvent_id() {
        return this.event_id;
    }

    public void setEvent_id(String str) {
        this.event_id = str;
    }
}
