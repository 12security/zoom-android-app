package com.zipow.videobox.tempbean;

import android.text.TextUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.zipow.videobox.util.ZMActionMsgUtil;
import java.io.IOException;
import p021us.zoom.videomeetings.C4558R;

public class IMessageTemplateActionItem {
    public static final String STYLE_DANGER = "danger";
    public static final String STYLE_DEFAULT = "default";
    public static final String STYLE_DISABLED = "disabled";
    public static final String STYLE_PRIMARY = "primary";
    private String style;
    private String text;
    private String value;

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        if (this.text != null) {
            jsonWriter.name(ZMActionMsgUtil.KEY_EVENT).value(this.text);
        }
        if (this.style != null) {
            jsonWriter.name("style").value(this.style);
        }
        if (this.value != null) {
            jsonWriter.name("value").value(this.value);
        }
        jsonWriter.endObject();
    }

    @Nullable
    public static IMessageTemplateActionItem parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateActionItem iMessageTemplateActionItem = new IMessageTemplateActionItem();
        if (jsonObject.has(ZMActionMsgUtil.KEY_EVENT)) {
            JsonElement jsonElement = jsonObject.get(ZMActionMsgUtil.KEY_EVENT);
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateActionItem.setText(jsonElement.getAsString());
            }
        }
        if (jsonObject.has("value")) {
            JsonElement jsonElement2 = jsonObject.get("value");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateActionItem.setValue(jsonElement2.getAsString());
            }
        }
        if (jsonObject.has("style")) {
            JsonElement jsonElement3 = jsonObject.get("style");
            if (jsonElement3.isJsonPrimitive()) {
                iMessageTemplateActionItem.setStyle(jsonElement3.getAsString());
            }
        }
        return iMessageTemplateActionItem;
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

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String str) {
        this.style = str;
    }

    public boolean isDisabled() {
        return "disabled".equalsIgnoreCase(this.style);
    }

    public void applayStyle(@Nullable TextView textView) {
        if (textView != null) {
            if (TextUtils.isEmpty(this.style)) {
                this.style = "default";
            }
            textView.setEnabled(true);
            if (STYLE_DANGER.equalsIgnoreCase(this.style)) {
                textView.setBackgroundResource(C4558R.C4559drawable.zm_msg_template_action_btn_danger_bg);
                textView.setTextColor(ContextCompat.getColorStateList(textView.getContext(), C4558R.color.zm_msg_template_action_danger_btn_text_color));
            } else if (STYLE_PRIMARY.equalsIgnoreCase(this.style)) {
                textView.setBackgroundResource(C4558R.C4559drawable.zm_msg_template_action_btn_primary_bg);
                textView.setTextColor(ContextCompat.getColorStateList(textView.getContext(), C4558R.color.zm_msg_template_action_primary_btn_text_color));
            } else if ("default".equalsIgnoreCase(this.style)) {
                textView.setBackgroundResource(C4558R.C4559drawable.zm_msg_template_action_btn_normal_bg);
                textView.setTextColor(ContextCompat.getColorStateList(textView.getContext(), C4558R.color.zm_msg_template_action_normal_btn_text_color));
            } else {
                textView.setEnabled(false);
            }
        }
    }
}
