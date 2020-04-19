package com.zipow.videobox.tempbean;

import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessageTemplate;
import java.io.IOException;
import org.apache.http.cookie.ClientCookie;

public class IMessageTemplateBase {
    @Nullable
    private String fall_back = null;
    protected String type;
    private int version;

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void writeJson(@Nullable JsonWriter jsonWriter) throws IOException {
        if (jsonWriter != null) {
            if (this.type != null) {
                jsonWriter.name("type").value(this.type);
            }
            if (this.fall_back != null) {
                jsonWriter.name("fall_back").value(this.fall_back);
            }
            if (this.version >= 0) {
                jsonWriter.name(ClientCookie.VERSION_ATTR).value((long) this.version);
            }
        }
    }

    @Nullable
    public static <T extends IMessageTemplateBase> T parse(@Nullable JsonObject jsonObject, @Nullable T t) {
        if (t == null || jsonObject == null) {
            return null;
        }
        if (jsonObject.has("type")) {
            JsonElement jsonElement = jsonObject.get("type");
            if (jsonElement.isJsonPrimitive()) {
                t.setType(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("fall_back")) {
            JsonElement jsonElement2 = jsonObject.get("fall_back");
            if (jsonElement2.isJsonPrimitive()) {
                t.setFall_back(jsonElement2.getAsString());
            }
        }
        if (jsonObject.has(ClientCookie.VERSION_ATTR)) {
            JsonElement jsonElement3 = jsonObject.get(ClientCookie.VERSION_ATTR);
            if (jsonElement3 != null) {
                t.setVersion(jsonElement3.getAsInt());
            }
        }
        return t;
    }

    @Nullable
    public String getFall_back() {
        return this.fall_back;
    }

    public void setFall_back(@Nullable String str) {
        this.fall_back = str;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int i) {
        this.version = i;
    }

    public boolean isSupportItem() {
        ZoomMessageTemplate zoomMessageTemplate = PTApp.getInstance().getZoomMessageTemplate();
        if (zoomMessageTemplate == null) {
            return false;
        }
        return zoomMessageTemplate.isSupportItem(this.type, this.version);
    }
}
