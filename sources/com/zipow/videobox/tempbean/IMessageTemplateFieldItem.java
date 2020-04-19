package com.zipow.videobox.tempbean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.zipow.videobox.markdown.MarkDownUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IMessageTemplateFieldItem {
    private boolean editable;
    private String event;
    private List<IMessageTemplateExtendMessage> extendMessages;
    private boolean isName;
    private String key;
    private String link;
    private boolean markdown;
    private boolean shorts;
    private boolean showMore;
    private boolean showMoreExtend;
    private IMessageTemplateTextStyle style;
    private String value;

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        if (this.key != null) {
            jsonWriter.name("key").value(this.key);
        }
        if (this.value != null) {
            jsonWriter.name("value").value(this.value);
        }
        if (this.style != null) {
            jsonWriter.name("style");
            this.style.writeJson(jsonWriter);
        }
        if (this.link != null) {
            jsonWriter.name("link").value(this.link);
        }
        jsonWriter.name("short").value(this.shorts);
        jsonWriter.name("isName").value(this.isName);
        jsonWriter.name("editable").value(this.editable);
        if (this.event != null) {
            jsonWriter.name("event").value(this.event);
        }
        jsonWriter.name("markdown").value(this.markdown);
        if (this.extendMessages != null) {
            jsonWriter.name("extracted_messages");
            jsonWriter.beginArray();
            for (IMessageTemplateExtendMessage writeJson : this.extendMessages) {
                writeJson.writeJson(jsonWriter);
            }
            jsonWriter.endArray();
        }
        jsonWriter.endObject();
    }

    @Nullable
    public static IMessageTemplateFieldItem parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateFieldItem iMessageTemplateFieldItem = new IMessageTemplateFieldItem();
        if (jsonObject.has("key")) {
            JsonElement jsonElement = jsonObject.get("key");
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateFieldItem.setKey(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("value")) {
            JsonElement jsonElement2 = jsonObject.get("value");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateFieldItem.setValue(jsonElement2.getAsString());
            }
        }
        if (jsonObject.has("style")) {
            JsonElement jsonElement3 = jsonObject.get("style");
            if (jsonElement3.isJsonObject()) {
                iMessageTemplateFieldItem.setStyle(IMessageTemplateTextStyle.parse(jsonElement3.getAsJsonObject()));
            }
        }
        if (jsonObject.has("link")) {
            JsonElement jsonElement4 = jsonObject.get("link");
            if (jsonElement4.isJsonPrimitive()) {
                iMessageTemplateFieldItem.setLink(jsonElement4.getAsString());
            }
        }
        if (jsonObject.has("isName")) {
            JsonElement jsonElement5 = jsonObject.get("isName");
            if (jsonElement5.isJsonPrimitive()) {
                iMessageTemplateFieldItem.setName(jsonElement5.getAsBoolean());
            }
        }
        if (jsonObject.has("editable")) {
            JsonElement jsonElement6 = jsonObject.get("editable");
            if (jsonElement6.isJsonPrimitive()) {
                iMessageTemplateFieldItem.setEditable(jsonElement6.getAsBoolean());
            }
        }
        if (jsonObject.has("event")) {
            JsonElement jsonElement7 = jsonObject.get("event");
            if (jsonElement7.isJsonPrimitive()) {
                iMessageTemplateFieldItem.setEvent(jsonElement7.getAsString());
            }
        }
        if (jsonObject.has("short")) {
            JsonElement jsonElement8 = jsonObject.get("short");
            if (jsonElement8.isJsonPrimitive()) {
                iMessageTemplateFieldItem.setShorts(jsonElement8.getAsBoolean());
            }
        }
        if (jsonObject.has("markdown")) {
            JsonElement jsonElement9 = jsonObject.get("markdown");
            if (jsonElement9.isJsonPrimitive()) {
                iMessageTemplateFieldItem.setMarkdown(jsonElement9.getAsBoolean());
            }
        }
        if (jsonObject.has("extracted_messages")) {
            JsonElement jsonElement10 = jsonObject.get("extracted_messages");
            if (jsonElement10.isJsonArray()) {
                JsonArray asJsonArray = jsonElement10.getAsJsonArray();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < asJsonArray.size(); i++) {
                    JsonElement jsonElement11 = asJsonArray.get(i);
                    if (jsonElement11.isJsonObject()) {
                        IMessageTemplateExtendMessage parse = IMessageTemplateExtendMessage.parse(jsonElement11.getAsJsonObject());
                        if (parse != null) {
                            arrayList.add(parse);
                        }
                    }
                }
                iMessageTemplateFieldItem.setExtendMessages(arrayList);
            }
        }
        return iMessageTemplateFieldItem;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public IMessageTemplateTextStyle getStyle() {
        return this.style;
    }

    public void setStyle(IMessageTemplateTextStyle iMessageTemplateTextStyle) {
        this.style = iMessageTemplateTextStyle;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String str) {
        this.link = str;
    }

    public boolean isName() {
        return this.isName;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean z) {
        this.editable = z;
    }

    public void setName(boolean z) {
        this.isName = z;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String str) {
        this.event = str;
    }

    public boolean isShorts() {
        return this.shorts;
    }

    public void setShorts(boolean z) {
        this.shorts = z;
    }

    public boolean isMarkdown() {
        return this.markdown;
    }

    public void setMarkdown(boolean z) {
        this.markdown = z;
    }

    public List<IMessageTemplateExtendMessage> getExtendMessages() {
        return this.extendMessages;
    }

    public void setExtendMessages(List<IMessageTemplateExtendMessage> list) {
        MarkDownUtils.addMarkDownLabel(list);
        this.extendMessages = list;
    }

    public boolean isShowMore() {
        return this.showMore;
    }

    public void setShowMore(boolean z) {
        this.showMore = z;
    }

    public boolean isShowMoreExtend() {
        return this.showMoreExtend;
    }

    public void setShowMoreExtend(boolean z) {
        this.showMoreExtend = z;
    }
}
