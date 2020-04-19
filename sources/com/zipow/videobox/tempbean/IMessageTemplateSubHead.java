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

public class IMessageTemplateSubHead extends IMessageTemplateBase {
    private List<IMessageTemplateExtendMessage> extendMessages;
    private String link;
    private boolean markdown;
    private IMessageTemplateTextStyle style;
    private String text;

    @Nullable
    public static IMessageTemplateSubHead parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateSubHead iMessageTemplateSubHead = (IMessageTemplateSubHead) parse(jsonObject, new IMessageTemplateSubHead());
        if (iMessageTemplateSubHead == null) {
            return null;
        }
        iMessageTemplateSubHead.setType("sub_head");
        if (jsonObject.has(ZMActionMsgUtil.KEY_EVENT)) {
            JsonElement jsonElement = jsonObject.get(ZMActionMsgUtil.KEY_EVENT);
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateSubHead.setText(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("link")) {
            JsonElement jsonElement2 = jsonObject.get("link");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateSubHead.setLink(jsonElement2.getAsString());
            }
        }
        if (jsonObject.has("style")) {
            JsonElement jsonElement3 = jsonObject.get("style");
            if (jsonElement3.isJsonObject()) {
                iMessageTemplateSubHead.setStyle(IMessageTemplateTextStyle.parse(jsonElement3.getAsJsonObject()));
            }
        }
        if (jsonObject.has("markdown")) {
            JsonElement jsonElement4 = jsonObject.get("markdown");
            if (jsonElement4.isJsonPrimitive()) {
                iMessageTemplateSubHead.setMarkdown(jsonElement4.getAsBoolean());
            }
        }
        if (jsonObject.has("extracted_messages")) {
            JsonElement jsonElement5 = jsonObject.get("extracted_messages");
            if (jsonElement5.isJsonArray()) {
                JsonArray asJsonArray = jsonElement5.getAsJsonArray();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < asJsonArray.size(); i++) {
                    JsonElement jsonElement6 = asJsonArray.get(i);
                    if (jsonElement6.isJsonObject()) {
                        IMessageTemplateExtendMessage parse = IMessageTemplateExtendMessage.parse(jsonElement6.getAsJsonObject());
                        if (parse != null) {
                            arrayList.add(parse);
                        }
                    }
                }
                iMessageTemplateSubHead.setExtendMessages(arrayList);
            }
        }
        return iMessageTemplateSubHead;
    }

    public void writeJson(@Nullable JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        super.writeJson(jsonWriter);
        if (this.text != null) {
            jsonWriter.name(ZMActionMsgUtil.KEY_EVENT).value(this.text);
        }
        if (this.link != null) {
            jsonWriter.name("link").value(this.link);
        }
        jsonWriter.name("markdown").value(this.markdown);
        if (this.style != null) {
            jsonWriter.name("style");
            this.style.writeJson(jsonWriter);
        }
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
}
