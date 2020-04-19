package com.zipow.videobox.tempbean;

import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class IMessageTemplateImage extends IMessageTemplateBase {
    private String event_id;
    private String url;

    public void writeJson(@Nullable JsonWriter jsonWriter) throws IOException {
        if (jsonWriter != null) {
            jsonWriter.beginObject();
            super.writeJson(jsonWriter);
            if (this.event_id != null) {
                jsonWriter.name("event_id").value(this.event_id);
            }
            if (this.url != null) {
                jsonWriter.name("url").value(this.url);
            }
            jsonWriter.endObject();
        }
    }

    @Nullable
    public static IMessageTemplateImage parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateImage iMessageTemplateImage = (IMessageTemplateImage) parse(jsonObject, new IMessageTemplateImage());
        if (iMessageTemplateImage == null) {
            return null;
        }
        if (jsonObject.has("event_id")) {
            JsonElement jsonElement = jsonObject.get("event_id");
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateImage.setEvent_id(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("url")) {
            JsonElement jsonElement2 = jsonObject.get("url");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateImage.setUrl(jsonElement2.getAsString());
            }
        }
        return iMessageTemplateImage;
    }

    public String getEvent_id() {
        return this.event_id;
    }

    public void setEvent_id(String str) {
        this.event_id = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
