package com.zipow.videobox.tempbean;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class IMessageTemplateTextStyle {
    private boolean bold;
    private String color;
    private boolean italic;

    @Nullable
    public static IMessageTemplateTextStyle parse(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        IMessageTemplateTextStyle iMessageTemplateTextStyle = new IMessageTemplateTextStyle();
        if (jsonObject.has("bold")) {
            JsonElement jsonElement = jsonObject.get("bold");
            if (jsonElement.isJsonPrimitive()) {
                iMessageTemplateTextStyle.setBold(jsonElement.getAsBoolean());
            }
        }
        if (jsonObject.has("color")) {
            JsonElement jsonElement2 = jsonObject.get("color");
            if (jsonElement2.isJsonPrimitive()) {
                iMessageTemplateTextStyle.setColor(jsonElement2.getAsString());
            }
        }
        if (jsonObject.has("italic")) {
            JsonElement jsonElement3 = jsonObject.get("italic");
            if (jsonElement3.isJsonPrimitive()) {
                iMessageTemplateTextStyle.setItalic(jsonElement3.getAsBoolean());
            }
        }
        return iMessageTemplateTextStyle;
    }

    public void writeJson(@NonNull JsonWriter jsonWriter) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("bold").value(this.bold);
        if (this.color != null) {
            jsonWriter.name("color").value(this.color);
        }
        jsonWriter.name("italic").value(this.italic);
        jsonWriter.endObject();
    }

    public void applyStyle(@Nullable TextView textView) {
        if (textView != null) {
            if (isBold() && isItalic()) {
                textView.setTypeface(Typeface.DEFAULT, 3);
            } else if (isBold()) {
                textView.setTypeface(Typeface.DEFAULT, 1);
            } else if (isItalic()) {
                textView.setTypeface(Typeface.DEFAULT, 2);
            } else {
                textView.setTypeface(Typeface.DEFAULT, 0);
            }
            if (!TextUtils.isEmpty(getColor())) {
                try {
                    textView.setTextColor(Color.parseColor(getColor()));
                } catch (Exception unused) {
                    if ("orange".equalsIgnoreCase(getColor())) {
                        textView.setTextColor(Color.parseColor("#FFA500"));
                    }
                }
            }
        }
    }

    public boolean isBold() {
        return this.bold;
    }

    public void setBold(boolean z) {
        this.bold = z;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String str) {
        this.color = str;
    }

    public boolean isItalic() {
        return this.italic;
    }

    public void setItalic(boolean z) {
        this.italic = z;
    }
}
