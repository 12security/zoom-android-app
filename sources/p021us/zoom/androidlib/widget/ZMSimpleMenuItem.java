package p021us.zoom.androidlib.widget;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;

/* renamed from: us.zoom.androidlib.widget.ZMSimpleMenuItem */
public class ZMSimpleMenuItem implements IZMMenuItem {
    private int mAction;
    private boolean mDisable;
    private Drawable mIcon;
    private String mLabel;
    private boolean mSelected;
    private int mTextColor;

    public ZMSimpleMenuItem() {
        this.mAction = 0;
        this.mSelected = false;
        this.mDisable = false;
        this.mTextColor = 0;
    }

    public ZMSimpleMenuItem(String str, Drawable drawable) {
        this(0, str, drawable, false);
    }

    public ZMSimpleMenuItem(int i, String str) {
        this(i, str, null, false);
    }

    public ZMSimpleMenuItem(int i, String str, int i2) {
        this.mAction = 0;
        this.mSelected = false;
        this.mDisable = false;
        this.mTextColor = 0;
        this.mAction = i;
        this.mLabel = str;
        this.mTextColor = i2;
    }

    public ZMSimpleMenuItem(int i, String str, Drawable drawable, boolean z) {
        this(i, str, drawable, z, false);
    }

    public ZMSimpleMenuItem(int i, String str, Drawable drawable, boolean z, boolean z2) {
        this.mAction = 0;
        this.mSelected = false;
        this.mDisable = false;
        this.mTextColor = 0;
        this.mAction = i;
        this.mLabel = str;
        this.mIcon = drawable;
        this.mSelected = z;
        this.mDisable = z2;
    }

    public void updateMenuItem(String str, boolean z, boolean z2) {
        this.mLabel = str;
        this.mSelected = z;
        this.mDisable = z2;
    }

    public void updateMenuItem(String str, Drawable drawable, boolean z, boolean z2) {
        this.mLabel = str;
        this.mIcon = drawable;
        this.mSelected = z;
        this.mDisable = z2;
    }

    @NonNull
    public String toString() {
        return this.mLabel;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public void setLabel(String str) {
        this.mLabel = str;
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
    }

    public int getAction() {
        return this.mAction;
    }

    public void setAction(int i) {
        this.mAction = i;
    }

    public boolean isSelected() {
        return this.mSelected;
    }

    public boolean isDisable() {
        return this.mDisable;
    }

    public void setmDisable(boolean z) {
        this.mDisable = z;
    }

    public void setSelected(boolean z) {
        this.mSelected = z;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
    }
}
