package com.zipow.videobox.tempbean;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.zipow.videobox.util.ZMActionMsgUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IMessageTemplateSelect extends IMessageTemplateBase {
    public static final int TYPE_CHANNEL = 2;
    public static final int TYPE_CONTACT = 1;
    public static final int TYPE_CUSTOM = 3;
    private String event;
    private String event_id;
    private List<IMessageTemplateSelectItemGroup> groupItems;
    private boolean isProgressing = false;
    private List<IMessageTemplateSelectItem> selectedItems;
    private String source;
    private IMessageTemplateTextStyle style;
    private String text;

    @Nullable
    public static IMessageTemplateSelect parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateSelect iMessageTemplateSelect = (IMessageTemplateSelect) parse(jsonObject, new IMessageTemplateSelect());
        if (iMessageTemplateSelect == null) {
            return null;
        }
        if (jsonObject.has(ZMActionMsgUtil.KEY_EVENT)) {
            JsonElement jsonElement = jsonObject.get(ZMActionMsgUtil.KEY_EVENT);
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateSelect.setText(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("static_source")) {
            JsonElement jsonElement2 = jsonObject.get("static_source");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateSelect.setSource(jsonElement2.getAsString());
            }
        }
        if (jsonObject.has("style")) {
            JsonElement jsonElement3 = jsonObject.get("style");
            if (jsonElement3.isJsonObject()) {
                iMessageTemplateSelect.setStyle(IMessageTemplateTextStyle.parse(jsonElement3.getAsJsonObject()));
            }
        }
        if (jsonObject.has("event_id")) {
            JsonElement jsonElement4 = jsonObject.get("event_id");
            if (jsonElement4.isJsonPrimitive()) {
                iMessageTemplateSelect.setEvent_id(jsonElement4.getAsString());
            }
        }
        if (jsonObject.has("selected_items")) {
            ArrayList arrayList = new ArrayList();
            JsonElement jsonElement5 = jsonObject.get("selected_items");
            if (jsonElement5.isJsonArray()) {
                JsonArray asJsonArray = jsonElement5.getAsJsonArray();
                for (int i = 0; i < asJsonArray.size(); i++) {
                    JsonElement jsonElement6 = asJsonArray.get(i);
                    if (jsonElement6.isJsonObject()) {
                        arrayList.add(IMessageTemplateSelectItem.parse(jsonElement6.getAsJsonObject()));
                    }
                }
                iMessageTemplateSelect.setSelectedItems(arrayList);
            }
        }
        if (jsonObject.has("group_items")) {
            ArrayList arrayList2 = new ArrayList();
            JsonElement jsonElement7 = jsonObject.get("group_items");
            if (jsonElement7.isJsonArray()) {
                JsonArray asJsonArray2 = jsonElement7.getAsJsonArray();
                for (int i2 = 0; i2 < asJsonArray2.size(); i2++) {
                    JsonElement jsonElement8 = asJsonArray2.get(i2);
                    if (jsonElement8.isJsonObject()) {
                        arrayList2.add(IMessageTemplateSelectItemGroup.parse(jsonElement8.getAsJsonObject()));
                    }
                }
                iMessageTemplateSelect.setGroupItems(arrayList2);
            }
        }
        return iMessageTemplateSelect;
    }

    public void writeJson(@Nullable JsonWriter jsonWriter) throws IOException {
        if (jsonWriter != null) {
            jsonWriter.beginObject();
            if (this.text != null) {
                jsonWriter.name(ZMActionMsgUtil.KEY_EVENT).value(this.text);
            }
            if (this.source != null) {
                jsonWriter.name("static_source").value(this.source);
            }
            if (this.style != null) {
                jsonWriter.name("style");
                this.style.writeJson(jsonWriter);
            }
            if (this.selectedItems != null) {
                jsonWriter.name("selected_items");
                jsonWriter.beginArray();
                for (IMessageTemplateSelectItem writeJson : this.selectedItems) {
                    writeJson.writeJson(jsonWriter);
                }
                jsonWriter.endArray();
            }
            if (this.groupItems != null) {
                jsonWriter.name("group_items");
                jsonWriter.beginArray();
                for (IMessageTemplateSelectItemGroup writeJson2 : this.groupItems) {
                    writeJson2.writeJson(jsonWriter);
                }
                jsonWriter.endArray();
            }
            jsonWriter.endObject();
        }
    }

    public int getSelectType() {
        if (TextUtils.equals(this.source, "members")) {
            return 1;
        }
        return TextUtils.equals(this.source, "channels") ? 2 : 3;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String str) {
        this.event = str;
    }

    public String getEvent_id() {
        return this.event_id;
    }

    public void setEvent_id(String str) {
        this.event_id = str;
    }

    public IMessageTemplateTextStyle getStyle() {
        return this.style;
    }

    public void setStyle(IMessageTemplateTextStyle iMessageTemplateTextStyle) {
        this.style = iMessageTemplateTextStyle;
    }

    public List<IMessageTemplateSelectItem> getSelectedItems() {
        return this.selectedItems;
    }

    public void setSelectedItems(List<IMessageTemplateSelectItem> list) {
        this.selectedItems = list;
    }

    public List<IMessageTemplateSelectItemGroup> getGroupItems() {
        return this.groupItems;
    }

    public void setGroupItems(List<IMessageTemplateSelectItemGroup> list) {
        this.groupItems = list;
    }

    public boolean isProgressing() {
        return this.isProgressing;
    }

    public void setProgressing(boolean z) {
        this.isProgressing = z;
    }
}
