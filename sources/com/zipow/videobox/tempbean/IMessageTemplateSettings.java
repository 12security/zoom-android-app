package com.zipow.videobox.tempbean;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class IMessageTemplateSettings {
    private String default_sidebar_color;
    private boolean is_split_sidebar;

    @Nullable
    public static IMessageTemplateSettings parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateSettings iMessageTemplateSettings = new IMessageTemplateSettings();
        if (jsonObject.has("is_split_sidebar")) {
            JsonElement jsonElement = jsonObject.get("is_split_sidebar");
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateSettings.setIs_split_sidebar(jsonElement.getAsBoolean());
            }
        }
        if (jsonObject.has("default_sidebar_color")) {
            JsonElement jsonElement2 = jsonObject.get("default_sidebar_color");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateSettings.setDefault_sidebar_color(jsonElement2.getAsString());
            }
        }
        return iMessageTemplateSettings;
    }

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("is_split_sidebar").value(this.is_split_sidebar);
        if (!TextUtils.isEmpty(this.default_sidebar_color)) {
            jsonWriter.name("default_sidebar_color").value(this.default_sidebar_color);
        }
        jsonWriter.endObject();
    }

    public boolean isIs_split_sidebar() {
        return this.is_split_sidebar;
    }

    public void setIs_split_sidebar(boolean z) {
        this.is_split_sidebar = z;
    }

    public String getDefault_sidebar_color() {
        return this.default_sidebar_color;
    }

    public void setDefault_sidebar_color(String str) {
        this.default_sidebar_color = str;
    }
}
