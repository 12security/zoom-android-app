package p021us.zoom.androidlib.widget;

import android.content.Context;

/* renamed from: us.zoom.androidlib.widget.IZMListItem */
public interface IZMListItem {
    String getLabel();

    String getSubLabel();

    void init(Context context);

    boolean isSelected();
}
