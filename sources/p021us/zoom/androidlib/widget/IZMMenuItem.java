package p021us.zoom.androidlib.widget;

import android.graphics.drawable.Drawable;

/* renamed from: us.zoom.androidlib.widget.IZMMenuItem */
public interface IZMMenuItem {
    int getAction();

    Drawable getIcon();

    String getLabel();

    int getTextColor();

    boolean isDisable();

    boolean isSelected();
}
