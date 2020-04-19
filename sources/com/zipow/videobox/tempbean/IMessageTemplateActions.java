package com.zipow.videobox.tempbean;

import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IMessageTemplateActions extends IMessageTemplateBase {
    private String event_id;
    private List<IMessageTemplateActionItem> items;
    private int limit;

    public void writeJson(@Nullable JsonWriter jsonWriter) throws IOException {
        if (jsonWriter != null) {
            jsonWriter.beginObject();
            super.writeJson(jsonWriter);
            if (this.limit >= 0) {
                jsonWriter.name(BoxList.FIELD_LIMIT).value((long) this.limit);
            }
            if (this.event_id != null) {
                jsonWriter.name("event_id").value(this.event_id);
            }
            if (this.items != null) {
                jsonWriter.name("items");
                jsonWriter.beginArray();
                for (IMessageTemplateActionItem writeJson : this.items) {
                    writeJson.writeJson(jsonWriter);
                }
                jsonWriter.endArray();
            }
            jsonWriter.endObject();
        }
    }

    @Nullable
    public static IMessageTemplateActions parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateActions iMessageTemplateActions = (IMessageTemplateActions) parse(jsonObject, new IMessageTemplateActions());
        if (iMessageTemplateActions == null) {
            return null;
        }
        if (jsonObject.has(BoxList.FIELD_LIMIT)) {
            JsonElement jsonElement = jsonObject.get(BoxList.FIELD_LIMIT);
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateActions.setLimit(jsonElement.getAsInt());
            }
        }
        if (jsonObject.has("event_id")) {
            JsonElement jsonElement2 = jsonObject.get("event_id");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateActions.setEvent_id(jsonElement2.getAsString());
            }
        }
        if (jsonObject.has("items")) {
            JsonElement jsonElement3 = jsonObject.get("items");
            if (jsonElement3.isJsonArray()) {
                ArrayList arrayList = new ArrayList();
                JsonArray asJsonArray = jsonElement3.getAsJsonArray();
                for (int i = 0; i < asJsonArray.size(); i++) {
                    arrayList.add(IMessageTemplateActionItem.parse(asJsonArray.get(i).getAsJsonObject()));
                }
                iMessageTemplateActions.setItems(arrayList);
            }
        }
        return iMessageTemplateActions;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int i) {
        this.limit = i;
    }

    public List<IMessageTemplateActionItem> getItems() {
        return this.items;
    }

    public void setItems(List<IMessageTemplateActionItem> list) {
        this.items = list;
    }

    public String getEvent_id() {
        return this.event_id;
    }

    public void setEvent_id(String str) {
        this.event_id = str;
    }
}
