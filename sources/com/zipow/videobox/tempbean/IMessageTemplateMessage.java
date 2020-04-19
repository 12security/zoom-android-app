package com.zipow.videobox.tempbean;

import androidx.annotation.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.zipow.videobox.markdown.MarkDownUtils;
import com.zipow.videobox.util.ZMActionMsgUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IMessageTemplateMessage extends IMessageTemplateBase {
    private boolean editable;
    private String event;
    private String event_id;
    private List<IMessageTemplateExtendMessage> extendMessages;
    private String link;
    private boolean markdown;
    private boolean showMore;
    private boolean showMoreExtend;
    private IMessageTemplateTextStyle style;
    private String text;

    public void writeJson(@Nullable JsonWriter jsonWriter) throws IOException {
        if (jsonWriter != null) {
            jsonWriter.beginObject();
            super.writeJson(jsonWriter);
            if (this.text != null) {
                jsonWriter.name(ZMActionMsgUtil.KEY_EVENT).value(this.text);
            }
            if (this.link != null) {
                jsonWriter.name("link").value(this.link);
            }
            if (this.style != null) {
                jsonWriter.name("style");
                this.style.writeJson(jsonWriter);
            }
            jsonWriter.name("editable").value(this.editable);
            if (this.event_id != null) {
                jsonWriter.name("event_id").value(this.event_id);
            }
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
    }

    @Nullable
    public static IMessageTemplateMessage parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateMessage iMessageTemplateMessage = (IMessageTemplateMessage) parse(jsonObject, new IMessageTemplateMessage());
        if (iMessageTemplateMessage == null) {
            return null;
        }
        if (jsonObject.has(ZMActionMsgUtil.KEY_EVENT)) {
            JsonElement jsonElement = jsonObject.get(ZMActionMsgUtil.KEY_EVENT);
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateMessage.setText(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("link")) {
            JsonElement jsonElement2 = jsonObject.get("link");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateMessage.setLink(jsonElement2.getAsString());
            }
        }
        if (jsonObject.has("style")) {
            JsonElement jsonElement3 = jsonObject.get("style");
            if (jsonElement3.isJsonObject()) {
                iMessageTemplateMessage.setStyle(IMessageTemplateTextStyle.parse(jsonElement3.getAsJsonObject()));
            }
        }
        if (jsonObject.has("editable")) {
            JsonElement jsonElement4 = jsonObject.get("editable");
            if (jsonElement4.isJsonPrimitive()) {
                iMessageTemplateMessage.setEditable(jsonElement4.getAsBoolean());
            }
        }
        if (jsonObject.has("event_id")) {
            JsonElement jsonElement5 = jsonObject.get("event_id");
            if (jsonElement5.isJsonPrimitive()) {
                iMessageTemplateMessage.setEvent_id(jsonElement5.getAsString());
            }
        }
        if (jsonObject.has("event")) {
            JsonElement jsonElement6 = jsonObject.get("event");
            if (jsonElement6.isJsonPrimitive()) {
                iMessageTemplateMessage.setEvent(jsonElement6.getAsString());
            }
        }
        if (jsonObject.has("markdown")) {
            JsonElement jsonElement7 = jsonObject.get("markdown");
            if (jsonElement7.isJsonPrimitive()) {
                iMessageTemplateMessage.setMarkdown(jsonElement7.getAsBoolean());
            }
        }
        if (jsonObject.has("extracted_messages")) {
            JsonElement jsonElement8 = jsonObject.get("extracted_messages");
            if (jsonElement8.isJsonArray()) {
                JsonArray asJsonArray = jsonElement8.getAsJsonArray();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < asJsonArray.size(); i++) {
                    JsonElement jsonElement9 = asJsonArray.get(i);
                    if (jsonElement9.isJsonObject()) {
                        IMessageTemplateExtendMessage parse = IMessageTemplateExtendMessage.parse(jsonElement9.getAsJsonObject());
                        if (parse != null) {
                            arrayList.add(parse);
                        }
                    }
                }
                iMessageTemplateMessage.setExtendMessages(arrayList);
            }
        }
        return iMessageTemplateMessage;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String str) {
        this.link = str;
    }

    public IMessageTemplateTextStyle getStyle() {
        return this.style;
    }

    public void setStyle(IMessageTemplateTextStyle iMessageTemplateTextStyle) {
        this.style = iMessageTemplateTextStyle;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean z) {
        this.editable = z;
    }

    public String getEvent_id() {
        return this.event_id;
    }

    public void setEvent_id(String str) {
        this.event_id = str;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String str) {
        this.event = str;
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
