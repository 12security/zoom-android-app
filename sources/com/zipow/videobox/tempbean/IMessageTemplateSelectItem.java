package com.zipow.videobox.tempbean;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.zipow.videobox.util.ZMActionMsgUtil;
import java.io.IOException;

public class IMessageTemplateSelectItem {
    private String text;
    private String value;

    @Nullable
    public static IMessageTemplateSelectItem parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateSelectItem iMessageTemplateSelectItem = new IMessageTemplateSelectItem();
        if (jsonObject.has(ZMActionMsgUtil.KEY_EVENT)) {
            JsonElement jsonElement = jsonObject.get(ZMActionMsgUtil.KEY_EVENT);
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateSelectItem.setText(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("value")) {
            JsonElement jsonElement2 = jsonObject.get("value");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateSelectItem.setValue(jsonElement2.getAsString());
            }
        }
        return iMessageTemplateSelectItem;
    }

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        if (this.text != null) {
            jsonWriter.name(ZMActionMsgUtil.KEY_EVENT).value(this.text);
        }
        if (this.value != null) {
            jsonWriter.name("value").value(this.value);
        }
        jsonWriter.endObject();
    }

    public boolean equals(Object obj) {
        if (obj instanceof IMessageTemplateSelectItem) {
            return TextUtils.equals(((IMessageTemplateSelectItem) obj).getValue(), getValue());
        }
        return false;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }
}
