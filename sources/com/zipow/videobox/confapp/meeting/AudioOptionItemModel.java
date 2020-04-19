package com.zipow.videobox.confapp.meeting;

public class AudioOptionItemModel<T> {
    public static final int FOOTER = 2;
    public static final int HEADER = 0;
    public static final int ITEM_COUNTRY = 1;
    public T data;
    public int type;

    public AudioOptionItemModel(int i, T t) {
        this.type = i;
        this.data = t;
    }
}
