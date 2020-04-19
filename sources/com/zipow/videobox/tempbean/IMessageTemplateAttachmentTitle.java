package com.zipow.videobox.tempbean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.zipow.videobox.util.ZMActionMsgUtil;
import java.io.IOException;

public class IMessageTemplateAttachmentTitle {
    private IMessageTemplateTextStyle style;
    private String text;

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        if (this.text != null) {
            jsonWriter.name(ZMActionMsgUtil.KEY_EVENT).value(this.text);
        }
        if (this.style != null) {
            jsonWriter.name("style");
            this.style.writeJson(jsonWriter);
        }
        jsonWriter.endObject();
    }

    @Nullable
    public static IMessageTemplateAttachmentTitle parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateAttachmentTitle iMessageTemplateAttachmentTitle = new IMessageTemplateAttachmentTitle();
        if (jsonObject.has(ZMActionMsgUtil.KEY_EVENT)) {
            JsonElement jsonElement = jsonObject.get(ZMActionMsgUtil.KEY_EVENT);
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateAttachmentTitle.setText(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("style")) {
            JsonElement jsonElement2 = jsonObject.get("style");
            if (jsonElement2.isJsonObject()) {
                iMessageTemplateAttachmentTitle.setStyle(IMessageTemplateTextStyle.parse(jsonElement2.getAsJsonObject()));
            }
        }
        return iMessageTemplateAttachmentTitle;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public IMessageTemplateTextStyle getStyle() {
        return this.style;
    }

    public void setStyle(IMessageTemplateTextStyle iMessageTemplateTextStyle) {
        this.style = iMessageTemplateTextStyle;
    }
}
