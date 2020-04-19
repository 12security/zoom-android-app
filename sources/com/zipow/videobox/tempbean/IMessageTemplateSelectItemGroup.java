package com.zipow.videobox.tempbean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxGroup;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IMessageTemplateSelectItemGroup {
    private String group;
    private List<IMessageTemplateSelectItem> items;

    @Nullable
    public static IMessageTemplateSelectItemGroup parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateSelectItemGroup iMessageTemplateSelectItemGroup = new IMessageTemplateSelectItemGroup();
        if (jsonObject.has(BoxGroup.TYPE)) {
            JsonElement jsonElement = jsonObject.get(BoxGroup.TYPE);
            if (jsonElement != null) {
                iMessageTemplateSelectItemGroup.setGroup(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("items")) {
            ArrayList arrayList = new ArrayList();
            JsonArray asJsonArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < asJsonArray.size(); i++) {
                arrayList.add(IMessageTemplateSelectItem.parse(asJsonArray.get(i).getAsJsonObject()));
            }
            iMessageTemplateSelectItemGroup.setItems(arrayList);
        }
        return iMessageTemplateSelectItemGroup;
    }

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        if (this.group != null) {
            jsonWriter.name(BoxGroup.TYPE).value(this.group);
        }
        if (this.items != null) {
            jsonWriter.name("items");
            jsonWriter.beginArray();
            for (IMessageTemplateSelectItem writeJson : this.items) {
                writeJson.writeJson(jsonWriter);
            }
            jsonWriter.endArray();
        }
        jsonWriter.endObject();
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String str) {
        this.group = str;
    }

    public List<IMessageTemplateSelectItem> getItems() {
        return this.items;
    }

    public void setItems(List<IMessageTemplateSelectItem> list) {
        this.items = list;
    }
}
