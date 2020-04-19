package com.zipow.videobox.view.p014mm.sticker;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* renamed from: com.zipow.videobox.view.mm.sticker.CommonEmoji */
public class CommonEmoji {
    private static final Set<String> ILLEGALS = new HashSet();
    private String category;
    private List<String> diversities;
    private String diversity;
    private List<CommonEmoji> diversityEmojis;
    private List<String> genders;
    private String key;
    private int localImgRes = -1;
    private CommonEmoji manEmoji;
    private List<String> matches;
    private String name;
    private int order;
    private CharSequence output;
    private String shortName;
    private CommonEmoji womanEmoji;

    static {
        ILLEGALS.add("1f595");
        ILLEGALS.add("1f1f9-1f1fc");
    }

    public String getCategory() {
        return this.category;
    }

    public int getLocalImgRes() {
        return this.localImgRes;
    }

    public List<CommonEmoji> getDiversityEmojis() {
        return this.diversityEmojis;
    }

    public void addDiversityEmoji(@Nullable CommonEmoji commonEmoji) {
        if (commonEmoji != null) {
            if (this.diversityEmojis == null) {
                this.diversityEmojis = new ArrayList();
            }
            this.diversityEmojis.add(commonEmoji);
        }
    }

    public boolean isIllegal() {
        String str = this.key;
        return str != null && ILLEGALS.contains(str);
    }

    public CommonEmoji getManEmoji() {
        return this.manEmoji;
    }

    public CommonEmoji getWomanEmoji() {
        return this.womanEmoji;
    }

    public int getOrder() {
        return this.order;
    }

    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public CharSequence getOutput() {
        return this.output;
    }

    public String getKey() {
        return this.key;
    }

    public String getDiversity() {
        return this.diversity;
    }

    public void setCategory(String str) {
        this.category = str;
    }

    public void setLocalImgRes(int i) {
        this.localImgRes = i;
    }

    public void setManEmoji(CommonEmoji commonEmoji) {
        this.manEmoji = commonEmoji;
    }

    public void setWomanEmoji(CommonEmoji commonEmoji) {
        this.womanEmoji = commonEmoji;
    }

    public void setOrder(int i) {
        this.order = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setShortName(String str) {
        this.shortName = str;
    }

    public void setGenders(List<String> list) {
        this.genders = list;
    }

    public void setDiversities(List<String> list) {
        this.diversities = list;
    }

    public void setMatches(List<String> list) {
        this.matches = list;
    }

    public void setOutput(CharSequence charSequence) {
        this.output = charSequence;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public void setDiversity(String str) {
        this.diversity = str;
    }

    public List<String> getGenders() {
        return this.genders;
    }

    public List<String> getDiversities() {
        return this.diversities;
    }

    public List<String> getMatches() {
        return this.matches;
    }
}
