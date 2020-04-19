package com.zipow.videobox.tempbean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class IMessageTemplateAttachmentInfo {
    private IMessageTemplateAttachmentDescription description;
    private IMessageTemplateAttachmentTitle title;

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        if (this.title != null) {
            jsonWriter.name("title");
            this.title.writeJson(jsonWriter);
        }
        if (this.description != null) {
            jsonWriter.name(BoxItem.FIELD_DESCRIPTION);
            this.description.writeJson(jsonWriter);
        }
        jsonWriter.endObject();
    }

    @Nullable
    public static IMessageTemplateAttachmentInfo parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateAttachmentInfo iMessageTemplateAttachmentInfo = new IMessageTemplateAttachmentInfo();
        if (jsonObject.has("title")) {
            JsonElement jsonElement = jsonObject.get("title");
            if (jsonElement.isJsonObject()) {
                iMessageTemplateAttachmentInfo.setTitle(IMessageTemplateAttachmentTitle.parse(jsonElement.getAsJsonObject()));
            }
        }
        if (jsonObject.has(BoxItem.FIELD_DESCRIPTION)) {
            JsonElement jsonElement2 = jsonObject.get(BoxItem.FIELD_DESCRIPTION);
            if (jsonElement2.isJsonObject()) {
                iMessageTemplateAttachmentInfo.setDescription(IMessageTemplateAttachmentDescription.parse(jsonElement2.getAsJsonObject()));
            }
        }
        return iMessageTemplateAttachmentInfo;
    }

    public IMessageTemplateAttachmentTitle getTitle() {
        return this.title;
    }

    public void setTitle(IMessageTemplateAttachmentTitle iMessageTemplateAttachmentTitle) {
        this.title = iMessageTemplateAttachmentTitle;
    }

    public IMessageTemplateAttachmentDescription getDescription() {
        return this.description;
    }

    public void setDescription(IMessageTemplateAttachmentDescription iMessageTemplateAttachmentDescription) {
        this.description = iMessageTemplateAttachmentDescription;
    }
}
