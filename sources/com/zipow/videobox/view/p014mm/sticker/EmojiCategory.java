package com.zipow.videobox.view.p014mm.sticker;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.zipow.videobox.view.mm.sticker.EmojiCategory */
public class EmojiCategory {
    @NonNull
    private List<CommonEmoji> emojis = new ArrayList();
    private int iconResource;
    private String label;
    private String name;

    public String getName() {
        return this.name;
    }

    public String getLabel() {
        return this.label;
    }

    @NonNull
    public List<CommonEmoji> getEmojis() {
        return this.emojis;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public int getIconResource() {
        return this.iconResource;
    }

    public void setIconResource(int i) {
        this.iconResource = i;
    }
}
