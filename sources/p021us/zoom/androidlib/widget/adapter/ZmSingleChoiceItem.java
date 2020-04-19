package p021us.zoom.androidlib.widget.adapter;

/* renamed from: us.zoom.androidlib.widget.adapter.ZmSingleChoiceItem */
public class ZmSingleChoiceItem<T> {
    private T data;
    private String iconContentDescription;
    private int imgIconRes;
    private boolean isSelected;
    private String title;

    public ZmSingleChoiceItem(T t, String str, int i, String str2) {
        this.data = t;
        this.title = str;
        this.imgIconRes = i;
        this.iconContentDescription = str2;
    }

    public ZmSingleChoiceItem(T t, String str, int i, String str2, boolean z) {
        this.data = t;
        this.title = str;
        this.isSelected = z;
        this.imgIconRes = i;
        this.iconContentDescription = str2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public int getImgIconRes() {
        return this.imgIconRes;
    }

    public String getIconContentDescription() {
        return this.iconContentDescription;
    }

    public T getData() {
        return this.data;
    }
}
