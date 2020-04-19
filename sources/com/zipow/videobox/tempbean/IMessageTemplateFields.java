package com.zipow.videobox.tempbean;

import androidx.annotation.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IMessageTemplateFields extends IMessageTemplateBase {
    private String event_id;
    private List<IMessageTemplateFieldItem> items;

    @Nullable
    public static IMessageTemplateFields parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateFields iMessageTemplateFields = (IMessageTemplateFields) parse(jsonObject, new IMessageTemplateFields());
        if (iMessageTemplateFields == null) {
            return null;
        }
        if (jsonObject.has("items")) {
            ArrayList arrayList = new ArrayList();
            JsonElement jsonElement = jsonObject.get("items");
            if (jsonElement.isJsonArray()) {
                JsonArray asJsonArray = jsonElement.getAsJsonArray();
                for (int i = 0; i < asJsonArray.size(); i++) {
                    JsonElement jsonElement2 = asJsonArray.get(i);
                    if (jsonElement2.isJsonObject()) {
                        arrayList.add(IMessageTemplateFieldItem.parse(jsonElement2.getAsJsonObject()));
                    }
                }
                iMessageTemplateFields.setItems(arrayList);
            }
        }
        if (jsonObject.has("event_id")) {
            JsonElement jsonElement3 = jsonObject.get("event_id");
            if (jsonElement3.isJsonPrimitive()) {
                iMessageTemplateFields.setEvent_id(jsonElement3.getAsString());
            }
        }
        return iMessageTemplateFields;
    }

    public void writeJson(@Nullable JsonWriter jsonWriter) throws IOException {
        if (jsonWriter != null) {
            jsonWriter.beginObject();
            super.writeJson(jsonWriter);
            if (this.items != null) {
                jsonWriter.name("items");
                jsonWriter.beginArray();
                for (IMessageTemplateFieldItem writeJson : this.items) {
                    writeJson.writeJson(jsonWriter);
                }
                jsonWriter.endArray();
            }
            if (this.event_id != null) {
                jsonWriter.name("event_id").value(this.event_id);
            }
            jsonWriter.endObject();
        }
    }

    public List<IMessageTemplateFieldItem> getItems() {
        return this.items;
    }

    public void setItems(List<IMessageTemplateFieldItem> list) {
        this.items = list;
    }

    public String getEvent_id() {
        return this.event_id;
    }

    public void setEvent_id(String str) {
        this.event_id = str;
    }
}
